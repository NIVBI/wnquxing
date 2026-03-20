package com.wnquxing.controller;

import com.wnquxing.entity.vo.ResponseVO;
import com.wnquxing.exception.BusinessException;
import com.wnquxing.service.OpenRouterService;
import com.wnquxing.service.impl.OpenRouterServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/ai")
public class AIController extends ABaseController {

    private static final Logger log = LoggerFactory.getLogger(AIController.class);

    @Resource
    private OpenRouterService openRouterService;

    /**
     * 简单的AI对话接口
     */
    @PostMapping("/chat")
    public ResponseVO chat(@RequestBody Map<String, String> request) {
        try {
            String message = request.get("message");
            if (message == null || message.trim().isEmpty()) {
                return getBusinessErrorResponseVO(new BusinessException("消息不能为空"), null);
            }

            log.info("用户请求AI: {}", message);
            String reply = openRouterService.chat(message);
            log.info("AI回复: {}", reply);

            Map<String, String> result = new HashMap<>();
            result.put("reply", reply);

            return getSuccessResponseVO(result);

        } catch (Exception e) {
            log.error("AI对话失败", e);
            return getServerErrorResponseVO(null);
        }
    }

    /**
     * 带系统提示词的对话
     */
    @PostMapping("/chatWithSystem")
    public ResponseVO chatWithSystem(@RequestBody Map<String, String> request) {
        try {
            String system = request.get("system");
            String message = request.get("message");

            if (message == null || message.trim().isEmpty()) {
                return getBusinessErrorResponseVO(new BusinessException("消息不能为空"), null);
            }

            String reply;
            if (system != null && !system.trim().isEmpty()) {
                reply = openRouterService.chatWithSystem(system, message);
            } else {
                reply = openRouterService.chat(message);
            }

            Map<String, String> result = new HashMap<>();
            result.put("reply", reply);

            return getSuccessResponseVO(result);

        } catch (Exception e) {
            log.error("AI对话失败", e);
            return getServerErrorResponseVO(null);
        }
    }


}