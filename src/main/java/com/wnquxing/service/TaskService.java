package com.wnquxing.service;

import com.wnquxing.entity.po.Task;
import com.wnquxing.entity.query.TaskQuery;
import com.wnquxing.entity.vo.PaginationResultVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:用户任务信息表Service
 * 
 * @Date:2026-01-12
 * 
 */
public interface TaskService{

	/**
	 * @Description: 根据条件查询列表
	 */
  List<Task> findListByQuery(TaskQuery query);

	/**
	 * @Description: 根据条件查询数量
	 */
  Integer findCountByQuery(TaskQuery query);

	/**
	 * @Description: 根据条件更新
	 */
  Integer updateByQuery(Task bean, TaskQuery query);

	/**
	 * @Description: 根据条件删除
	 */
  Integer deleteByQuery(TaskQuery query);

	/**
	 * @Description: 分页查询
	 */
  PaginationResultVO<Task> findListByPage(TaskQuery query);

	/**
	 * @Description: 新增
	 */
  Integer add(Task bean);

	/**
	 * @Description: 批量新增
	 */
  Integer addBatch(List<Task> listBean);

	/**
	 * @Description: 批量新增或更新
	 */
  Integer addOrUpdateBatch(List<Task> listBean);

	/**
	 * @Description: 根据Id查询
	 */
  Task getById(Long id);

	/**
	 * @Description: 根据Id更新
	 */
  Integer updateById(Task bean, Long id);

	/**
	 * @Description: 根据Id删除
	 */
  Integer deleteById(Long id);

	/**
	 * 创建任务并设置每日提醒
	 */
	Integer createTaskWithDailyReminder(Task task, Date remindStartTime, Date remindEndTime, Integer remindHour);

	/**
	 * 检查并发送每日任务提醒（定时任务调用）
	 */
	void checkAndSendDailyTaskReminders();

	/**
	 * 更新任务每日提醒设置
	 */
	Boolean updateTaskDailyReminder(Long taskId, Date newStartTime, Date newEndTime, Integer newRemindHour);

	/**
	 * 取消任务每日提醒
	 */
	Boolean cancelTaskDailyReminder(Long taskId);

	/**
	 * 获取任务提醒设置信息
	 */
	Map<String, Object> getTaskReminderInfo(Long taskId);

	/**
	 * 获取用户的所有任务提醒设置
	 */
	List<Map<String, Object>> getUserTaskReminders(String userId);

	/**
	 * 手动触发任务提醒
	 */
	void triggerTaskReminder(Long taskId);

	/**
	 * 检查当前任务是否需要提醒
	 */
	Boolean checkTaskNeedRemind(Long taskId);

}