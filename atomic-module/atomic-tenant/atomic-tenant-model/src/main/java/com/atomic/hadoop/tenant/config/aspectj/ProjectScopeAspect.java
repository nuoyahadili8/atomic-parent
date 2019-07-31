package com.atomic.hadoop.tenant.config.aspectj;

import cn.hutool.core.collection.CollUtil;
import com.atomic.common.core.domain.BaseEntity;
import com.atomic.common.utils.StringUtils;
import com.atomic.framework.util.ShiroUtils;
import com.atomic.hadoop.tenant.config.annotation.ProjectScope;
import com.atomic.system.domain.SysPost;
import com.atomic.system.domain.SysUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/4/27/027 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Aspect
@Component
public class ProjectScopeAspect {
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "projectScope";

    @Pointcut("@annotation(com.atomic.hadoop.tenant.config.annotation.ProjectScope)")
    public void ProjectScopePointCut(){

    }

    @Before("ProjectScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable {
        handleProjectScope(point);
    }

    protected void handleProjectScope(final JoinPoint joinPoint) {
        // 获得注解
        ProjectScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null) {
            return;
        }
        // 获取当前的用户
        SysUser currentUser = ShiroUtils.getSysUser();
        if (currentUser != null) {
            // 如果是超级管理员，则不过滤数据
            if (!currentUser.isAdmin()) {
                projectScopeFilter(joinPoint, currentUser, controllerDataScope.tableAlias());
            }
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param user      用户
     * @param alias     别名
     */
    public static void projectScopeFilter(JoinPoint joinPoint, SysUser user, String alias) {
        StringBuilder sqlString = new StringBuilder();

        List<SysPost> posts = user.getPosts();
        if (!CollUtil.isEmpty(posts)){
            for (SysPost sysPost : posts){
                if (sysPost.getPostId() == 1L){
                    sqlString = new StringBuilder();
                    break;
                }else{
                    sqlString.append(StringUtils.format(
                            " OR {}.project_id IN ( SELECT hpd.project_id FROM hadoop_project_dept hpd WHERE hpd.dept_id = {} ) ", alias,
                            user.getDept().getDeptId()));
                }
            }
            if (StringUtils.isNotBlank(sqlString.toString())) {
                BaseEntity baseEntity = (BaseEntity) joinPoint.getArgs()[0];
                baseEntity.getParams().put(DATA_SCOPE, " (" + sqlString.substring(4) + ")");
            }
        } else{
            //没有分配岗位的用户无法查看工程项目
            BaseEntity baseEntity = (BaseEntity) joinPoint.getArgs()[0];
            baseEntity.getParams().put(DATA_SCOPE, " 1=2 ");
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private ProjectScope getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(ProjectScope.class);
        }
        return null;
    }

}
