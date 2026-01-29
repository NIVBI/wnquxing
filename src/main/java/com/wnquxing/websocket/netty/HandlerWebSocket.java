package com.wnquxing.websocket.netty;

import com.wnquxing.websocket.ChannelContextUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@ChannelHandler.Sharable
@Slf4j
public class HandlerWebSocket extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Resource
    private ChannelContextUtil channelContextUtil;

    /**
     * 通道就绪后调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive");
        channelContextUtil.removeChannel(ctx.channel());

    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        Channel channel = channelHandlerContext.channel();
        Attribute<Integer> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        Integer userId = Integer.valueOf(attribute.get());
//        redisComponent.saveUserHeartBeat(userId);
        log.info("channelRead0:userId:{}  消息:{}", userId, textWebSocketFrame.text());

    }


    //初始化
    /*@Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String url = handshakeComplete.requestUri();
            log.info("url:{}", url);
            String token = getToken(url);
            log.info("token:{}", token);

            //检验token
            if (!redisComponent.isLegalToken(token)) {
                ctx.channel().close();
                return;
            }
            TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfoDto(token);
            channelContextUtil.addChannel(tokenUserInfoDto.getUserId(), ctx.channel());
        }
    }

    private String getToken(String url){
        if(StringUtils.isEmpty(url) || url.indexOf("?") == -1){
            return null;
        }
        String[] urls = url.split("\\?");
        if(urls.length != 2){
            return null;
        }
        String[] token = urls[1].split("=");
        if(token.length != 2){
            return null;
        }
        return token[1];
    }*/
}
