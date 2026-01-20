package com.wnquxing.entity.query;

public class TaskRemindQuery {
    private Integer remindType; // 1:开始提醒 2:结束提醒 3:逾期提醒
    private Long userId; // 用户ID

    public Integer getRemindType() {
        return remindType;
    }

    public void setRemindType(Integer remindType) {
        this.remindType = remindType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}