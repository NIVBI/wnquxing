package com.wnquxing.entity.query;

import java.util.List;

public class TaskRemindExtQuery extends TaskQuery {
    private Integer remindType; // 1:开始提醒 2:结束提醒 3:逾期提醒
    private String userId; // 用户ID

    public Integer getRemindType() {
        return remindType;
    }

    public void setRemindType(Integer remindType) {
        this.remindType = remindType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // 添加辅助方法
    public boolean isUserIdNotEmpty() {
        return userId != null && !userId.trim().isEmpty();
    }
}