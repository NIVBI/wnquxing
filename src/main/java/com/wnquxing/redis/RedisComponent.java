package com.wnquxing.redis;

import com.wnquxing.entity.Constants;
import com.wnquxing.entity.dto.TokenUserInfoDTO;
import com.wnquxing.utils.StringUtils;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.*;

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
    // 新增任务提醒相关方法

    /**
     * 保存任务提醒信息
     */
    public void saveTaskReminder(Long taskId, String userId, Date reminderTime, String taskInfo) {
        String key = Constants.REDIS_KEY_TASK_REMINDER + taskId;
        Map<String, Object> reminderInfo = new HashMap<>();
        reminderInfo.put("taskId", taskId);
        reminderInfo.put("userId", userId);
        reminderInfo.put("reminderTime", reminderTime.getTime());
        reminderInfo.put("taskInfo", taskInfo);
        reminderInfo.put("lastRemindTime", 0L);
        reminderInfo.put("remindCount", 0);

        for (Map.Entry<String, Object> entry : reminderInfo.entrySet()) {
            redisUtils.hset(key, entry.getKey(), entry.getValue());
        }

        // 设置过期时间（任务结束时间后7天过期）
        // 这里先设置30天，具体过期时间在设置提醒时会计算
        redisUtils.expire(key, 30 * 24 * 3600);
    }

    /**
     * 获取任务提醒信息
     */
    public Map<String, Object> getTaskReminder(Long taskId) {
        String key = Constants.REDIS_KEY_TASK_REMINDER + taskId;
        Map<Object, Object> rawMap = redisUtils.hgetAll(key);

        if (rawMap == null || rawMap.isEmpty()) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : rawMap.entrySet()) {
            result.put(entry.getKey().toString(), entry.getValue());
        }

        return result;
    }

    /**
     * 删除任务提醒
     */
    public void deleteTaskReminder(Long taskId) {
        String key = Constants.REDIS_KEY_TASK_REMINDER + taskId;
        redisUtils.delete(key);
    }

    /**
     * 更新任务最后提醒时间
     */
    public void updateTaskLastRemindTime(Long taskId, Long lastRemindTime) {
        String key = Constants.REDIS_KEY_TASK_REMINDER + taskId;
        redisUtils.hset(key, "lastRemindTime", lastRemindTime);

        // 增加提醒次数
        Object remindCountObj = redisUtils.hget(key, "remindCount");
        Integer remindCount = remindCountObj != null ? Integer.parseInt(remindCountObj.toString()) : 0;
        redisUtils.hset(key, "remindCount", remindCount + 1);
    }

    /**
     * 获取所有需要提醒的任务ID
     */
    public Set<Long> getAllReminderTaskIds() {
        Set<String> keys = redisUtils.keys(Constants.REDIS_KEY_TASK_REMINDER + "*");
        Set<Long> taskIds = new HashSet<>();

        if (keys != null) {
            for (String key : keys) {
                // 提取taskId
                String taskIdStr = key.replace(Constants.REDIS_KEY_TASK_REMINDER, "");
                try {
                    taskIds.add(Long.parseLong(taskIdStr));
                } catch (NumberFormatException e) {
                    // 忽略格式错误的key
                }
            }
        }

        return taskIds;
    }



}
