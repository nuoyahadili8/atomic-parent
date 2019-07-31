package com.atomic.hadoop.oozie.service.impl;

import com.atomic.common.core.text.Convert;
import com.atomic.framework.shiro.service.IdGen;
import com.atomic.framework.util.ShiroUtils;
import com.atomic.hadoop.common.config.OozieClientConfig;
import com.atomic.hadoop.common.oozie.service.IWorkflowService;
import com.atomic.hadoop.common.oozie.vo.ConfCredentialVo;
import com.atomic.hadoop.oozie.config.OozieConstants;
import com.atomic.hadoop.oozie.domain.OozieHiveSql;
import com.atomic.hadoop.oozie.domain.OoziePackageTemplate;
import com.atomic.hadoop.oozie.mapper.OoziePackageTemplateMapper;
import com.atomic.hadoop.oozie.service.IOozieHiveSqlService;
import com.atomic.hadoop.oozie.service.IOoziePackageTemplateService;
import com.atomic.hadoop.tenant.domain.HadoopPlatform;
import com.atomic.hadoop.tenant.service.IOozieCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包模板 服务层实现
 *
 * @author atomic
 * @date 2019-05-12
 */
@Service
public class OoziePackageTemplateServiceImpl implements IOoziePackageTemplateService {
    @Autowired
    private OoziePackageTemplateMapper ooziePackageTemplateMapper;

    @Autowired
    private IWorkflowService workflowService;

    @Autowired
    private IOozieHiveSqlService oozieHiveSqlService;

    @Autowired
    private OozieClientConfig oozieClientConfig;

    @Autowired
    private IOozieCredentialService oozieCredentialService;

    /**
     * 查询包模板信息
     *
     * @param packageId 包模板ID
     * @return 包模板信息
     */
    @Override
    public OoziePackageTemplate selectOoziePackageTemplateById(String packageId) {
        return ooziePackageTemplateMapper.selectOoziePackageTemplateById(packageId);
    }

    /**
     * 查询包模板列表
     *
     * @param ooziePackageTemplate 包模板信息
     * @return 包模板集合
     */
    @Override
    public List<OoziePackageTemplate> selectOoziePackageTemplateList(OoziePackageTemplate ooziePackageTemplate) {
        return ooziePackageTemplateMapper.selectOoziePackageTemplateList(ooziePackageTemplate);
    }

    @Override
    public List<OoziePackageTemplate> selectPackageWithNotUsedOfCommonPackage(OoziePackageTemplate ooziePackageTemplate) {
        return ooziePackageTemplateMapper.selectPackageWithNotUsedOfCommonPackage(ooziePackageTemplate);
    }

    /**
     * 新增包模板
     *
     * @param ooziePackageTemplate 包模板信息
     * @return 结果
     */
    @Override
    public int insertOoziePackageTemplate(OoziePackageTemplate ooziePackageTemplate) {
        String packageId = IdGen.getCounter(OoziePackageTemplate.class.getSimpleName());
        ooziePackageTemplate.setPackageId(packageId);
        ooziePackageTemplate.setCreateTime(new Date());
        ooziePackageTemplate.setCreateUser(ShiroUtils.getLoginName());
        ooziePackageTemplate.setPackageStatus(OozieConstants.PACKAGE_STATUS_NEW);
        return ooziePackageTemplateMapper.insertOoziePackageTemplate(ooziePackageTemplate);
    }

    /**
     * 修改包模板
     *
     * @param ooziePackageTemplate 包模板信息
     * @return 结果
     */
    @Override
    public int updateOoziePackageTemplate(OoziePackageTemplate ooziePackageTemplate) {
        return ooziePackageTemplateMapper.updateOoziePackageTemplate(ooziePackageTemplate);
    }

    /**
     * 删除包模板对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOoziePackageTemplateByIds(String ids) {
        return ooziePackageTemplateMapper.deleteOoziePackageTemplateByIds(Convert.toStrArray(ids));
    }

    /**
     * 保存oozie package template，workflow.xml，DAG流程图节点信息
     * @param packageId
     * @param flowJson
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveOoziePackageTemplateAndUploadHdfs(String packageId, String flowJson) throws Exception {
        OoziePackageTemplate ooziePackageTemplate = selectOoziePackageTemplateById(packageId);
        ooziePackageTemplate.setWorkflowJson(flowJson);
        HadoopPlatform hadoopPlatform = ooziePackageTemplate.getHadoopPlatform();
        List<ConfCredentialVo> credList = oozieCredentialService.getPackageCredList(hadoopPlatform.getId(), ooziePackageTemplate.getConHive2Id(), ooziePackageTemplate.getConHcatalogId(), ooziePackageTemplate.getConHbaseId());
        String flowXml = "";
        if (credList !=null && credList.size()>0){
            flowXml = workflowService.workflowJsonToXml(ooziePackageTemplate.getNameEn(), flowJson, credList);
        }else {
            flowXml = workflowService.workflowJsonToXml(ooziePackageTemplate.getNameEn(), flowJson);
        }

        ooziePackageTemplate.setWorkflowXml(flowXml);
        ooziePackageTemplate.setPackageStatus("2");
        updateOoziePackageTemplate(ooziePackageTemplate);

        //更新脚本信息，删除多余且与节点无关的脚本
        List<Integer> nodeIds = workflowService.getNodeIds(flowJson);
        Map<String, Object> params = new HashMap();
        params.put("taskId", packageId);
        params.put("nodeIds", nodeIds);
        List<OozieHiveSql> oozieHiveSqls = oozieHiveSqlService.selectPreDeleteScript(params);
        StringBuilder scriptIds = new StringBuilder();
        for (int i = 0; i < oozieHiveSqls.size(); i++) {
            scriptIds.append(oozieHiveSqls.get(i).getId());
            if (i < oozieHiveSqls.size() - 1) {
                scriptIds.append(",");
            }
        }
        oozieHiveSqlService.deleteOozieHiveSqlByIds(scriptIds.toString());
        return 1;
    }

}
