package com.greenbox.entity.query;

import java.util.Date;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:书籍信息表
 * 
 * @Date:2026-01-12
 * 
 */
public class BookQuery extends BaseQuery{

	/**
	 * @Description: 书籍表主键ID
	 */
  private Long id;

	/**
	 * @Description: 书籍名称
	 */
  private String bookName;

	/**
	 * @Description: 书籍类别（如：英语、小说、工具类等）
	 */
  private String category;

	/**
	 * @Description: 书籍作者
	 */
  private String author;

	/**
	 * @Description: 书籍简介
	 */
  private String introduction;

	/**
	 * @Description: 书籍记录创建时间
	 */
  private Date createTime;

  private String bookNameFuzzy;

  private String categoryFuzzy;

  private String authorFuzzy;

  private String introductionFuzzy;

  private String createTimeStart;

  private String createTimeEnd;

  public Long getId(){
  	return id;
  }

  public void setId(Long id){
  	this.id = id;
  }

  public String getBookName(){
  	return bookName;
  }

  public void setBookName(String bookName){
  	this.bookName = bookName;
  }

  public String getCategory(){
  	return category;
  }

  public void setCategory(String category){
  	this.category = category;
  }

  public String getAuthor(){
  	return author;
  }

  public void setAuthor(String author){
  	this.author = author;
  }

  public String getIntroduction(){
  	return introduction;
  }

  public void setIntroduction(String introduction){
  	this.introduction = introduction;
  }

  public Date getCreateTime(){
  	return createTime;
  }

  public void setCreateTime(Date createTime){
  	this.createTime = createTime;
  }

  public String getBookNameFuzzy(){
  	return bookNameFuzzy;
  }

  public void setBookNameFuzzy(String bookNameFuzzy){
  	this.bookNameFuzzy = bookNameFuzzy;
  }

  public String getCategoryFuzzy(){
  	return categoryFuzzy;
  }

  public void setCategoryFuzzy(String categoryFuzzy){
  	this.categoryFuzzy = categoryFuzzy;
  }

  public String getAuthorFuzzy(){
  	return authorFuzzy;
  }

  public void setAuthorFuzzy(String authorFuzzy){
  	this.authorFuzzy = authorFuzzy;
  }

  public String getIntroductionFuzzy(){
  	return introductionFuzzy;
  }

  public void setIntroductionFuzzy(String introductionFuzzy){
  	this.introductionFuzzy = introductionFuzzy;
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