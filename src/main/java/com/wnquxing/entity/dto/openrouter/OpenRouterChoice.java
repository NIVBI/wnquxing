package com.wnquxing.entity.dto.openrouter;

import lombok.Data;

// 响应中的 choice 对象
@Data
public class OpenRouterChoice {
    private Integer index;
    private OpenRouterMessage message;
    private String finishReason;
}
