package com.wnquxing.entity.dto.openrouter;

import lombok.Data;

import java.util.List;

// 请求对象
@Data
public class OpenRouterRequest {
    private String model;
    private List<OpenRouterMessage> messages;
    private Double temperature;
    private Integer maxTokens;
    private Boolean stream;
    private Double topP;
    private Integer n;
    private Boolean stop;
    private Double presencePenalty;
    private Double frequencyPenalty;
    private List<String> stopSequences;

    public OpenRouterRequest(String model, List<OpenRouterMessage> messages) {
        this.model = model;
        this.messages = messages;
        this.temperature = 0.7;
        this.maxTokens = 2000;
        this.stream = false;
        this.topP = 0.9;
        this.n = 1;
    }
}