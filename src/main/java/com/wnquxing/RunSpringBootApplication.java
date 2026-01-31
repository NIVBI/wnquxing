package com.wnquxing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan(basePackages = {"com.wnquxing.mappers"})
@SpringBootApplication(scanBasePackages = {"com.wnquxing"})
@EnableScheduling
public class RunSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunSpringBootApplication.class, args);
    }
}
