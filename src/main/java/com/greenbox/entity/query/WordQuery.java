package com.greenbox.entity.query;

import java.util.Date;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:单词信息表
 * 
 * @Date:2026-01-12
 * 
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
	 * @Description: 单词记录创建时间
	 */
  private Date createTime;

  private String chineseFuzzy;

  private String englishFuzzy;

  private String phoneticSymbolFuzzy;

  private String exampleSentenceFuzzy;

  private String createTimeStart;

  private String createTimeEnd;

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

}