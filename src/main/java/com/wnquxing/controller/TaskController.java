package com.wnquxing.controller;

import com.wnquxing.entity.po.Task;
import com.wnquxing.entity.query.TaskQuery;
import com.wnquxing.entity.vo.ResponseVO;
import com.wnquxing.service.TaskService;

import java.util.*;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

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


	//log声明
	private static final Logger log = LoggerFactory.getLogger(TaskController.class);
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


	/**
	 * 创建任务（带每日提醒）
	 */
	@PostMapping("/createWithReminder")
	public ResponseVO<Long> createTaskWithReminder(
			@RequestBody Task task,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date remindStartTime,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date remindEndTime,
			@RequestParam(defaultValue = "8") Integer remindHour) {

		ResponseVO<Long> response = new ResponseVO<>();

		try {
			Integer result = taskService.createTaskWithDailyReminder(task, remindStartTime, remindEndTime, remindHour);

			if (result > 0) {
				response.setStatus("success");
				response.setCode(200);
				response.setInfo("任务创建成功并设置每日提醒");
				response.setData(task.getId());
			} else {
				response.setStatus("error");
				response.setCode(500);
				response.setInfo("任务创建失败");
			}
		} catch (IllegalArgumentException e) {
			response.setStatus("error");
			response.setCode(400);
			response.setInfo(e.getMessage());
		} catch (Exception e) {
			log.error("创建任务异常", e);
			response.setStatus("error");
			response.setCode(500);
			response.setInfo("系统错误");
		}

		return response;
	}

	/**
	 * 更新任务提醒设置
	 */
	@PostMapping("/updateReminder/{taskId}")
	public ResponseVO<Void> updateReminder(
			@PathVariable Long taskId,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date newStartTime,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date newEndTime,
			@RequestParam(required = false) Integer newRemindHour) {

		ResponseVO<Void> response = new ResponseVO<>();

		Boolean result = taskService.updateTaskDailyReminder(taskId, newStartTime, newEndTime, newRemindHour);

		if (result) {
			response.setStatus("success");
			response.setCode(200);
			response.setInfo("提醒设置更新成功");
		} else {
			response.setStatus("error");
			response.setCode(500);
			response.setInfo("更新失败");
		}

		return response;
	}

	/**
	 * 取消任务提醒
	 */
	@PostMapping("/cancelReminder/{taskId}")
	public ResponseVO<Void> cancelReminder(@PathVariable Long taskId) {
		ResponseVO<Void> response = new ResponseVO<>();

		Boolean result = taskService.cancelTaskDailyReminder(taskId);

		if (result) {
			response.setStatus("success");
			response.setCode(200);
			response.setInfo("任务提醒已取消");
		} else {
			response.setStatus("error");
			response.setCode(500);
			response.setInfo("取消失败");
		}

		return response;
	}

	/**
	 * 获取任务提醒信息
	 */
	@GetMapping("/reminderInfo/{taskId}")
	public ResponseVO<Map<String, Object>> getReminderInfo(@PathVariable Long taskId) {
		ResponseVO<Map<String, Object>> response = new ResponseVO<>();

		Map<String, Object> reminderInfo = taskService.getTaskReminderInfo(taskId);

		if (reminderInfo != null) {
			response.setStatus("success");
			response.setCode(200);
			response.setInfo("获取成功");
			response.setData(reminderInfo);
		} else {
			response.setStatus("error");
			response.setCode(404);
			response.setInfo("未找到提醒设置");
		}

		return response;
	}

	/**
	 * 获取用户的任务提醒列表
	 */
	@GetMapping("/myReminders")
	public ResponseVO<List<Map<String, Object>>> getMyReminders(@RequestParam String userId) {
		ResponseVO<List<Map<String, Object>>> response = new ResponseVO<>();

		List<Map<String, Object>> reminders = taskService.getUserTaskReminders(userId);

		response.setStatus("success");
		response.setCode(200);
		response.setInfo("获取成功");
		response.setData(reminders);

		return response;
	}

	/**
	 * 手动触发提醒（测试用）
	 */
	@PostMapping("/triggerReminder/{taskId}")
	public ResponseVO<Void> triggerReminder(@PathVariable Long taskId) {
		ResponseVO<Void> response = new ResponseVO<>();

		try {
			taskService.triggerTaskReminder(taskId);
			response.setStatus("success");
			response.setCode(200);
			response.setInfo("提醒已触发");
		} catch (Exception e) {
			response.setStatus("error");
			response.setCode(500);
			response.setInfo("触发失败");
		}

		return response;
	}

	/**
	 * 检查任务是否需要提醒
	 */
	@GetMapping("/checkRemind/{taskId}")
	public ResponseVO<Boolean> checkNeedRemind(@PathVariable Long taskId) {
		ResponseVO<Boolean> response = new ResponseVO<>();

		Boolean needRemind = taskService.checkTaskNeedRemind(taskId);

		response.setStatus("success");
		response.setCode(200);
		response.setInfo("检查完成");
		response.setData(needRemind);

		return response;
	}

	// 原有任务相关接口...
	@PostMapping("/create")
	public ResponseVO<Integer> createTask(@RequestBody Task task) {
		ResponseVO<Integer> response = new ResponseVO<>();

		try {
			Integer result = taskService.add(task);
			response.setStatus("success");
			response.setCode(200);
			response.setInfo("创建成功");
			response.setData(result);
		} catch (Exception e) {
			response.setStatus("error");
			response.setCode(500);
			response.setInfo("创建失败");
		}

		return response;
	}

	@GetMapping("/{id}")
	public ResponseVO<Task> getTaskById(@PathVariable Long id) {
		ResponseVO<Task> response = new ResponseVO<>();

		Task task = taskService.getById(id);
		if (task != null) {
			response.setStatus("success");
			response.setCode(200);
			response.setInfo("获取成功");
			response.setData(task);
		} else {
			response.setStatus("error");
			response.setCode(404);
			response.setInfo("任务不存在");
		}

		return response;
	}
}
