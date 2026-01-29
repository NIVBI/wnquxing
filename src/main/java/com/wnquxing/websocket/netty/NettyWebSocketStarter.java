package com.wnquxing.websocket.netty;

import com.wnquxing.config.AppConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.Utf8FrameValidator;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class NettyWebSocketStarter implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(NettyWebSocketStarter.class);
    // 用于处理连接
    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    //用于处理消息
    private static EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Resource
    private HandlerTokenValidation handlerTokenValidation;

    @Resource
    private HandlerWebSocket handlerWebSocket;

    @Resource
    private AppConfig appConfig;

    @PreDestroy
    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }


    @Override
    public void run() {
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG)).childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            // 设置几个重要的处理器
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(64 * 1024));
                            pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast(new HandlerHeartBeat());
                            pipeline.addLast(new Utf8FrameValidator());
                            // token 校验，拦截channelRead事件
                            pipeline.addLast(handlerTokenValidation);
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true, 6553, true, true, 10000L));
                            //
                            pipeline.addLast(handlerWebSocket);
                        }
                    });

            Integer wsPort = appConfig.getWsPort();
            String wsPortConfig = System.getProperty("ws.port");
            if (wsPortConfig != null) {
                wsPort = Integer.parseInt(wsPortConfig);
            }
            Channel channel = bootstrap.bind(wsPort).sync().channel();
            log.info("ws启动成功,端口:{}", wsPort);
            channel.closeFuture().sync();
        }catch (Exception e){
            log.error("启动失败", e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
