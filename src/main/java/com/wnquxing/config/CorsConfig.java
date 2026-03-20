package com.wnquxing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有路径生效
                .allowedOrigins("http://localhost:5173") // 允许的前端源，生产环境请替换为实际域名
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                .allowedHeaders("*") // 允许所有请求头，或指定 "X-Custom-Header"
                .allowCredentials(true) // 允许携带凭证（如 Cookie）
                .maxAge(3600); // 预检请求的缓存时间（秒）
    }
}
