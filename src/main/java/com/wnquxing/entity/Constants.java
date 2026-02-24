package com.wnquxing.entity;

public class Constants {
    private static final String EASYMEETING = "easymeeting:";

    public static final Integer REDIS_TIMEOUT_1S = 1;
    public static final Integer REDIS_TIMEOUT_1M = REDIS_TIMEOUT_1S * 60;
    public static final Integer REDIS_TIMEOUT_1DAY = REDIS_TIMEOUT_1M * 24 * 60;

    public static final String REDIS_KEY_VERIFY_CODE = EASYMEETING + "verifyCode:";

    public static final String REDIS_KEY_WS_TOKEN = EASYMEETING + "token:";
    public static final String REDIS_KEY_WS_USERID = EASYMEETING + "userId:";

    public static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z!@#$%^&*_]{8,20}$";


    public static final Integer LENGTH_11 = 11;
    public static final Integer LENGTH_9 = 9;
    public static final Integer LENGTH_7 = 7;

    // 新增：任务提醒相关Redis key
    public static final String REDIS_KEY_TASK_REMINDER = "task_reminder:";
    public static final String REDIS_KEY_TASK_DAILY_REMINDER = "task_daily_reminder:";

    public static final Integer DEFAULT_PUSH_COUNT = 10;
    // 默认每页显示数量
    private static final int DEFAULT_PAGE_SIZE = 10;

    //视频并发量
    private static final int MAX_CONCURRENT_MATCH =10;
    // 视频匹配相关Redis key
    public static final String REDIS_KEY_MATCH_WAITING_QUEUE = EASYMEETING + "match:waiting:"; // 等待队列（List）
    public static final String REDIS_KEY_MATCH_USER_INFO = EASYMEETING + "match:user:"; // 用户匹配信息（Hash）
    public static final String REDIS_KEY_MATCH_ROOM_USERS = EASYMEETING + "match:room:"; // 房间用户集合（Set）
    public static final String REDIS_KEY_MATCH_ROOM_INFO = EASYMEETING + "match:room:info:"; // 房间信息（Hash）

    // 匹配超时时间（秒）
    public static final Integer MATCH_TIMEOUT = 30; // 30秒匹配超时
    public static final Integer MATCH_ROOM_EXPIRE = 3600; // 房间过期时间（1小时）

    // 匹配状态常量
    public static final Integer MATCH_STATUS_WAITING = 0; // 等待中
    public static final Integer MATCH_STATUS_SUCCESS = 1; // 匹配成功
    public static final Integer MATCH_STATUS_PEER_LEFT = 2; // 对方已离开
    // 添加超时消息类型常量
    public static final String MATCH_TIMEOUT_TYPE = "match_timeout";

}
