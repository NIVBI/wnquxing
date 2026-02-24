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


    // ==================== 视频匹配相关方法 ====================

    /**
     * 加入等待队列
     */
    public void addToWaitingQueue(Long userId) {
        String key = Constants.REDIS_KEY_MATCH_WAITING_QUEUE;
        redisUtils.lpush(key, String.valueOf(userId), Constants.MATCH_TIMEOUT * 2);
    }

    /**
     * 加入等待队列并设置超时标记
     */
    public void addToWaitingQueueWithTimeout(Long userId) {
        String key = Constants.REDIS_KEY_MATCH_WAITING_QUEUE;
        redisUtils.lpush(key, String.valueOf(userId), Constants.MATCH_TIMEOUT * 2);

        String timeoutKey = Constants.REDIS_KEY_MATCH_USER_INFO + userId + ":timeout";
        redisUtils.setex(timeoutKey, String.valueOf(System.currentTimeMillis()), Constants.MATCH_TIMEOUT);
    }

    /**
     * 从等待队列中随机获取一个用户
     */
    public Long getRandomFromWaitingQueue() {
        String key = Constants.REDIS_KEY_MATCH_WAITING_QUEUE;
        List<String> queue = redisUtils.getQueueList(key);
        if (queue == null || queue.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(queue.size());
        String userId = queue.get(index);
        return userId != null ? Long.parseLong(userId) : null;
    }

    /**
     * 从等待队列中移除用户
     */
    public void removeFromWaitingQueue(Long userId) {
        String key = Constants.REDIS_KEY_MATCH_WAITING_QUEUE;
        redisUtils.remove(key, userId.toString());
    }

    /**
     * 获取等待队列长度
     */
    public Long getWaitingQueueSize() {
        String key = Constants.REDIS_KEY_MATCH_WAITING_QUEUE;
        List<String> queue = redisUtils.getQueueList(key);
        return queue != null ? (long) queue.size() : 0L;
    }

    /**
     * 检查用户是否超时
     */
    public Boolean isUserTimeout(Long userId) {
        String timeoutKey = Constants.REDIS_KEY_MATCH_USER_INFO + userId + ":timeout";
        String timeoutTime = (String) redisUtils.get(timeoutKey);
        if (timeoutTime == null) {
            return true;
        }
        long startTime = Long.parseLong(timeoutTime);
        long currentTime = System.currentTimeMillis();
        return (currentTime - startTime) > (Constants.MATCH_TIMEOUT * 1000L);
    }

    /**
     * 移除用户超时标记
     */
    public void removeUserTimeout(Long userId) {
        String timeoutKey = Constants.REDIS_KEY_MATCH_USER_INFO + userId + ":timeout";
        redisUtils.delete(timeoutKey);
    }

    /**
     * 获取所有超时的用户
     */
    public List<Long> getTimeoutUsers() {
        String key = Constants.REDIS_KEY_MATCH_WAITING_QUEUE;
        List<String> waitingUsers = redisUtils.getQueueList(key);
        List<Long> timeoutUsers = new ArrayList<>();

        if (waitingUsers != null) {
            for (String userIdStr : waitingUsers) {
                Long userId = Long.parseLong(userIdStr);
                if (isUserTimeout(userId)) {
                    timeoutUsers.add(userId);
                }
            }
        }
        return timeoutUsers;
    }

    /**
     * 批量移除超时用户
     */
    public void removeTimeoutUsers(List<Long> timeoutUsers) {
        if (timeoutUsers == null || timeoutUsers.isEmpty()) {
            return;
        }
        String key = Constants.REDIS_KEY_MATCH_WAITING_QUEUE;
        for (Long userId : timeoutUsers) {
            redisUtils.remove(key, userId.toString());
            deleteUserMatchInfo(userId);
            removeUserTimeout(userId);
        }
    }

    /**
     * 保存用户匹配信息
     */
    public void saveUserMatchInfo(Long userId, Integer status, String roomName, Long matchedUserId) {
        String key = Constants.REDIS_KEY_MATCH_USER_INFO + userId;
        Map<String, Object> info = new HashMap<>();
        info.put("status", String.valueOf(status));
        info.put("roomName", roomName != null ? roomName : "");
        info.put("matchedUserId", matchedUserId != null ? String.valueOf(matchedUserId) : "");
        info.put("startTime", String.valueOf(System.currentTimeMillis()));

        for (Map.Entry<String, Object> entry : info.entrySet()) {
            redisUtils.hset(key, entry.getKey(), entry.getValue());
        }
        redisUtils.expire(key, 7200);
    }

    /**
     * 获取用户匹配信息
     */
    public Map<String, String> getUserMatchInfo(Long userId) {
        String key = Constants.REDIS_KEY_MATCH_USER_INFO + userId;
        Map<Object, Object> map = redisUtils.hgetAll(key);
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            result.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return result;
    }

    /**
     * 更新用户匹配状态
     */
    public void updateUserMatchStatus(Long userId, Integer status) {
        String key = Constants.REDIS_KEY_MATCH_USER_INFO + userId;
        redisUtils.hset(key, "status", String.valueOf(status));
    }

    /**
     * 删除用户匹配信息
     */
    public void deleteUserMatchInfo(Long userId) {
        String key = Constants.REDIS_KEY_MATCH_USER_INFO + userId;
        redisUtils.delete(key);
    }

    /**
     * 创建房间
     */
    public void createRoom(String roomName, Long user1Id, Long user2Id) {
        String roomInfoKey = Constants.REDIS_KEY_MATCH_ROOM_INFO + roomName;
        Map<String, Object> roomInfo = new HashMap<>();
        roomInfo.put("roomName", roomName);
        roomInfo.put("user1Id", String.valueOf(user1Id));
        roomInfo.put("user2Id", String.valueOf(user2Id));
        roomInfo.put("createTime", String.valueOf(System.currentTimeMillis()));
        roomInfo.put("duration", "0");
        roomInfo.put("userCount", "2");

        for (Map.Entry<String, Object> entry : roomInfo.entrySet()) {
            redisUtils.hset(roomInfoKey, entry.getKey(), entry.getValue());
        }
        redisUtils.expire(roomInfoKey, Constants.MATCH_ROOM_EXPIRE);

        String userRoomKey = Constants.REDIS_KEY_MATCH_USER_INFO + user1Id + ":room";
        redisUtils.setex(userRoomKey, roomName, Constants.MATCH_ROOM_EXPIRE);

        userRoomKey = Constants.REDIS_KEY_MATCH_USER_INFO + user2Id + ":room";
        redisUtils.setex(userRoomKey, roomName, Constants.MATCH_ROOM_EXPIRE);
    }

    /**
     * 获取房间用户列表
     */
    public Set<String> getRoomUsers(String roomName) {
        String roomInfoKey = Constants.REDIS_KEY_MATCH_ROOM_INFO + roomName;
        Map<Object, Object> roomInfo = redisUtils.hgetAll(roomInfoKey);
        if (roomInfo == null || roomInfo.isEmpty()) {
            return null;
        }
        Set<String> users = new HashSet<>();
        if (roomInfo.containsKey("user1Id")) {
            users.add(roomInfo.get("user1Id").toString());
        }
        if (roomInfo.containsKey("user2Id")) {
            users.add(roomInfo.get("user2Id").toString());
        }
        return users;
    }

    /**
     * 从房间移除用户
     */
    public void removeUserFromRoom(String roomName, Long userId) {
        String roomInfoKey = Constants.REDIS_KEY_MATCH_ROOM_INFO + roomName;
        Map<Object, Object> roomInfo = redisUtils.hgetAll(roomInfoKey);
        if (roomInfo == null || roomInfo.isEmpty()) {
            return;
        }

        String userIdStr = String.valueOf(userId);
        if (userIdStr.equals(roomInfo.get("user1Id"))) {
            redisUtils.hdelete(roomInfoKey, "user1Id");
        } else if (userIdStr.equals(roomInfo.get("user2Id"))) {
            redisUtils.hdelete(roomInfoKey, "user2Id");
        }

        Long userCount = 0L;
        if (redisUtils.hexists(roomInfoKey, "user1Id")) {
            userCount++;
        }
        if (redisUtils.hexists(roomInfoKey, "user2Id")) {
            userCount++;
        }

        if (userCount == 0) {
            redisUtils.delete(roomInfoKey);
        } else {
            redisUtils.hset(roomInfoKey, "userCount", String.valueOf(userCount));
        }

        String userRoomKey = Constants.REDIS_KEY_MATCH_USER_INFO + userId + ":room";
        redisUtils.delete(userRoomKey);
    }

    /**
     * 获取房间信息
     */
    public Map<String, String> getRoomInfo(String roomName) {
        String key = Constants.REDIS_KEY_MATCH_ROOM_INFO + roomName;
        Map<Object, Object> map = redisUtils.hgetAll(key);
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            result.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return result;
    }

    /**
     * 更新房间监督时长
     */
    public void updateRoomDuration(String roomName, Integer duration) {
        String key = Constants.REDIS_KEY_MATCH_ROOM_INFO + roomName;
        redisUtils.hset(key, "duration", String.valueOf(duration));
    }

    /**
     * 检查用户是否在等待队列中
     */
    public Boolean isUserInWaitingQueue(Long userId) {
        String key = Constants.REDIS_KEY_MATCH_WAITING_QUEUE;
        List<String> queue = redisUtils.getQueueList(key);
        return queue != null && queue.contains(userId.toString());
    }

    /**
     * 获取用户所在房间
     */
    public String getUserRoomName(Long userId) {
        String userRoomKey = Constants.REDIS_KEY_MATCH_USER_INFO + userId + ":room";
        return (String) redisUtils.get(userRoomKey);
    }
}
