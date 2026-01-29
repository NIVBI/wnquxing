package com.wnquxing.redis;

import com.wnquxing.entity.Constants;
import com.wnquxing.entity.dto.TokenUserInfoDTO;
import com.wnquxing.utils.StringUtils;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Component("redisComponent")
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;

    public String saveVerifyCode(String verifyCode) {
        String verifyCodeKey = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_VERIFY_CODE + verifyCodeKey,verifyCode,Constants.REDIS_TIMEOUT_1M * 5);
        return verifyCodeKey;
    }

    public String getVerifyCode(String verifyCodeKey) {
        return (String) redisUtils.get(Constants.REDIS_KEY_VERIFY_CODE + verifyCodeKey);
    }

    public void deleteVerifyCode(String verifyCodeKey) {
        redisUtils.delete(Constants.REDIS_KEY_VERIFY_CODE + verifyCodeKey);
    }

    public void saveTokenUserInfoDto(TokenUserInfoDTO tokenUserInfoDto) {
        redisUtils.setex(Constants.REDIS_KEY_WS_TOKEN+tokenUserInfoDto.getToken(), tokenUserInfoDto, Constants.REDIS_TIMEOUT_1DAY * 2);
        redisUtils.setex(Constants.REDIS_KEY_WS_USERID+tokenUserInfoDto.getUserId(), tokenUserInfoDto.getToken(), Constants.REDIS_TIMEOUT_1DAY * 2);
    }

    public TokenUserInfoDTO getTokenUserInfoDto(String token) {
        return (TokenUserInfoDTO)redisUtils.get(Constants.REDIS_KEY_WS_TOKEN+token);
    }

    public TokenUserInfoDTO getTokenUserInfoDtoByUserId(String userId) {
        String token = (String) redisUtils.get(Constants.REDIS_KEY_WS_USERID+userId);
        return (TokenUserInfoDTO)redisUtils.get(Constants.REDIS_KEY_WS_TOKEN+token);
    }

    public void cleanUserTokenByUserId(String userId) {
        String token = (String) redisUtils.get(Constants.REDIS_KEY_WS_USERID+userId);
        if (token == null) {
            return;
        }
        redisUtils.delete(Constants.REDIS_KEY_WS_USERID+userId);
        redisUtils.delete(Constants.REDIS_KEY_WS_TOKEN+token);
    }



}
