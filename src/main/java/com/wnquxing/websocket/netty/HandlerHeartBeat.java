package com.wnquxing.websocket.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerHeartBeat extends ChannelDuplexHandler {
    private static final Logger log = LoggerFactory.getLogger(HandlerHeartBeat.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if(event.state() == IdleState.READER_IDLE){
                Channel channel = ctx.channel();
                Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
                log.info("用户{}心跳超时", attribute.get());
                ctx.close();
            }else if(event.state() == IdleState.WRITER_IDLE){
                ctx.writeAndFlush("heartbeat");
            }
        }
    }
}
