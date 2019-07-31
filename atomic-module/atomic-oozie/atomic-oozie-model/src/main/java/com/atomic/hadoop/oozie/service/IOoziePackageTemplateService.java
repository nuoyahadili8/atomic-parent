package com.atomic.hadoop.oozie.service;

import com.atomic.hadoop.oozie.domain.OoziePackageTemplate;

import java.util.List;

/**
 * 包模板 服务层
 *
 * @author atomic
 * @date 2019-05-12
 */
public interface IOoziePackageTemplateService {
    /**
     * 查询包模板信息
     *
     * @param packageId 包模板ID
     * @return 包模板信息
     */
    OoziePackageTemplate selectOoziePackageTemplateById(String packageId);

    /**
     * 查询包模板列表
     *
     * @param ooziePackageTemplate 包模板信息
     * @return 包模板集合
     */
    List<OoziePackageTemplate> selectOoziePackageTemplateList(OoziePackageTemplate ooziePackageTemplate);

    List<OoziePackageTemplate> selectPackageWithNotUsedOfCommonPackage(OoziePackageTemplate ooziePackageTemplate);

    /**
     * 新增包模板
     *
     * @param ooziePackageTemplate 包模板信息
     * @return 结果
     */
    int insertOoziePackageTemplate(OoziePackageTemplate ooziePackageTemplate);

    /**
     * 修改包模板
     *
     * @param ooziePackageTemplate 包模板信息
     * @return 结果
     */
    int updateOoziePackageTemplate(OoziePackageTemplate ooziePackageTemplate);

    /**
     * 删除包模板信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteOoziePackageTemplateByIds(String ids);

    /**
     * 保存oozie package template，workflow.xml，DAG流程图节点信息
     * @param packageId
     * @param flowJson
     * @return
     */
    int saveOoziePackageTemplateAndUploadHdfs(String packageId, String flowJson) throws Exception;

}
