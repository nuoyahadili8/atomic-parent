package com.atomic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

/**
 * 启动程序
 *
 * @author atomic
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan({"com.atomic.*.mapper","com.atomic.*.*.mapper","com.atomic.*.*.*.mapper"})
@ComponentScan("com.atomic.*")
public class AtomicApplication {
    public static void main(String[] args) throws FileNotFoundException {
        // System.setProperty("spring.devtools.restart.enabled", "false");
//        new SpringApplicationBuilder(AtomicApplication.class)
//                .listeners((ApplicationListener<ApplicationEnvironmentPreparedEvent>) event -> {
//                    new EmbeddedZooKeeper(2181, false).start();
//                })
//                .run(args);
        SpringApplication.run(AtomicApplication.class, args);
        System.out.println("############  Atomic启动成功 ############\n");
    }
}