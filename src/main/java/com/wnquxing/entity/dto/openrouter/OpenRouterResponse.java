package com.wnquxing.entity.dto.openrouter;

import lombok.Data;

import java.util.List;

// 主响应对象
@Data
public class OpenRouterResponse {
    private String id;
    private String model;
    private List<OpenRouterChoice> choices;
    private OpenRouterUsage usage;
    private Long created;
}
