package com.wnquxing.entity.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 单词分类级别枚举
 */
public enum WordLevelEnum {

    /**
     * 初中级别
     */
    JUNIOR_HIGH(0, "初中", "初中词汇"),

    /**
     * 高中级别
     */
    SENIOR_HIGH(1, "高中", "高中词汇"),

    /**
     * 四级级别
     */
    CET4(2, "四级", "大学英语四级词汇"),

    /**
     * 六级级别
     */
    CET6(3, "六级", "大学英语六级词汇"),

    /**
     * 考研级别
     */
    GRADUATE(4, "考研", "研究生入学考试词汇");

    private final Integer code;
    private final String name;
    private final String description;

    WordLevelEnum(Integer code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据code获取枚举
     */
    public static WordLevelEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (WordLevelEnum level : values()) {
            if (level.getCode().equals(code)) {
                return level;
            }
        }
        return null;
    }

    /**
     * 根据name获取枚举
     */
    public static WordLevelEnum getByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        for (WordLevelEnum level : values()) {
            if (level.getName().equals(name)) {
                return level;
            }
        }
        return null;
    }

    /**
     * 验证code是否有效
     */
    public static boolean isValidCode(Integer code) {
        return getByCode(code) != null;
    }

    /**
     * 获取所有级别的code列表
     */
    public static List<Integer> getAllCodes() {
        return Arrays.stream(values())
                .map(WordLevelEnum::getCode)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有级别的name列表
     */
    public static List<String> getAllNames() {
        return Arrays.stream(values())
                .map(WordLevelEnum::getName)
                .collect(Collectors.toList());
    }

    /**
     * 获取级别映射（code->name）
     */
    public static Map<Integer, String> getLevelMap() {
        Map<Integer, String> map = new HashMap<>();
        for (WordLevelEnum level : values()) {
            map.put(level.getCode(), level.getName());
        }
        return map;
    }

    /**
     * 获取级别详细映射（code->description）
     */
    public static Map<Integer, String> getLevelDetailMap() {
        Map<Integer, String> map = new HashMap<>();
        for (WordLevelEnum level : values()) {
            map.put(level.getCode(), level.getDescription());
        }
        return map;
    }

    /**
     * 获取下一个级别的枚举（用于升级功能）
     */
    public WordLevelEnum getNextLevel() {
        int nextCode = this.code + 1;
        return getByCode(nextCode);
    }

    /**
     * 获取上一个级别的枚举
     */
    public WordLevelEnum getPreviousLevel() {
        int prevCode = this.code - 1;
        return getByCode(prevCode);
    }

    /**
     * 判断是否为最高级别
     */
    public boolean isHighestLevel() {
        return this == GRADUATE;
    }

    /**
     * 判断是否为最低级别
     */
    public boolean isLowestLevel() {
        return this == JUNIOR_HIGH;
    }

    /**
     * 获取推荐的学习顺序
     */
    public static List<WordLevelEnum> getLearningOrder() {
        return Arrays.asList(JUNIOR_HIGH, SENIOR_HIGH, CET4, CET6, GRADUATE);
    }

    /**
     * 根据用户水平推荐起始级别
     * @param educationLevel 用户教育水平：初中=0, 高中=1, 大学=2, 研究生=3
     */
    public static WordLevelEnum recommendStartLevel(Integer educationLevel) {
        if (educationLevel == null) {
            return JUNIOR_HIGH;
        }

        switch (educationLevel) {
            case 0: // 初中
                return JUNIOR_HIGH;
            case 1: // 高中
                return SENIOR_HIGH;
            case 2: // 大学
                return CET4;
            case 3: // 研究生
                return GRADUATE;
            default:
                return JUNIOR_HIGH;
        }
    }

    @Override
    public String toString() {
        return "WordLevelEnum{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

//    // 1. 根据code获取枚举
//    WordLevelEnum level = WordLevelEnum.getByCode(2); // 返回 CET4
//
//    // 2. 获取所有级别
//    List<Integer> allLevels = WordLevelEnum.getAllCodes();
//
//    // 3. 推荐起始级别
//    WordLevelEnum startLevel = WordLevelEnum.recommendStartLevel(1); // 高中用户从高中级别开始
//
//    // 4. 判断是否为最高级别
//    boolean isHighest = WordLevelEnum.GRADUATE.isHighestLevel(); // true
//
//    // 5. 获取下一个级别（用于升级）
//    WordLevelEnum nextLevel = WordLevelEnum.CET4.getNextLevel(); // 返回 CET6
}