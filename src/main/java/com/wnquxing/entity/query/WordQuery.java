package com.wnquxing.entity.query;

import java.util.Date;
import java.util.List;

/**
 * @Auther:关山越
 * @Description:单词信息表
 * @Date:2026-01-12
 */
public class WordQuery extends BaseQuery{

    /**
     * @Description: 单词表主键ID
     */
    private Long id;

    /**
     * @Description: 单词汉语释义
     */
    private String chinese;

    /**
     * @Description: 英语单词
     */
    private String english;

    /**
     * @Description: 单词音标
     */
    private String phoneticSymbol;

    /**
     * @Description: 例句
     */
    private String exampleSentence;

    /**
     * @Description: 单词等级（0:初中, 1:高中, 2:四级, 3:六级, 4:考研）
     */
    private Integer wordLevel;

    /**
     * @Description: 单词记录创建时间
     */
    private Date createTime;

    private String chineseFuzzy;

    private String englishFuzzy;

    private String phoneticSymbolFuzzy;

    private String exampleSentenceFuzzy;

    private String createTimeStart;

    private String createTimeEnd;

    // 新增字段用于单词推送功能

    /**
     * @Description: 单词等级列表（用于多级别查询）
     */
    private List<Integer> wordLevelList;

    /**
     * @Description: 排除已学习的单词ID列表
     */
    private List<Long> excludeWordIds;

    /**
     * @Description: 是否随机排序
     */
    private Boolean randomOrder;

    /**
     * @Description: 推送数量限制
     */
    private Integer limitCount;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getChinese(){
        return chinese;
    }

    public void setChinese(String chinese){
        this.chinese = chinese;
    }

    public String getEnglish(){
        return english;
    }

    public void setEnglish(String english){
        this.english = english;
    }

    public String getPhoneticSymbol(){
        return phoneticSymbol;
    }

    public void setPhoneticSymbol(String phoneticSymbol){
        this.phoneticSymbol = phoneticSymbol;
    }

    public String getExampleSentence(){
        return exampleSentence;
    }

    public void setExampleSentence(String exampleSentence){
        this.exampleSentence = exampleSentence;
    }

    public Integer getWordLevel(){
        return wordLevel;
    }

    public void setWordLevel(Integer wordLevel){
        this.wordLevel = wordLevel;
    }

    public Date getCreateTime(){
        return createTime;
    }

    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public String getChineseFuzzy(){
        return chineseFuzzy;
    }

    public void setChineseFuzzy(String chineseFuzzy){
        this.chineseFuzzy = chineseFuzzy;
    }

    public String getEnglishFuzzy(){
        return englishFuzzy;
    }

    public void setEnglishFuzzy(String englishFuzzy){
        this.englishFuzzy = englishFuzzy;
    }

    public String getPhoneticSymbolFuzzy(){
        return phoneticSymbolFuzzy;
    }

    public void setPhoneticSymbolFuzzy(String phoneticSymbolFuzzy){
        this.phoneticSymbolFuzzy = phoneticSymbolFuzzy;
    }

    public String getExampleSentenceFuzzy(){
        return exampleSentenceFuzzy;
    }

    public void setExampleSentenceFuzzy(String exampleSentenceFuzzy){
        this.exampleSentenceFuzzy = exampleSentenceFuzzy;
    }

    public String getCreateTimeStart(){
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart){
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeEnd(){
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd){
        this.createTimeEnd = createTimeEnd;
    }

    public List<Integer> getWordLevelList(){
        return wordLevelList;
    }

    public void setWordLevelList(List<Integer> wordLevelList){
        this.wordLevelList = wordLevelList;
    }

    public List<Long> getExcludeWordIds(){
        return excludeWordIds;
    }

    public void setExcludeWordIds(List<Long> excludeWordIds){
        this.excludeWordIds = excludeWordIds;
    }

    public Boolean getRandomOrder(){
        return randomOrder;
    }

    public void setRandomOrder(Boolean randomOrder){
        this.randomOrder = randomOrder;
    }

    public Integer getLimitCount(){
        return limitCount;
    }

    public void setLimitCount(Integer limitCount){
        this.limitCount = limitCount;
    }
}