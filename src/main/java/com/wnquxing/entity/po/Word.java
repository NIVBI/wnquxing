package com.wnquxing.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.wnquxing.utils.DateUtils;
import com.wnquxing.entity.enums.DateTimePatternEnum;
import java.util.Date;
import java.io.Serializable;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:单词信息表
 * 
 * @Date:2026-01-12
 * 
 */
public class Word implements Serializable{

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
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

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

  public Integer getWordLevel(){return wordLevel;}

	public void setWordLevel(Integer wordLevel){this.wordLevel = wordLevel;}


	@Override
  public String toString() {
  	return "单词表主键ID:" + (id==null?"空":id) + "," +
  	       "单词汉语释义:" + (chinese==null?"空":chinese) + "," +
  	       "英语单词:" + (english==null?"空":english) + "," +
			"单词等级:" + (wordLevel==null?"空":wordLevel) + "," +
  	       "单词音标:" + (phoneticSymbol==null?"空":phoneticSymbol) + "," +
  	       "例句:" + (exampleSentence==null?"空":exampleSentence) + "," +
  	       "单词记录创建时间:" + (createTime==null?"空":DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
  }
}