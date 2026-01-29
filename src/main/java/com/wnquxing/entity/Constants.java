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

}
