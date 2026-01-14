package com.greenbox.entity.query;

import java.util.Date;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:用户打卡记录表
 * 
 * @Date:2026-01-12
 * 
 */
public class CheckInQuery extends BaseQuery{

	/**
	 * @Description: 签到ID（主键）
	 */
  private Long id;

	/**
	 * @Description: 打卡时间
	 */
  private Date checkTime;

	/**
	 * @Description: 关联用户ID
	 */
  private String userId;

	/**
	 * @Description: 打卡情况（0-未打卡，1-已打卡，2-迟到打卡等）
	 */
  private Integer checkStatus;

	/**
	 * @Description: 打卡记录创建时间
	 */
  private Date createTime;

  private String checkTimeStart;

  private String checkTimeEnd;

  private String userIdFuzzy;

  private String createTimeStart;

  private String createTimeEnd;

  public Long getId(){
  	return id;
  }

  public void setId(Long id){
  	this.id = id;
  }

  public Date getCheckTime(){
  	return checkTime;
  }

  public void setCheckTime(Date checkTime){
  	this.checkTime = checkTime;
  }

  public String getUserId(){
  	return userId;
  }

  public void setUserId(String userId){
  	this.userId = userId;
  }

  public Integer getCheckStatus(){
  	return checkStatus;
  }

  public void setCheckStatus(Integer checkStatus){
  	this.checkStatus = checkStatus;
  }

  public Date getCreateTime(){
  	return createTime;
  }

  public void setCreateTime(Date createTime){
  	this.createTime = createTime;
  }

  public String getCheckTimeStart(){
  	return checkTimeStart;
  }

  public void setCheckTimeStart(String checkTimeStart){
  	this.checkTimeStart = checkTimeStart;
  }

  public String getCheckTimeEnd(){
  	return checkTimeEnd;
  }

  public void setCheckTimeEnd(String checkTimeEnd){
  	this.checkTimeEnd = checkTimeEnd;
  }

  public String getUserIdFuzzy(){
  	return userIdFuzzy;
  }

  public void setUserIdFuzzy(String userIdFuzzy){
  	this.userIdFuzzy = userIdFuzzy;
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