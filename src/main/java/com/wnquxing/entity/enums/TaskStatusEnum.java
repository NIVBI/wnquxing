package com.wnquxing.entity.enums;

public enum TaskStatusEnum {
    UNSTARTED(0, "未开始"),
    IN_PROGRESS(1, "进行中"),
    COMPLETED(2, "已完成");

    private Integer status;
    private String desc;


    TaskStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public static TaskStatusEnum getByStatus(Integer status) {
        for (TaskStatusEnum item : TaskStatusEnum.values()) {
            if (item.getStatus().equals(status)) {
                return item;
            }
        }
        return null;
    }
}