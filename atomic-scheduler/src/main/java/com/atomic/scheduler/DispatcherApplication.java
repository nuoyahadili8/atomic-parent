package com.atomic.scheduler;

import org.springframework.core.env.Environment;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Project:
 * @Description:
 * @Version 1.0.0
 * @Throws SystemException:
 * @Author: <li>2019/6/21/021 Administrator Create 1.0
 * @Copyright ©2018-2019 al.github
 * @Modified By:
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan({"com.atomic.*.mapper", "com.atomic.*.*.mapper"})
@ComponentScan({"com.atomic.*"})
@EnableDubbo
public class DispatcherApplication {

//    public static void main(String[] args) {
//        SpringApplication.run(DispatcherApplication.class, args);
//    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(DispatcherApplication.class)
                .listeners((ApplicationListener<ApplicationEnvironmentPreparedEvent>) event -> {
                    new EmbeddedZooKeeper(2181, false).start();
                })
                .run(args);
    }
}
