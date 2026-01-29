package com.wnquxing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Value("${admin.emails:}")
    private String adminEmail;

    @Value("${ws.port:}")
    private Integer wsPort;

    public Boolean isAdminEmail(String email) {
        return adminEmail.equals(email);
    }



    public Integer getWsPort() {
        return wsPort;
    }
}
