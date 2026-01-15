package com.wnquxing.entity.query;

import java.util.Date;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:监督匹配房间信息表
 * 
 * @Date:2026-01-12
 * 
 */
public class SupervisionMatchQuery extends BaseQuery{

	/**
	 * @Description: 监督匹配表主键ID
	 */
  private Long id;

	/**
	 * @Description: 监督时长（单位：分钟）
	 */
  private Integer supervisionDuration;

	/**
	 * @Description: 监督房间名称
	 */
  private String roomName;

	/**
	 * @Description: 监督匹配记录创建时间
	 */
  private Date createTime;

  private String roomNameFuzzy;

  private String createTimeStart;

  private String createTimeEnd;

  public Long getId(){
  	return id;
  }

  public void setId(Long id){
  	this.id = id;
  }

  public Integer getSupervisionDuration(){
  	return supervisionDuration;
  }

  public void setSupervisionDuration(Integer supervisionDuration){
  	this.supervisionDuration = supervisionDuration;
  }

  public String getRoomName(){
  	return roomName;
  }

  public void setRoomName(String roomName){
  	this.roomName = roomName;
  }

  public Date getCreateTime(){
  	return createTime;
  }

  public void setCreateTime(Date createTime){
  	this.createTime = createTime;
  }

  public String getRoomNameFuzzy(){
  	return roomNameFuzzy;
  }

  public void setRoomNameFuzzy(String roomNameFuzzy){
  	this.roomNameFuzzy = roomNameFuzzy;
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