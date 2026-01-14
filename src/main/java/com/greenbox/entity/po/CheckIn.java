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
 * @Description:用户打卡记录表
 * 
 * @Date:2026-01-12
 * 
 */
public class CheckIn implements Serializable{

	/**
	 * @Description: 签到ID（主键）
	 */
  private Long id;

	/**
	 * @Description: 打卡时间
	 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

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

  @Override
  public String toString() {
  	return "签到ID（主键）:" + (id==null?"空":id) + "," +
  	       "打卡时间:" + (checkTime==null?"空":DateUtils.format(checkTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "," +
  	       "关联用户ID:" + (userId==null?"空":userId) + "," +
  	       "打卡情况（0-未打卡，1-已打卡，2-迟到打卡等）:" + (checkStatus==null?"空":checkStatus) + "," +
  	       "打卡记录创建时间:" + (createTime==null?"空":DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
  }
}