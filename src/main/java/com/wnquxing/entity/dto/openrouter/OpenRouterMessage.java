package com.wnquxing.entity.dto.openrouter;

import lombok.Data;
import java.util.List;

// 消息对象
@Data
public class OpenRouterMessage {
    private String role; // "user", "assistant", "system"
    private String content;

    public OpenRouterMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
