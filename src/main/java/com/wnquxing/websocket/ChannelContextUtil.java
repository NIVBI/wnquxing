package com.wnquxing.websocket;

import com.wnquxing.entity.dto.MessageSendDto;
import com.wnquxing.entity.dto.TokenUserInfoDTO;
import com.wnquxing.entity.enums.MessageSend2TypeEnum;
import com.wnquxing.entity.po.User;
import com.wnquxing.mappers.UserMapper;
import com.wnquxing.redis.RedisComponent;
import com.wnquxing.utils.JsonUtils;
import com.wnquxing.utils.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ChannelContextUtil {

    private static final ConcurrentHashMap<String, Channel> USER_CONTEXT_MAP = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, ChannelGroup> MEETING_ROOM_CONTEXT_MAP = new ConcurrentHashMap<>();

    @Resource
    private UserMapper userInfoMapper;

    @Resource
    private RedisComponent redisComponent;

    public void addContext(String userId, Channel channel) {
        try {
            String channelId = channel.id().toString();
            AttributeKey attributeKey = null;
            if (!AttributeKey.exists(channelId)) {
                attributeKey = AttributeKey.newInstance(channelId);
            }else {
                attributeKey = AttributeKey.valueOf(channelId);
            }
            channel.attr(attributeKey).set(userId);

            //将用户纳入管理
            USER_CONTEXT_MAP.put(userId, channel);


            TokenUserInfoDTO tokenUserInfoDTO = redisComponent.getTokenUserInfoDtoByUserId(userId);
            String curMeetingNo = tokenUserInfoDTO.getCurMeetingNo();
            if (curMeetingNo == null) {
                return;
            }
            addMeetingRoom(curMeetingNo, userId);
        } catch (Exception e) {
            log.error("初始化连接失败",e.getMessage());
        }

    }

    private void addMeetingRoom(String curMeetingNo, String userId) {
        Channel userChannel = USER_CONTEXT_MAP.get(userId);
        if (userChannel == null) {
            return;
        }
        ChannelGroup channelGroup = MEETING_ROOM_CONTEXT_MAP.get(curMeetingNo);
        if (channelGroup == null) {
            channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            MEETING_ROOM_CONTEXT_MAP.put(curMeetingNo, channelGroup);
        }

        if (channelGroup.find(userChannel.id()) == null) {
            channelGroup.add(userChannel);
        }
    }

    public void sendMsg(MessageSendDto messageSendDto){
        if (MessageSend2TypeEnum.USER.getType().equals(messageSendDto.getMessageSend2Type())) {
            send2User(messageSendDto);
        } else {
            send2Group(messageSendDto);
        }
    }

    private void send2User(MessageSendDto messageSendDto) {
        String receiveId = messageSendDto.getReceiveUserId();
        Channel channel = USER_CONTEXT_MAP.get(receiveId);
        if (channel == null) {
            return;
        }
        channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.convertObj2Json(messageSendDto)));

        //强制下线
    }

    private void send2Group(MessageSendDto messageSendDto) {
        String meetingNo = messageSendDto.getMeetingNo();
        if (meetingNo == null) {
            return;
        }
        ChannelGroup channelGroup = MEETING_ROOM_CONTEXT_MAP.get(meetingNo);
        if (channelGroup == null) {
            return;
        }
        channelGroup.writeAndFlush(new TextWebSocketFrame(JsonUtils.convertObj2Json(messageSendDto)));
    }

    public void forceOffline(String userId) {
        redisComponent.cleanUserTokenByUserId(userId);
        Channel channel = USER_CONTEXT_MAP.get(userId);
        if (channel == null) {
            return;
        }
        channel.close();
    }

    public void closeContext(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return;
        }
        Channel channel = USER_CONTEXT_MAP.get(userId);
        if (channel == null) {
            return;
        }
        USER_CONTEXT_MAP.remove(userId);
        channel.close();
    }

    public void removeChannel(Channel channel) {
        Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId = attribute.get();
        if(StringUtils.isEmpty(String.valueOf(userId))) {
            return;
        }

        //不再管理该通道
        USER_CONTEXT_MAP.remove(userId);


    }
}