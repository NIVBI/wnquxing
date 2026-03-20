package com.wnquxing.entity.dto.openrouter;

import lombok.Data;

// 响应中的 usage 对象
@Data
public class OpenRouterUsage {
    private Integer promptTokens;
    private Integer completionTokens;
    private Integer totalTokens;
    private Integer reasoningTokens;
}
