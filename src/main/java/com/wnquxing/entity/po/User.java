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
 * @Description:用户基础信息表
 * 
 * @Date:2026-01-12
 * 
 */
public class User implements Serializable{

	/**
	 * @Description: 用户表主键ID
	 */
  private Long id;

	/**
	 * @Description: 用户唯一标识ID
	 */
  private String userId;

	/**
	 * @Description: 用户密码（建议加密存储）
	 */
  private String password;

	/**
	 * @Description: 用户创建时间（表记录创建时间）
	 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

	/**
	 * @Description: 用户邮箱号
	 */
  private String email;

  public Long getId(){
  	return id;
  }

  public void setId(Long id){
  	this.id = id;
  }

  public String getUserId(){
  	return userId;
  }

  public void setUserId(String userId){
  	this.userId = userId;
  }

  public String getPassword(){
  	return password;
  }

  public void setPassword(String password){
  	this.password = password;
  }

  public Date getCreateTime(){
  	return createTime;
  }

  public void setCreateTime(Date createTime){
  	this.createTime = createTime;
  }

  public String getEmail(){
  	return email;
  }

  public void setEmail(String email){
  	this.email = email;
  }

  @Override
  public String toString() {
  	return "用户表主键ID:" + (id==null?"空":id) + "," +
  	       "用户唯一标识ID:" + (userId==null?"空":userId) + "," +
  	       "用户密码（建议加密存储）:" + (password==null?"空":password) + "," +
  	       "用户创建时间（表记录创建时间）:" + (createTime==null?"空":DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "," +
  	       "用户邮箱号:" + (email==null?"空":email);
  }
}