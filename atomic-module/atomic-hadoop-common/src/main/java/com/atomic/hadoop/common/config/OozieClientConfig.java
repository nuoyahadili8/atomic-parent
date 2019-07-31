package com.atomic.hadoop.common.config;

import com.atomic.hadoop.common.oozie.utils.OozieClientUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/7/5/005 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@Data
@Configuration
public class OozieClientConfig {

    @Value("${oozie.app.local.root}")
    private String oozieAppRootLocal;

    @Value("${oozie.app.hdfs.root}")
    private String oozieAppRootHdfs;

    @Value("${oozie.app.isUploadHDFS}")
    private boolean isUploadHDFS;

    @Bean("oozieClientUtils")
    public OozieClientUtils getOozieClientUtils(){
        return new OozieClientUtils();
    }
}
