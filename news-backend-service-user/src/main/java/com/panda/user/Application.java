package com.panda.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@MapperScan(basePackages = "com.panda.user.mapper")
@ComponentScan("com.panda")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
