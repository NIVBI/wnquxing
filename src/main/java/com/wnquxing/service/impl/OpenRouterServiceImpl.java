package com.wnquxing.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wnquxing.config.OpenRouterConfig;
import com.wnquxing.entity.dto.AiResult;
import com.wnquxing.entity.dto.openrouter.*;
import com.wnquxing.exception.BusinessException;
import com.wnquxing.service.OpenRouterService;
import com.wnquxing.utils.JsonUtils;
import okhttp3.*;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;



@Service("openRouterService")
public class OpenRouterServiceImpl implements OpenRouterService {
    private static final String DONE = "[DONE]";
    private static final Integer timeout = 60;

    private static final String AI_URL = "https://spark-api-open.xf-yun.com/v1/chat/completions";

    private static final Logger log = LoggerFactory.getLogger(OpenRouterServiceImpl.class);

    @Resource
    private OpenRouterConfig openRouterConfig;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String chat(String userMessage) throws IOException, InterruptedException {

        return getAiResult(userMessage);
    }

    @Override
    public String chat(List<OpenRouterMessage> messages) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            // 创建符合 OpenRouter API 格式的请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", openRouterConfig.getModel());
            requestBody.put("messages", messages);

            // 可选参数
            if (openRouterConfig.getTemperature() != null) {
                requestBody.put("temperature", openRouterConfig.getTemperature());
            }
            if (openRouterConfig.getMaxTokens() != null) {
                requestBody.put("max_tokens", openRouterConfig.getMaxTokens());
            }

            // 转换为JSON
            String jsonRequest = objectMapper.writeValueAsString(requestBody);
            log.info("请求体内容: {}", jsonRequest);

            // 创建HTTP POST请求
            HttpPost httpPost = new HttpPost(openRouterConfig.getApiUrl());
            httpPost.setHeader("Authorization", "Bearer " + openRouterConfig.getApiKey());
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("HTTP-Referer", "https://wnquxing.com");
            httpPost.setHeader("X-Title", "NIVBI");
            httpPost.setEntity(new StringEntity(jsonRequest, StandardCharsets.UTF_8));

            // 执行请求
            long startTime = System.currentTimeMillis();
            CloseableHttpResponse response = httpClient.execute(httpPost);
            long endTime = System.currentTimeMillis();

            // 解析响应
            String jsonResponse = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            int statusCode = response.getStatusLine().getStatusCode();

            log.info("OpenRouter响应状态码: {}, 耗时: {}ms", statusCode, (endTime - startTime));
            log.info("OpenRouter响应内容: {}", jsonResponse);

            if (statusCode == 200) {
                // 解析响应
                OpenRouterResponse openRouterResponse = objectMapper.readValue(jsonResponse, OpenRouterResponse.class);

                // 记录token使用情况
                if (openRouterResponse.getUsage() != null) {
                    OpenRouterUsage usage = openRouterResponse.getUsage();
                    log.info("Token使用 - 提示: {}, 完成: {}, 总计: {}",
                            usage.getPromptTokens(),
                            usage.getCompletionTokens(),
                            usage.getTotalTokens());
                }

                // 获取AI回复内容
                if (openRouterResponse.getChoices() != null && !openRouterResponse.getChoices().isEmpty()) {
                    return openRouterResponse.getChoices().get(0).getMessage().getContent();
                } else {
                    log.error("AI响应中没有choices字段: {}", jsonResponse);
                    return "AI返回数据格式异常";
                }
            } else {
                log.error("调用OpenRouter失败, 状态码: {}, 响应: {}", statusCode, jsonResponse);
                return "服务调用失败，请稍后重试";
            }

        } catch (IOException e) {
            log.error("调用OpenRouter发生IO异常", e);
            return "网络异常，请检查连接";
        } catch (Exception e) {
            log.error("调用OpenRouter发生未知异常", e);
            return "系统异常，请稍后重试";
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("关闭HTTP客户端异常", e);
            }
        }
    }

    @Override
    public String chatWithSystem(String systemPrompt, String userMessage) {
        List<OpenRouterMessage> messages = new ArrayList<>();
        messages.add(new OpenRouterMessage("system", systemPrompt));
        messages.add(new OpenRouterMessage("user", userMessage));

        return chat(messages);
    }

    @Override
    @Async
    public void chatAsync(String userMessage, ChatCallback callback) {
        try {
            String reply = chat(userMessage);
            callback.onSuccess(reply);
        } catch (Exception e) {
            log.error("异步AI对话失败", e);
            callback.onFailure("处理失败: " + e.getMessage());
        }
    }


    /**
     * 流式响应（如果需要）
     * 注意：这个功能需要更复杂的实现，这里提供基础框架
     */
    public void chatStream(String userMessage, StreamCallback callback) {
        // 流式响应的实现较为复杂，需要处理Server-Sent Events
        // 如果项目需要流式响应，我可以单独提供完整实现
        log.info("流式响应功能需要单独实现");
    }

    /**
     * 流式响应回调接口
     */
    public interface StreamCallback {
        void onChunk(String chunk);
        void onComplete();
        void onError(String error);
    }
    private static String getAiResult( String content)
            throws InterruptedException, IOException {

        StringBuilder resultBuilder = new StringBuilder();

        Map<String, Object> params = new HashMap<>();
        params.put("model", "4.0Ultra");
        params.put("stream", true);

        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", content);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(message);
        params.put("messages", messages);

        RequestBody body = RequestBody.create(
                JsonUtils.convertObj2Json(params),
                MediaType.parse( " application/json; charset=utf-8" )
        );

        Request request = new Request.Builder()
                . url(AI_URL)
                . post(body)
                . addHeader("Authorization", " Bearer okXSBtZngGJPcKlkDSyL:SEeVRmujhzvazhWcOCHf")
                . addHeader("Accept", "text/event-stream")
                . build();

        OkHttpClient client = new OkHttpClient.Builder()
                . connectTimeout(timeout, TimeUnit.SECONDS)
                . readTimeout(timeout, TimeUnit.SECONDS)
                . writeTimeout(timeout, TimeUnit.SECONDS)
                . build();

        CountDownLatch eventLatch = new CountDownLatch(1);

        RealEventSource realEventSource = new RealEventSource(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                String contentChunk = null;
                try {
                    contentChunk = getContent(data);
                } catch (BusinessException e) {
                    throw new RuntimeException(e);
                }
                synchronized (resultBuilder) {
                    resultBuilder.append(contentChunk);
//                    if (writer != null) {
//                        try {
//                            writer.write(contentChunk);
//                            writer.flush();
//                        } catch (IOException e) {
//                            // Handling write exceptions
//                        }
//                    }
                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                eventLatch.countDown();
            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response) {
                System.err.println("AI interface call failed: " + t.getMessage());
                        eventLatch.countDown();
            }
        });

        realEventSource.connect(client);
        eventLatch.await();
        return resultBuilder.toString();
    }

    private static String getContent(String data) throws BusinessException {
        AiResult aiResult = JsonUtils.convertJson2Obj(data, AiResult.class);
        return aiResult.getChoices().get(0).getDelta().getContent();
    }
}