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
 * @Description:监督匹配房间信息表
 * 
 * @Date:2026-01-12
 * 
 */
public class SupervisionMatch implements Serializable{

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
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

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

  @Override
  public String toString() {
  	return "监督匹配表主键ID:" + (id==null?"空":id) + "," +
  	       "监督时长（单位：分钟）:" + (supervisionDuration==null?"空":supervisionDuration) + "," +
  	       "监督房间名称:" + (roomName==null?"空":roomName) + "," +
  	       "监督匹配记录创建时间:" + (createTime==null?"空":DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
  }
}