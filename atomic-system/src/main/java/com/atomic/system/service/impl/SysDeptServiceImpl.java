package com.atomic.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.atomic.common.annotation.DataScope;
import com.atomic.common.constant.UserConstants;
import com.atomic.common.core.domain.Ztree;
import com.atomic.common.exception.BusinessException;
import com.atomic.common.utils.StringUtils;
import com.atomic.system.domain.SysDept;
import com.atomic.system.domain.SysRole;
import com.atomic.system.mapper.SysDeptMapper;
import com.atomic.system.service.ISysDeptService;

/**
 * 组织管理 服务实现
 *
 * @author atomic
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {
    @Autowired
    private SysDeptMapper deptMapper;

    /**
     * 查询组织管理数据
     *
     * @param dept 组织信息
     * @return 组织信息集合
     */
    @Override
    @DataScope(tableAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept) {
        return deptMapper.selectDeptList(dept);
    }

    /**
     * 查询组织管理树
     *
     * @param dept 组织信息
     * @return 所有组织信息
     */
    @Override
    @DataScope(tableAlias = "d")
    public List<Ztree> selectDeptTree(SysDept dept) {
        List<SysDept> deptList = deptMapper.selectDeptList(dept);
        List<Ztree> ztrees = initZtree(deptList);
        return ztrees;
    }

    /**
     * 根据角色ID查询组织（数据权限）
     *
     * @param role 角色对象
     * @return 组织列表（数据权限）
     */
    @Override
    public List<Ztree> roleDeptTreeData(SysRole role) {
        Long roleId = role.getRoleId();
        List<Ztree> ztrees = new ArrayList<Ztree>();
        List<SysDept> deptList = selectDeptList(new SysDept());
        if (StringUtils.isNotNull(roleId)) {
            List<String> roleDeptList = deptMapper.selectRoleDeptTree(roleId);
            ztrees = initZtree(deptList, roleDeptList);
        } else {
            ztrees = initZtree(deptList);
        }
        return ztrees;
    }

    /**
     * 对象转组织树
     *
     * @param deptList 组织列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysDept> deptList) {
        return initZtree(deptList, null);
    }

    /**
     * 对象转组织树
     *
     * @param deptList     组织列表
     * @param roleDeptList 角色已存在菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysDept> deptList, List<String> roleDeptList) {

        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (SysDept dept : deptList) {
            if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
                Ztree ztree = new Ztree();
                ztree.setId(dept.getDeptId());
                ztree.setpId(dept.getParentId());
                ztree.setName(dept.getDeptName());
                ztree.setTitle(dept.getDeptName());
                if (isCheck) {
                    ztree.setChecked(roleDeptList.contains(dept.getDeptId() + dept.getDeptName()));
                }
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }

    /**
     * 查询组织人数
     *
     * @param parentId 组织ID
     * @return 结果
     */
    @Override
    public int selectDeptCount(Long parentId) {
        SysDept dept = new SysDept();
        dept.setParentId(parentId);
        return deptMapper.selectDeptCount(dept);
    }

    /**
     * 查询组织是否存在用户
     *
     * @param deptId 组织ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0 ? true : false;
    }

    /**
     * 删除组织管理信息
     *
     * @param deptId 组织ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(Long deptId) {
        return deptMapper.deleteDeptById(deptId);
    }

    /**
     * 新增保存组织信息
     *
     * @param dept 组织信息
     * @return 结果
     */
    @Override
    public int insertDept(SysDept dept) {
        SysDept info = deptMapper.selectDeptById(dept.getParentId());
        // 如果父节点不为"正常"状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new BusinessException("组织停用，不允许新增");
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        return deptMapper.insertDept(dept);
    }

    /**
     * 修改保存组织信息
     *
     * @param dept 组织信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDept(SysDept dept) {
        SysDept info = deptMapper.selectDeptById(dept.getParentId());
        if (StringUtils.isNotNull(info)) {
            String ancestors = info.getAncestors() + "," + info.getDeptId();
            dept.setAncestors(ancestors);
            updateDeptChildren(dept.getDeptId(), ancestors);
        }
        int result = deptMapper.updateDept(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
            // 如果该组织是启用状态，则启用该组织的所有上级组织
            updateParentDeptStatus(dept);
        }
        return result;
    }

    /**
     * 修改该组织的父级组织状态
     *
     * @param dept 当前组织
     */
    private void updateParentDeptStatus(SysDept dept) {
        String updateBy = dept.getUpdateBy();
        dept = deptMapper.selectDeptById(dept.getDeptId());
        dept.setUpdateBy(updateBy);
        deptMapper.updateDeptStatus(dept);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId    组织ID
     * @param ancestors 元素列表
     */
    public void updateDeptChildren(Long deptId, String ancestors) {
        SysDept dept = new SysDept();
        dept.setParentId(deptId);
        List<SysDept> childrens = deptMapper.selectDeptList(dept);
        for (SysDept children : childrens) {
            children.setAncestors(ancestors + "," + dept.getParentId());
        }
        if (childrens.size() > 0) {
            deptMapper.updateDeptChildren(childrens);
        }
    }

    /**
     * 根据组织ID查询信息
     *
     * @param deptId 组织ID
     * @return 组织信息
     */
    @Override
    public SysDept selectDeptById(Long deptId) {
        return deptMapper.selectDeptById(deptId);
    }

    /**
     * 校验组织名称是否唯一
     *
     * @param dept 组织信息
     * @return 结果
     */
    @Override
    public String checkDeptNameUnique(SysDept dept) {
        Long deptId = StringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        if (StringUtils.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue()) {
            return UserConstants.DEPT_NAME_NOT_UNIQUE;
        }
        return UserConstants.DEPT_NAME_UNIQUE;
    }
}
