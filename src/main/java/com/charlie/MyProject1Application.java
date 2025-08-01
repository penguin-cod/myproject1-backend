package com.charlie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.charlie.mapper")
@SpringBootApplication
public class MyProject1Application {
    public static void main(String[] args) {
        SpringApplication.run(MyProject1Application.class,args);
    }
}
