package com.wnquxing.entity.enums;

public enum TaskTypeEnum {
    READING(0, "读书任务"),
    WORD(1, "单词任务"),
    VIDEO(2, "视频任务（时长）");

    private Integer type;
    private String desc;

    TaskTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static TaskTypeEnum getByType(Integer type) {
        if (type == null) {
            return null;
        }
        for (TaskTypeEnum item : TaskTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByType(Integer type) {
        TaskTypeEnum item = getByType(type);
        return item != null ? item.getDesc() : "";
    }

    public static boolean isValidType(Integer type) {
        return getByType(type) != null;
    }

}