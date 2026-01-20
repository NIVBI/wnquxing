package com.wnquxing.entity.vo;

// TaskRemindVO.java - 新增提醒VO类

import java.util.Date;

public class TaskRemindVO {
    private Long taskId;
    private String taskName;
    private String remindMessage;
    private Date remindTime;
    private Integer remindType;
    private String remindTypeDesc;

    // getter和setter
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getRemindMessage() {
        return remindMessage;
    }

    public void setRemindMessage(String remindMessage) {
        this.remindMessage = remindMessage;
    }

    public Date getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Date remindTime) {
        this.remindTime = remindTime;
    }

    public Integer getRemindType() {
        return remindType;
    }

    public void setRemindType(Integer remindType) {
        this.remindType = remindType;
    }

    public String getRemindTypeDesc() {
        return remindTypeDesc;
    }

    public void setRemindTypeDesc(String remindTypeDesc) {
        this.remindTypeDesc = remindTypeDesc;
    }
}
