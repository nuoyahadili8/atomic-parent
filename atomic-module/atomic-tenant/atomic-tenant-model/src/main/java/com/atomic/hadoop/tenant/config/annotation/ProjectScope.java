package com.atomic.hadoop.tenant.config.annotation;

import java.lang.annotation.*;

/**
 * @Project:
 * @Description: 工程项目数据权限过滤注解
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/4/27/027 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProjectScope {
    /**
     * 表的别名
     */
    String tableAlias() default "";
}
