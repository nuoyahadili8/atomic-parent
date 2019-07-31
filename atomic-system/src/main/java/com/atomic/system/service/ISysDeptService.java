package com.atomic.system.service;

import java.util.List;

import com.atomic.common.core.domain.Ztree;
import com.atomic.system.domain.SysDept;
import com.atomic.system.domain.SysRole;

/**
 * 组织管理 服务层
 *
 * @author atomic
 */
public interface ISysDeptService {
    /**
     * 查询组织管理数据
     *
     * @param dept 组织信息
     * @return 组织信息集合
     */
    List<SysDept> selectDeptList(SysDept dept);

    /**
     * 查询组织管理树
     *
     * @param dept 组织信息
     * @return 所有组织信息
     */
    List<Ztree> selectDeptTree(SysDept dept);

    /**
     * 根据角色ID查询菜单
     *
     * @param role 角色对象
     * @return 菜单列表
     */
    List<Ztree> roleDeptTreeData(SysRole role);

    /**
     * 查询组织人数
     *
     * @param parentId 父组织ID
     * @return 结果
     */
    int selectDeptCount(Long parentId);

    /**
     * 查询组织是否存在用户
     *
     * @param deptId 组织ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkDeptExistUser(Long deptId);

    /**
     * 删除组织管理信息
     *
     * @param deptId 组织ID
     * @return 结果
     */
    int deleteDeptById(Long deptId);

    /**
     * 新增保存组织信息
     *
     * @param dept 组织信息
     * @return 结果
     */
    int insertDept(SysDept dept);

    /**
     * 修改保存组织信息
     *
     * @param dept 组织信息
     * @return 结果
     */
    int updateDept(SysDept dept);

    /**
     * 根据组织ID查询信息
     *
     * @param deptId 组织ID
     * @return 组织信息
     */
    SysDept selectDeptById(Long deptId);

    /**
     * 校验组织名称是否唯一
     *
     * @param dept 组织信息
     * @return 结果
     */
    String checkDeptNameUnique(SysDept dept);
}
