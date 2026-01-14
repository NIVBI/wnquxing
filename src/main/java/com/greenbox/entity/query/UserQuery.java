package com.greenbox.entity.query;

import java.util.Date;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:用户基础信息表
 * 
 * @Date:2026-01-12
 * 
 */
public class UserQuery extends BaseQuery{

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
  private Date createTime;

	/**
	 * @Description: 用户邮箱号
	 */
  private String email;

  private String userIdFuzzy;

  private String passwordFuzzy;

  private String createTimeStart;

  private String createTimeEnd;

  private String emailFuzzy;

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

  public String getUserIdFuzzy(){
  	return userIdFuzzy;
  }

  public void setUserIdFuzzy(String userIdFuzzy){
  	this.userIdFuzzy = userIdFuzzy;
  }

  public String getPasswordFuzzy(){
  	return passwordFuzzy;
  }

  public void setPasswordFuzzy(String passwordFuzzy){
  	this.passwordFuzzy = passwordFuzzy;
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

  public String getEmailFuzzy(){
  	return emailFuzzy;
  }

  public void setEmailFuzzy(String emailFuzzy){
  	this.emailFuzzy = emailFuzzy;
  }

}