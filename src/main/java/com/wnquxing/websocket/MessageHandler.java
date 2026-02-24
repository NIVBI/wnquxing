package com.wnquxing.websocket;


import com.wnquxing.entity.dto.MessageSendDto;
import com.wnquxing.entity.enums.MessageSend2TypeEnum;
import com.wnquxing.exception.BusinessException;
import com.wnquxing.utils.JsonUtils;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static com.wnquxing.entity.Constants.MATCH_TIMEOUT_TYPE;

@Component("messageHandler")
public class MessageHandler {
    // 匹配消息类型
    public static final String MATCH_SUCCESS_TYPE = "match_success";
    public static final String MATCH_PEER_LEFT_TYPE = "match_peer_left";

    private static final String MESSAGE_TOPIC = "message.topic";
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    @Resource
    private RedissonClient redisson;

    @Resource
    private ChannelContextUtil channelContextUtil;

    @PostConstruct
    public void lisMessage(){
        RTopic rTopic = redisson.getTopic(MESSAGE_TOPIC);
        rTopic.addListener(MessageSendDto.class ,(MessageSendDto, sendDto)->{
            log.info("收到广播消息：{}", JsonUtils.convertObj2Json(sendDto));
            channelContextUtil.sendMsg(sendDto);
        });

    }

    public void sendMessage(MessageSendDto messageSendDto){
        RTopic rTopic = redisson.getTopic(MESSAGE_TOPIC);
        rTopic.publish(messageSendDto);
    }
    /**
     * 发送匹配成功消息
     */
    public void sendMatchSuccessMessage(String userId, String roomName, Long matchedUserId) {
        MessageSendDto<Map<String, Object>> messageSendDto = new MessageSendDto<>();
        messageSendDto.setMessageSend2Type(MessageSend2TypeEnum.USER.getType()); // 使用枚举值 0-个人
        messageSendDto.setReceiveUserId(userId);

        Map<String, Object> content = new HashMap<>();
        content.put("type", MATCH_SUCCESS_TYPE);
        content.put("roomName", roomName);
        content.put("matchedUserId", matchedUserId);
        content.put("message", "匹配成功，已进入房间");

        messageSendDto.setMessageContent(content);
        messageSendDto.setSendTime(System.currentTimeMillis());

        sendMessage(messageSendDto);
    }

    /**
     * 发送匹配超时消息
     */
    public void sendTimeoutMessage(String userId) {
        MessageSendDto<Map<String, Object>> messageSendDto = new MessageSendDto<>();
        messageSendDto.setMessageSend2Type(MessageSend2TypeEnum.USER.getType()); // 使用枚举值 0-个人
        messageSendDto.setReceiveUserId(userId);

        Map<String, Object> content = new HashMap<>();
        content.put("type", MATCH_TIMEOUT_TYPE);
        content.put("message", "匹配超时，请重新尝试");

        messageSendDto.setMessageContent(content);
        messageSendDto.setSendTime(System.currentTimeMillis());

        sendMessage(messageSendDto);
    }

    /**
     * 发送对方离开消息
     */
    public void sendPeerLeftMessage(String userId, String roomName) {
        MessageSendDto<Map<String, Object>> messageSendDto = new MessageSendDto<>();
        messageSendDto.setMessageSend2Type(MessageSend2TypeEnum.USER.getType()); // 使用枚举值 0-个人
        messageSendDto.setReceiveUserId(userId);

        Map<String, Object> content = new HashMap<>();
        content.put("type", MATCH_PEER_LEFT_TYPE);
        content.put("roomName", roomName);
        content.put("message", "对方已离开房间");

        messageSendDto.setMessageContent(content);
        messageSendDto.setSendTime(System.currentTimeMillis());

        sendMessage(messageSendDto);
    }
}
