package com.wnquxing.websocket.netty;

import com.wnquxing.entity.dto.TokenUserInfoDTO;
import com.wnquxing.redis.RedisComponent;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@ChannelHandler.Sharable
@Slf4j
public class HandlerTokenValidation extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Resource
    private RedisComponent redisComponent;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String uri = request.uri();
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
        List<String> tokens = queryStringDecoder.parameters().get("token");
        if (tokens == null || tokens.isEmpty()) {
            log.error("token is empty");
            sendErrorResponse(ctx);
            return;
        }
        TokenUserInfoDTO tokenUserInfoDTO = redisComponent.getTokenUserInfoDto(tokens.get(0));
        if (tokenUserInfoDTO == null) {
            sendErrorResponse(ctx);
            log.error("token error");
            return;
        }
        ctx.fireChannelRead(request.retain());

        // todo 连接成功初始化
    }



    private void sendErrorResponse(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.copiedBuffer("token is invalid", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
