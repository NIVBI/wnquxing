package com.wnquxing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"com.wnquxing.mappers"})
@SpringBootApplication(scanBasePackages = {"com.wnquxing"})
public class RunSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunSpringBootApplication.class, args);
    }
}
