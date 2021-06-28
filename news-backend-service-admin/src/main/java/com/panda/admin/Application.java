package com.panda.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.panda.admin.mapper")
@ComponentScan("com.panda")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
