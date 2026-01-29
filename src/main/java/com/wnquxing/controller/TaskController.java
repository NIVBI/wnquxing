package com.wnquxing.controller;

import com.wnquxing.entity.po.Task;
import com.wnquxing.entity.query.TaskQuery;
import com.wnquxing.entity.vo.ResponseVO;
import com.wnquxing.service.TaskService;

import java.util.*;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


	/**
	 * @Description: 创建任务（创建时状态为进行中，并发送提醒）
	 */
	@RequestMapping("createTask")
	public ResponseVO createTask(@RequestParam String userId, @RequestParam String taskType, @RequestParam String personalGoal, @RequestParam Integer continuousDays){
		Task bean = new Task();
		bean.setUserId(userId);
		bean.setTaskType(taskType);
		bean.setPersonalGoal(personalGoal);
		bean.setCompletionStatus(1); // 创建时状态设为"进行中"
		bean.setContinuousDays(continuousDays != null ? continuousDays : 0);
		bean.setCreateTime(new Date());

		// 1. 先添加任务
		this.taskService.add(bean);

//		// 2. 如果任务创建成功，立即发送任务提醒
//		if (bean.getId() != null) {
//			this.taskService.sendTaskRemind(bean.getId());
//		}

		return getSuccessResponseVO(null);
	}


}
