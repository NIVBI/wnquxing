package com.greenbox.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.greenbox.utils.DateUtils;
import com.greenbox.entity.enums.DateTimePatternEnum;
import java.util.Date;
import java.io.Serializable;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:书籍信息表
 * 
 * @Date:2026-01-12
 * 
 */
public class Book implements Serializable{

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
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

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

  @Override
  public String toString() {
  	return "书籍表主键ID:" + (id==null?"空":id) + "," +
  	       "书籍名称:" + (bookName==null?"空":bookName) + "," +
  	       "书籍类别（如：英语、小说、工具类等）:" + (category==null?"空":category) + "," +
  	       "书籍作者:" + (author==null?"空":author) + "," +
  	       "书籍简介:" + (introduction==null?"空":introduction) + "," +
  	       "书籍记录创建时间:" + (createTime==null?"空":DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
  }
}