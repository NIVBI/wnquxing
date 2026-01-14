package com.greenbox.entity.query;

import java.util.Date;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:用户任务信息表
 * 
 * @Date:2026-01-12
 * 
 */
public class TaskQuery extends BaseQuery{

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
	 * @Description: 完成情况（0-未完成，1-已完成）
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
  private Date createTime;

  private String userIdFuzzy;

  private String taskTypeFuzzy;

  private String personalGoalFuzzy;

  private String createTimeStart;

  private String createTimeEnd;

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

  public String getUserIdFuzzy(){
  	return userIdFuzzy;
  }

  public void setUserIdFuzzy(String userIdFuzzy){
  	this.userIdFuzzy = userIdFuzzy;
  }

  public String getTaskTypeFuzzy(){
  	return taskTypeFuzzy;
  }

  public void setTaskTypeFuzzy(String taskTypeFuzzy){
  	this.taskTypeFuzzy = taskTypeFuzzy;
  }

  public String getPersonalGoalFuzzy(){
  	return personalGoalFuzzy;
  }

  public void setPersonalGoalFuzzy(String personalGoalFuzzy){
  	this.personalGoalFuzzy = personalGoalFuzzy;
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