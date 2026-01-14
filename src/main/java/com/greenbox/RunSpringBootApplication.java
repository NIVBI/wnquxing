package com.greenbox;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"com.greenbox.mappers"})
@SpringBootApplication(scanBasePackages = {"com.greenbox"})
public class RunSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunSpringBootApplication.class, args);
    }
}
