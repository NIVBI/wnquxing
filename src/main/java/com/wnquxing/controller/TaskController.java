package com.wnquxing.controller;

import com.wnquxing.entity.po.Task;
import com.wnquxing.entity.query.TaskQuery;
import com.wnquxing.entity.vo.ResponseVO;
import com.wnquxing.service.TaskService;
import com.wnquxing.entity.query.TaskRemindExtQuery;

import java.util.*;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:用户任务信息表Controller
 * 
 * @Date:2026-01-12
 * 
 */
@RestController
@RequestMapping("/task")
public class TaskController extends ABaseController{

  @Resource
  private TaskService taskService;

  @RequestMapping("loadDataList")
  public ResponseVO loadDataList(TaskQuery query){
  	return getSuccessResponseVO(taskService.findListByPage(query));
  }
	/**
	 * @Description: 新增
	 */
  @RequestMapping("add")
  public ResponseVO add(Task bean){
  	this.taskService.add(bean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 批量新增
	 */
  @RequestMapping("addBatch")
  public ResponseVO addBatch(@RequestBody List<Task> listBean){
  	this.taskService.addBatch(listBean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 批量新增或更新
	 */
  @RequestMapping("addOrUpdateBatch")
  public ResponseVO addOrUpdateBatch(List<Task> listBean){
  	this.taskService.addOrUpdateBatch(listBean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据Id查询
	 */
  @RequestMapping("getById")
  public ResponseVO getById(Long id){
  	return getSuccessResponseVO(this.taskService.getById(id));
  }

	/**
	 * @Description: 根据Id更新
	 */
  @RequestMapping("updateById")
  public ResponseVO updateById(Task bean, Long id){
  	this.taskService.updateById(bean, id);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据Id删除
	 */
  @RequestMapping("deleteById")
  public ResponseVO deleteById(Long id){
  	this.taskService.deleteById(id);
  	return getSuccessResponseVO(null);
  }

	// ============== 新增：任务提醒相关功能开始（修正版）==============

	// ============== 新增：任务提醒相关功能开始（修复isEmpty问题）==============

	/**
	 * 检查任务是否需要提醒
	 * @param query 提醒查询条件
	 * @return ResponseVO<List<TaskRemindVO>>
	 */
	@RequestMapping("/checkTaskRemind")
	public ResponseVO checkTaskRemind(@RequestBody com.wnquxing.entity.query.TaskRemindQuery query) {
		try {
			Date currentTime = new Date();

			// 查询所有任务，然后根据时间推断状态
			TaskQuery taskQuery = new TaskQuery();

			// 修复：使用StringUtils或直接判断
			if (query != null && query.getUserId() != null) {
				String userId = String.valueOf(query.getUserId()); // 任何类型都转为字符串
				if (userId != null && !"".equals(userId) && !"null".equals(userId)) {
						taskQuery.setUserId(userId);
				}
			}

			List<Task> allTasks = taskService.findListByQuery(taskQuery);
			List<com.wnquxing.entity.vo.TaskRemindVO> remindList = new ArrayList<>();

			for (Task task : allTasks) {
				// 获取任务推断状态
				Integer inferredStatus = inferTaskStatus(task, currentTime);

				// 只有未开始和进行中的任务才检查提醒
				if (inferredStatus != null && (inferredStatus == 0 || inferredStatus == 1)) {
					com.wnquxing.entity.vo.TaskRemindVO remind = checkSingleTaskRemind(task, currentTime, query.getRemindType());
					if (remind != null) {
						remindList.add(remind);
					}
				}
			}

			return getSuccessResponseVO(remindList);
		} catch (Exception e) {
			e.printStackTrace();
			return getServerErrorResponseVO(null);
		}
	}

	/**
	 * 根据时间推断任务状态
	 * 0=未开始，1=进行中，2=已完成
	 */
	private Integer inferTaskStatus(Task task, Date currentTime) {
		try {
			// 先检查完成状态
			Integer completionStatus = getTaskCompletionStatus(task);
			if (completionStatus != null && completionStatus == 1) {
				return 2; // 标记为已完成
			}

			// 获取创建时间和持续天数
			Date createTime = getTaskCreateTime(task);
			if (createTime == null) {
				return 0; // 默认未开始
			}

			Long durationInMinutes = getTaskDuration(task);
			Calendar endTimeCal = Calendar.getInstance();
			endTimeCal.setTime(createTime);
			endTimeCal.add(Calendar.MINUTE, durationInMinutes.intValue());
			Date endTime = endTimeCal.getTime();

			// 根据时间推断状态
			if (currentTime.before(createTime)) {
				return 0; // 未开始
			} else if (currentTime.after(createTime) && currentTime.before(endTime)) {
				return 1; // 进行中
			} else {
				return 1; // 即使过了结束时间，只要没标记为完成，仍视为进行中（可能逾期）
			}
		} catch (Exception e) {
			return 0; // 默认未开始
		}
	}

	/**
	 * 检查单个任务是否需要提醒
	 */
	private com.wnquxing.entity.vo.TaskRemindVO checkSingleTaskRemind(Task task, Date currentTime, Integer remindType) {
		try {
			Date createTime = getTaskCreateTime(task);
			if (createTime == null) {
				return null;
			}

			Long durationInMinutes = getTaskDuration(task);
			Calendar endTimeCal = Calendar.getInstance();
			endTimeCal.setTime(createTime);
			endTimeCal.add(Calendar.MINUTE, durationInMinutes.intValue());
			Date endTime = endTimeCal.getTime();

			// 推断任务状态
			Integer taskStatus = inferTaskStatus(task, currentTime);

			// 根据提醒类型检查
			return checkRemindByType(task, createTime, endTime, currentTime, taskStatus, remindType);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据类型检查提醒
	 */
	private com.wnquxing.entity.vo.TaskRemindVO checkRemindByType(Task task, Date createTime, Date endTime,
																  Date currentTime, Integer taskStatus, Integer remindType) {

		// 已完成的任务不提醒
		if (taskStatus != null && taskStatus == 2) {
			return null;
		}

		// 如果没有指定提醒类型，检查所有类型
		if (remindType == null) {
			com.wnquxing.entity.vo.TaskRemindVO remind = checkRemindByType(task, createTime, endTime, currentTime, taskStatus, 1);
			if (remind == null) remind = checkRemindByType(task, createTime, endTime, currentTime, taskStatus, 2);
			if (remind == null) remind = checkRemindByType(task, createTime, endTime, currentTime, taskStatus, 3);
			return remind;
		}

		switch (remindType) {
			case 1: // 开始提醒（任务开始前30分钟）
				if (taskStatus != null && taskStatus == 0) { // 未开始
					Calendar remindStartCal = Calendar.getInstance();
					remindStartCal.setTime(createTime);
					remindStartCal.add(Calendar.MINUTE, -30);

					if (currentTime.after(remindStartCal.getTime()) && currentTime.before(createTime)) {
						return createRemindVO(task, "任务即将开始：" + getTaskName(task),
								createTime, 1, "开始提醒");
					}
				}
				break;

			case 2: // 结束提醒（任务结束前1小时）
				if (taskStatus != null && taskStatus == 1) { // 进行中
					Calendar remindEndCal = Calendar.getInstance();
					remindEndCal.setTime(endTime);
					remindEndCal.add(Calendar.HOUR, -1);

					if (currentTime.after(remindEndCal.getTime()) && currentTime.before(endTime)) {
						return createRemindVO(task, "任务即将结束：" + getTaskName(task),
								endTime, 2, "结束提醒");
					}
				}
				break;

			case 3: // 逾期提醒
				if (taskStatus != null && (taskStatus == 0 || taskStatus == 1) && currentTime.after(endTime)) {
					return createRemindVO(task, "任务已逾期：" + getTaskName(task),
							endTime, 3, "逾期提醒");
				}
				break;
		}

		return null;
	}

	/**
	 * 创建提醒VO对象
	 */
	private com.wnquxing.entity.vo.TaskRemindVO createRemindVO(Task task, String message,
															   Date remindTime, Integer remindType, String remindTypeDesc) {
		com.wnquxing.entity.vo.TaskRemindVO remind = new com.wnquxing.entity.vo.TaskRemindVO();
		remind.setTaskId(task.getId());
		remind.setTaskName(getTaskName(task));
		remind.setRemindMessage(message);
		remind.setRemindTime(remindTime);
		remind.setRemindType(remindType);
		remind.setRemindTypeDesc(remindTypeDesc);
		return remind;
	}

	/**
	 * 获取任务名称
	 */
	private String getTaskName(Task task) {
		try {
			// 使用personalGoal作为任务名称
			java.lang.reflect.Method getPersonalGoal = task.getClass().getMethod("getPersonalGoal");
			Object personalGoal = getPersonalGoal.invoke(task);
			if (personalGoal != null && personalGoal.toString() != null && !personalGoal.toString().trim().isEmpty()) {
				return personalGoal.toString();
			}
			return "任务" + task.getId();
		} catch (Exception e) {
			return "任务" + task.getId();
		}
	}

	/**
	 * 获取创建时间
	 */
	private Date getTaskCreateTime(Task task) {
		try {
			java.lang.reflect.Method getCreateTime = task.getClass().getMethod("getCreateTime");
			Object result = getCreateTime.invoke(task);
			if (result instanceof Date) {
				return (Date) result;
			}
			return new Date();
		} catch (Exception e) {
			return new Date();
		}
	}

	/**
	 * 获取持续时间（分钟）
	 */
	private Long getTaskDuration(Task task) {
		try {
			// 使用continuousDays（持续天数）转换为分钟
			java.lang.reflect.Method getContinuousDays = task.getClass().getMethod("getContinuousDays");
			Object result = getContinuousDays.invoke(task);

			if (result instanceof Integer) {
				int days = (Integer) result;
				if (days <= 0) {
					days = 1; // 默认1天
				}
				return (long) (days * 24 * 60); // 天转分钟
			} else if (result instanceof Long) {
				long days = (Long) result;
				if (days <= 0) {
					days = 1; // 默认1天
				}
				return days * 24 * 60;
			}

			return 1440L; // 默认1天（1440分钟）
		} catch (Exception e) {
			return 1440L; // 默认1天（1440分钟）
		}
	}

	/**
	 * 获取任务完成状态
	 */
	private Integer getTaskCompletionStatus(Task task) {
		try {
			java.lang.reflect.Method getCompletionStatus = task.getClass().getMethod("getCompletionStatus");
			Object result = getCompletionStatus.invoke(task);
			if (result instanceof Integer) {
				return (Integer) result;
			}
			return 0; // 默认未完成
		} catch (Exception e) {
			return 0; // 默认未完成
		}
	}

	/**
	 * 批量检查任务提醒状态
	 */
	@RequestMapping("/batchCheckRemind")
	public ResponseVO batchCheckRemind(@RequestBody List<Long> taskIds) {
		try {
			List<Map<String, Object>> result = new ArrayList<>();
			Date currentTime = new Date();

			for (Long taskId : taskIds) {
				Task task = taskService.getById(taskId);
				if (task != null) {
					Map<String, Object> taskResult = new HashMap<>();
					taskResult.put("taskId", taskId);
					taskResult.put("taskName", getTaskName(task));

					// 推断状态
					Integer inferredStatus = inferTaskStatus(task, currentTime);
					taskResult.put("inferredStatus", inferredStatus);
					taskResult.put("inferredStatusDesc", getStatusDesc(inferredStatus));

					// 检查所有类型的提醒
					com.wnquxing.entity.vo.TaskRemindVO startRemind = checkSingleTaskRemind(task, currentTime, 1);
					com.wnquxing.entity.vo.TaskRemindVO endRemind = checkSingleTaskRemind(task, currentTime, 2);
					com.wnquxing.entity.vo.TaskRemindVO overdueRemind = checkSingleTaskRemind(task, currentTime, 3);

					List<com.wnquxing.entity.vo.TaskRemindVO> reminds = new ArrayList<>();
					if (startRemind != null) reminds.add(startRemind);
					if (endRemind != null) reminds.add(endRemind);
					if (overdueRemind != null) reminds.add(overdueRemind);

					taskResult.put("hasRemind", !reminds.isEmpty());
					taskResult.put("reminds", reminds);
					taskResult.put("completionStatus", getTaskCompletionStatus(task));

					result.add(taskResult);
				}
			}

			return getSuccessResponseVO(result);
		} catch (Exception e) {
			e.printStackTrace();
			return getServerErrorResponseVO(null);
		}
	}

	/**
	 * 获取状态描述
	 */
	private String getStatusDesc(Integer status) {
		if (status == null) return "未知";
		switch (status) {
			case 0: return "未开始";
			case 1: return "进行中";
			case 2: return "已完成";
			default: return "未知";
		}
	}

	/**
	 * 获取待提醒任务列表（简化版）
	 */
	@RequestMapping("/getPendingRemindTasks")
	public ResponseVO getPendingRemindTasks(String userId) {
		try {
			Date currentTime = new Date();
			List<com.wnquxing.entity.vo.TaskRemindVO> allReminds = new ArrayList<>();

			// 查询用户的任务
			TaskQuery query = new TaskQuery();
			if (userId != null && userId.trim().length() > 0) {
				query.setUserId(userId);
			}

			List<Task> tasks = taskService.findListByQuery(query);

			for (Task task : tasks) {
				// 检查所有类型的提醒
				com.wnquxing.entity.vo.TaskRemindVO remind = checkSingleTaskRemind(task, currentTime, null);
				if (remind != null) {
					allReminds.add(remind);
				}
			}

			return getSuccessResponseVO(allReminds);
		} catch (Exception e) {
			e.printStackTrace();
			return getServerErrorResponseVO(null);
		}
	}

	/**
	 * 获取任务统计信息
	 */
	@RequestMapping("/getTaskStatistics")
	public ResponseVO getTaskStatistics(String userId) {
		try {
			TaskQuery query = new TaskQuery();
			if (userId != null && userId.trim().length() > 0) {
				query.setUserId(userId);
			}

			List<Task> tasks = taskService.findListByQuery(query);
			Date currentTime = new Date();

			int totalTasks = tasks.size();
			int unstartedCount = 0;
			int inProgressCount = 0;
			int completedCount = 0;
			int startRemindCount = 0;
			int endRemindCount = 0;
			int overdueRemindCount = 0;

			for (Task task : tasks) {
				Integer status = inferTaskStatus(task, currentTime);
				if (status != null) {
					if (status == 0) unstartedCount++;
					else if (status == 1) inProgressCount++;
					else if (status == 2) completedCount++;
				}

				if (checkSingleTaskRemind(task, currentTime, 1) != null) startRemindCount++;
				if (checkSingleTaskRemind(task, currentTime, 2) != null) endRemindCount++;
				if (checkSingleTaskRemind(task, currentTime, 3) != null) overdueRemindCount++;
			}

			Map<String, Object> statistics = new HashMap<>();
			statistics.put("totalTasks", totalTasks);
			statistics.put("unstartedCount", unstartedCount);
			statistics.put("inProgressCount", inProgressCount);
			statistics.put("completedCount", completedCount);
			statistics.put("startRemindCount", startRemindCount);
			statistics.put("endRemindCount", endRemindCount);
			statistics.put("overdueRemindCount", overdueRemindCount);
			statistics.put("checkTime", currentTime);

			return getSuccessResponseVO(statistics);
		} catch (Exception e) {
			e.printStackTrace();
			return getServerErrorResponseVO(null);
		}
	}

	/**
	 * 简单检查提醒（无需参数）
	 */
	@RequestMapping("/simpleRemindCheck")
	public ResponseVO simpleRemindCheck() {
		try {
			Date currentTime = new Date();
			TaskQuery query = new TaskQuery();
			List<Task> tasks = taskService.findListByQuery(query);

			List<com.wnquxing.entity.vo.TaskRemindVO> reminds = new ArrayList<>();

			for (Task task : tasks) {
				com.wnquxing.entity.vo.TaskRemindVO remind = checkSingleTaskRemind(task, currentTime, null);
				if (remind != null) {
					reminds.add(remind);
				}
			}

			return getSuccessResponseVO(reminds);
		} catch (Exception e) {
			e.printStackTrace();
			return getServerErrorResponseVO(null);
		}
	}

	/**
	 * 获取任务详情及提醒状态
	 */
	@RequestMapping("/getTaskWithRemind")
	public ResponseVO getTaskWithRemind(Long id) {
		try {
			Task task = taskService.getById(id);
			if (task == null) {
				return getSuccessResponseVO(null);
			}

			Date currentTime = new Date();
			Map<String, Object> result = new HashMap<>();

			result.put("task", task);
			result.put("taskName", getTaskName(task));
			result.put("inferredStatus", inferTaskStatus(task, currentTime));
			result.put("inferredStatusDesc", getStatusDesc(inferTaskStatus(task, currentTime)));

			// 检查所有提醒
			List<com.wnquxing.entity.vo.TaskRemindVO> reminds = new ArrayList<>();
			com.wnquxing.entity.vo.TaskRemindVO startRemind = checkSingleTaskRemind(task, currentTime, 1);
			com.wnquxing.entity.vo.TaskRemindVO endRemind = checkSingleTaskRemind(task, currentTime, 2);
			com.wnquxing.entity.vo.TaskRemindVO overdueRemind = checkSingleTaskRemind(task, currentTime, 3);

			if (startRemind != null) reminds.add(startRemind);
			if (endRemind != null) reminds.add(endRemind);
			if (overdueRemind != null) reminds.add(overdueRemind);

			result.put("reminds", reminds);
			result.put("hasRemind", !reminds.isEmpty());

			return getSuccessResponseVO(result);
		} catch (Exception e) {
			e.printStackTrace();
			return getServerErrorResponseVO(null);
		}
	}


	// ============== 新增：任务提醒相关功能结束 ==============
}
