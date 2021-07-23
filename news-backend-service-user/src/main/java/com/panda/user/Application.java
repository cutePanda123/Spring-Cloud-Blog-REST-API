package com.panda.user;

import com.mongodb.client.gridfs.GridFSBucket;
import com.panda.api.config.GridFsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication()
@MapperScan(basePackages = "com.panda.user.mapper")
@ComponentScan(
        basePackages = "com.panda",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GridFsConfig.class)
)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
