package com.charlie;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
@Slf4j
@MapperScan("com.charlie.mapper")
@SpringBootApplication
public class MyProject1Application {
    public static void main(String[] args) {
        SpringApplication.run(MyProject1Application.class,args);
    }
}
