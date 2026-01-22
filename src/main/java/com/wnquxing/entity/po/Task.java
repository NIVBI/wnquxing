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
 * @Description:用户任务信息表
 * 
 * @Date:2026-01-12
 * 
 */
public class Task implements Serializable{

	/**
	 * @Description: 任务表主键ID
	 */
  private Long id;

	/**
	 * @Description: 关联用户ID
	 */
  private String userId;

	/**
	 * @Description: 任务类型（如：背单词、读书、打卡等）
	 */
  private String taskType;

	/**
	 * @Description: 完成情况（0 "未开始"，1 "进行中" ，2, "已完成"）
	 */
  private Integer completionStatus;

	/**
	 * @Description: 个人目标描述
	 */
  private String personalGoal;

	/**
	 * @Description: 任务持续天数
	 */
  private Integer continuousDays;

	/**
	 * @Description: 任务记录创建时间
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

  public String getUserId(){
  	return userId;
  }

  public void setUserId(String userId){
  	this.userId = userId;
  }

  public String getTaskType(){
  	return taskType;
  }

  public void setTaskType(String taskType){
  	this.taskType = taskType;
  }

  public Integer getCompletionStatus(){
  	return completionStatus;
  }

  public void setCompletionStatus(Integer completionStatus){
  	this.completionStatus = completionStatus;
  }

  public String getPersonalGoal(){
  	return personalGoal;
  }

  public void setPersonalGoal(String personalGoal){
  	this.personalGoal = personalGoal;
  }

  public Integer getContinuousDays(){
  	return continuousDays;
  }

  public void setContinuousDays(Integer continuousDays){
  	this.continuousDays = continuousDays;
  }

  public Date getCreateTime(){
  	return createTime;
  }

  public void setCreateTime(Date createTime){
  	this.createTime = createTime;
  }

  @Override
  public String toString() {
  	return "任务表主键ID:" + (id==null?"空":id) + "," +
  	       "关联用户ID:" + (userId==null?"空":userId) + "," +
  	       "任务类型（如：背单词、读书、打卡等）:" + (taskType==null?"空":taskType) + "," +
  	       "完成情况（0-未完成，1-已完成）:" + (completionStatus==null?"空":completionStatus) + "," +
  	       "个人目标描述:" + (personalGoal==null?"空":personalGoal) + "," +
  	       "任务持续天数:" + (continuousDays==null?"空":continuousDays) + "," +
  	       "任务记录创建时间:" + (createTime==null?"空":DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
  }
}