package com.atomic.scheduler.mapper;

import com.atomic.scheduler.constant.QuartzConstants;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/6/12/012 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
public class AtomicJdbcMapperHandler {


    public static List<Map<String,String>> getHadoopPlatformList(JdbcTemplate jdbcTemplate,String hadoopPlatformId){
        final List<Map<String,String>> result = new ArrayList();
        jdbcTemplate.query(QuartzConstants.QUERY_HADOOP_PLATFORM_SQL, new Object[] {hadoopPlatformId}, rs -> {
            Map<String,String> row = new HashMap(16);
            row.put("id",rs.getString("id"));
            row.put("name",rs.getString("name"));
            row.put("hadoopVersion",rs.getString("hadoop_version"));
            row.put("defaultFS",rs.getString("default_fS"));
            row.put("hadoopConfig",rs.getString("hadoop_config"));
            row.put("rmUrl",rs.getString("rm_url"));
            row.put("hsUrl",rs.getString("hs_url"));
            row.put("oozieUrl",rs.getString("oozie_url"));
            row.put("isSecurity",rs.getString("is_security"));
            row.put("isEnable",rs.getString("is_enable"));
            row.put("createUser",rs.getString("create_user"));
            row.put("createTime",rs.getString("create_time"));
            result.add(row);
        });
        return result;
    }
}
