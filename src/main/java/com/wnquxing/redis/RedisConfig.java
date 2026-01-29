package com.wnquxing.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig<V> {

    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.host:}")
    private String host;

    @Value("${spring.redis.port:6379}")  // 添加默认值
    private Integer port;


    @Value("${spring.redis.password:}")
    private String password;




    @Bean(name="redissonClient", destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        try{
            Config config = new Config();
            config.useSingleServer()
                    .setAddress("redis://"+host+":"+port)
                    .setTimeout(3000)
                    .setRetryAttempts(3);
            RedissonClient redissonClient = Redisson.create(config);
            return redissonClient;
        }catch (Exception e) {
            log.info("请检查redis配置");
        }
        return null;
    }

    @Bean("redisTemplate")
    public RedisTemplate<String, V> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, V> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 设置 key 的序列化方式
        template.setKeySerializer(RedisSerializer.string());

        // 设置 value 的序列化方式
        template.setValueSerializer(RedisSerializer.json());

        // 设置 hash 的 key 的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());

        // 设置 hash 的 value 的序列化方式
        template.setHashValueSerializer(RedisSerializer.json());

        template.afterPropertiesSet();
        return template;
    }
}
