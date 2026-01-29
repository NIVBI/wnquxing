package com.wnquxing.websocket;

import com.wnquxing.websocket.netty.NettyWebSocketStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;

@Component("initRun")
public class  InitRun implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitRun.class);

    @Resource
    private DataSource dataSource;

    @Resource
    private NettyWebSocketStarter nettyWebSocketStarter;

    @Override
    public void run(ApplicationArguments args) {
        try{
            dataSource.getConnection();
            new Thread(nettyWebSocketStarter).start();
            logger.info("服务启动成功");
        }catch (SQLException e){
            logger.error("数据库配置错误");
        }catch (RedisConnectionFailureException e){
            logger.error("redis连接错误");
        }catch (Exception e){
            logger.error("服务启动失败", e);
        }
    }
}
