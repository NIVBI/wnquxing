package com.wnquxing.websocket;


import com.wnquxing.entity.dto.MessageSendDto;
import com.wnquxing.exception.BusinessException;
import com.wnquxing.utils.JsonUtils;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component("messageHandler")
public class MessageHandler {

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

}
