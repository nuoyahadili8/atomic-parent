package com.atomic.hadoop.tenant.service.impl;

import com.atomic.common.core.text.Convert;
import com.atomic.hadoop.tenant.domain.HadoopProject;
import com.atomic.hadoop.tenant.domain.HadoopProjectDept;
import com.atomic.hadoop.tenant.mapper.HadoopProjectDeptMapper;
import com.atomic.hadoop.tenant.mapper.HadoopProjectMapper;
import com.atomic.hadoop.tenant.service.IHadoopProjectDeptService;
import com.atomic.system.domain.SysDept;
import com.atomic.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 组织与工程关系 服务层实现
 *
 * @author atomic
 * @date 2019-05-01
 */
@Service
public class HadoopProjectDeptServiceImpl implements IHadoopProjectDeptService {
    @Autowired
    private HadoopProjectDeptMapper hadoopProjectDeptMapper;
    @Autowired
    private HadoopProjectMapper hadoopProjectMapper;
    @Autowired
    private ISysDeptService sysDeptService;


    /**
     * 查询组织与工程关系信息
     *
     * @param projectId 组织与工程关系ID
     * @return 组织与工程关系信息
     */
    @Override
    public HadoopProjectDept selectHadoopProjectDeptById(Integer projectId) {
        return hadoopProjectDeptMapper.selectHadoopProjectDeptById(projectId);
    }

    /**
     * 查询组织与工程关系列表
     *
     * @param hadoopProjectDept 组织与工程关系信息
     * @return 组织与工程关系集合
     */
    @Override
    public List<HadoopProjectDept> selectHadoopProjectDeptList(HadoopProjectDept hadoopProjectDept) {
        return hadoopProjectDeptMapper.selectHadoopProjectDeptList(hadoopProjectDept);
    }

    /**
     * 新增组织与工程关系
     *
     * @param hadoopProjectDept 组织与工程关系信息
     * @return 结果
     */
    @Override
    public int insertHadoopProjectDept(HadoopProjectDept hadoopProjectDept) {
        return hadoopProjectDeptMapper.insertHadoopProjectDept(hadoopProjectDept);
    }

    /**
     * 修改组织与工程关系
     *
     * @param hadoopProjectDept 组织与工程关系信息
     * @return 结果
     */
    @Override
    public int updateHadoopProjectDept(HadoopProjectDept hadoopProjectDept) {
        return hadoopProjectDeptMapper.updateHadoopProjectDept(hadoopProjectDept);
    }

    /**
     * 删除组织与工程关系对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHadoopProjectDeptByIds(String ids) {
        return hadoopProjectDeptMapper.deleteHadoopProjectDeptByIds(Convert.toStrArray(ids));
    }

    /**
     * 更新项目与组织关系表
     * @param deptIds
     * @param projectId
     * @return
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public int saveOrUpdateHadoopProjectDept(String deptIds, String projectId) {
        int result = 0;
        Set<String> deptIdSet =new HashSet<>();
        deptIdSet.addAll(Arrays.asList(deptIds.split(",")));
        HadoopProject hadoopProject = hadoopProjectMapper.selectHadoopProjectById(Integer.parseInt(projectId));
        SysDept sysDept = sysDeptService.selectDeptById(Long.parseLong(hadoopProject.getDemanderDeptId() + ""));
        String deptIdsStr=sysDept.getAncestors();
        deptIdSet.addAll(Arrays.asList(deptIdsStr.split(",")));
        deptIdSet.add(sysDept.getDeptId()+"");

        for (String deptId:deptIdSet){
            HadoopProjectDept hpd = new HadoopProjectDept();
            hpd.setProjectId(Integer.parseInt(projectId));
            hpd.setDeptId(Long.parseLong(deptId));
            try{
                result += hadoopProjectDeptMapper.insertHadoopProjectDept(hpd);
            }catch (Exception e){

            }

        }
        return result;
    }

}
