package com.atomic.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.atomic.system.domain.SysDept;

/**
 * 组织管理 数据层
 *
 * @author atomic
 */
public interface SysDeptMapper {
    /**
     * 查询组织人数
     *
     * @param dept 组织信息
     * @return 结果
     */
    int selectDeptCount(SysDept dept);

    /**
     * 查询组织是否存在用户
     *
     * @param deptId 组织ID
     * @return 结果
     */
    int checkDeptExistUser(Long deptId);

    /**
     * 查询组织管理数据
     *
     * @param dept 组织信息
     * @return 组织信息集合
     */
    List<SysDept> selectDeptList(SysDept dept);

    /**
     * 删除组织管理信息
     *
     * @param deptId 组织ID
     * @return 结果
     */
    int deleteDeptById(Long deptId);

    /**
     * 新增组织信息
     *
     * @param dept 组织信息
     * @return 结果
     */
    int insertDept(SysDept dept);

    /**
     * 修改组织信息
     *
     * @param dept 组织信息
     * @return 结果
     */
    int updateDept(SysDept dept);

    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
    int updateDeptChildren(@Param("depts") List<SysDept> depts);

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
     * @param deptName 组织名称
     * @param parentId 父组织ID
     * @return 结果
     */
    SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);

    /**
     * 根据角色ID查询组织
     *
     * @param roleId 角色ID
     * @return 组织列表
     */
    List<String> selectRoleDeptTree(Long roleId);

    /**
     * 修改所在组织的父级组织状态
     *
     * @param dept 组织
     */
    void updateDeptStatus(SysDept dept);
}
