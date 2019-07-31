package com.atomic.hadoop.tenant.service.impl;

import com.atomic.common.constant.UserConstants;
import com.atomic.common.core.domain.Ztree;
import com.atomic.common.core.text.Convert;
import com.atomic.framework.util.ShiroUtils;
import com.atomic.hadoop.common.hdfs.HadoopConstants;
import com.atomic.hadoop.tenant.config.annotation.ProjectScope;
import com.atomic.hadoop.tenant.domain.HadoopProject;
import com.atomic.hadoop.tenant.domain.HadoopProjectDept;
import com.atomic.hadoop.tenant.mapper.HadoopProjectMapper;
import com.atomic.hadoop.tenant.service.IHadoopProjectDeptService;
import com.atomic.hadoop.tenant.service.IHadoopProjectService;
import com.atomic.system.domain.SysDept;
import com.atomic.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 工程项目 服务层实现
 * 
 * @author atomic
 * @date 2019-04-26
 */
@Service
public class HadoopProjectServiceImpl implements IHadoopProjectService {
	@Autowired
	private HadoopProjectMapper hadoopProjectMapper;
	@Autowired
	private IHadoopProjectDeptService hadoopProjectDeptService;
	@Autowired
	private ISysDeptService deptService;

	/**
     * 查询工程项目信息
     * 
     * @param id 工程项目ID
     * @return 工程项目信息
     */
    @Override
	public HadoopProject selectHadoopProjectById(Integer id) {
	    return hadoopProjectMapper.selectHadoopProjectById(id);
	}
	
	/**
     * 查询工程项目列表
     * 
     * @param hadoopProject 工程项目信息
     * @return 工程项目集合
     */
	@Override
    @ProjectScope(tableAlias = "t1")
	public List<HadoopProject> selectHadoopProjectList(HadoopProject hadoopProject) {
	    return hadoopProjectMapper.selectHadoopProjectList(hadoopProject);
	}
	
    /**
     * 新增工程项目
     * 
     * @param hadoopProject 工程项目信息
     * @return 结果
     */
	@Override
	public int insertHadoopProject(HadoopProject hadoopProject) {
		hadoopProject.setProjectStatus(1);
		hadoopProject.setCreateTime(new Date());
		hadoopProject.setCreateUser(ShiroUtils.getLoginName());
		hadoopProject.setProjectSchedule(30.23);
	    return hadoopProjectMapper.insertHadoopProject(hadoopProject);
	}
	
	/**
     * 修改工程项目
     * 
     * @param hadoopProject 工程项目信息
     * @return 结果
     */
	@Override
	public int updateHadoopProject(HadoopProject hadoopProject) {
		hadoopProject.setUpdateUser(ShiroUtils.getLoginName());
		hadoopProject.setUpdateTime(new Date());
	    return hadoopProjectMapper.updateHadoopProject(hadoopProject);
	}

	/**
     * 删除工程项目对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteHadoopProjectByIds(String ids) {
		return hadoopProjectMapper.deleteHadoopProjectByIds(Convert.toStrArray(ids));
	}

	@Override
	public String checkProjectNameUnique(HadoopProject hadoopProject) {
		int count=hadoopProjectMapper.checkProjectNameUnique(hadoopProject.getNameCn());
		if (count>0){
			return HadoopConstants.PROJECT_NAME_NOT_UNIQUE;
		}
		return HadoopConstants.PROJECT_NAME_UNIQUE;
	}

	/**
	 * 根据工程获取授权和未授权的组织树
	 * @param hadoopProject
	 * @return
	 */
	@Transactional
	@Override
	public List<Ztree> projectDeptTreeData(HadoopProject hadoopProject) {
		HadoopProjectDept hadoopProjectDept = new HadoopProjectDept();
		hadoopProjectDept.setProjectId(hadoopProject.getProjectId());
		List<HadoopProjectDept> hadoopProjectDepts = hadoopProjectDeptService.selectHadoopProjectDeptList(hadoopProjectDept);
		List<SysDept> deptList = deptService.selectDeptList(new SysDept());
		List<String> projectDepts = new ArrayList<>();
		for (HadoopProjectDept hpd:hadoopProjectDepts){
			projectDepts.add(hpd.getDeptId()+"");
		}
		return initTree(deptList,projectDepts);
	}

	public List<Ztree> initTree(List<SysDept> deptList, List<String> hadoopProjectDepts){
		List<Ztree> ztrees = new ArrayList<>();
		for (SysDept dept : deptList) {
			if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
				Ztree ztree = new Ztree();
				ztree.setId(dept.getDeptId());
				ztree.setpId(dept.getParentId());
				ztree.setName(dept.getDeptName());
				ztree.setTitle(dept.getDeptName());
				ztree.setChecked(hadoopProjectDepts.contains(dept.getDeptId()+""));
				ztrees.add(ztree);
			}
		}
		return ztrees;
	}

}
