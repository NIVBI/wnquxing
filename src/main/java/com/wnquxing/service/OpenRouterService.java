package com.wnquxing.service;

import com.wnquxing.entity.dto.openrouter.OpenRouterMessage;

import java.io.IOException;
import java.util.List;

/**
 * OpenRouter AI 服务接口
 */
public interface OpenRouterService {

    /**
     * 发送消息到OpenRouter AI
     * @param userMessage 用户输入的消息
     * @return AI的回复内容
     */
    String chat(String userMessage) throws IOException, InterruptedException;

    /**
     * 发送消息到OpenRouter AI（支持多轮对话）
     * @param messages 消息列表
     * @return AI的回复内容
     */
    String chat(List<OpenRouterMessage> messages);

    /**
     * 带系统提示词的对话
     * @param systemPrompt 系统提示词
     * @param userMessage 用户消息
     * @return AI回复
     */
    String chatWithSystem(String systemPrompt, String userMessage);

    /**
     * 异步发送消息（如果需要异步处理）
     * @param userMessage 用户消息
     * @param callback 回调接口
     */
    void chatAsync(String userMessage, ChatCallback callback);

    /**
     * 回调接口
     */
    interface ChatCallback {
        void onSuccess(String reply);
        void onFailure(String error);
    }
}