package com.wnquxing.service.impl;

import com.wnquxing.mappers.TaskMapper;
import com.wnquxing.entity.enums.PageSize;
import com.wnquxing.entity.po.Task;
import com.wnquxing.entity.query.SimplePage;
import com.wnquxing.entity.query.TaskQuery;
import com.wnquxing.entity.vo.PaginationResultVO;
import com.wnquxing.service.TaskService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:用户任务信息表ServiceImpl
 * 
 * @Date:2026-01-12
 * 
 */
@Service("taskService")
public class TaskServiceImpl implements TaskService{

  private static Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

  @Resource
  private TaskMapper<Task, TaskQuery> taskMapper;

	/**
	 * @Description: 根据条件查询列表
	 */
  @Override
  public List<Task> findListByQuery(TaskQuery query){
  	return this.taskMapper.selectList(query);
  }

  @Override
	/**
	 * @Description: 根据条件查询数量
	 */
  public Integer findCountByQuery(TaskQuery query){
  	return this.taskMapper.selectCount(query);
  }

  @Override
	/**
	 * @Description: 根据条件更新
	 */
  public Integer updateByQuery(Task bean, TaskQuery query){
  	return this.taskMapper.updateByQuery(bean, query);
  }

  @Override
	/**
	 * @Description: 根据条件删除
	 */
  public Integer deleteByQuery(TaskQuery query){
  	return this.taskMapper.deleteByQuery(query);
  }

  @Override
	/**
	 * @Description: 分页查询
	 */

  public PaginationResultVO<Task> findListByPage(TaskQuery query){  	Integer count = this.taskMapper.selectCount(query);
  	int pageSize = query.getPageSize() == null? PageSize.SIZE15.getSize() : query.getPageSize();
  	SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
  	query.setSimplePage(page);
  	List<Task> list = this.taskMapper.selectList(query);
  	PaginationResultVO<Task> result = new PaginationResultVO<Task>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
  	return result;  }

  @Override
	/**
	 * @Description: 新增
	 */
  public Integer add(Task bean){
  	return this.taskMapper.insert(bean);  }

  @Override
	/**
	 * @Description: 批量新增
	 */
  public Integer addBatch(List<Task> listBean){
  	if(listBean == null || listBean.isEmpty())
  		return 0;
  	return this.taskMapper.insertBatch(listBean);
  }

  @Override
	/**
	 * @Description: 批量新增或更新
	 */
  public Integer addOrUpdateBatch(List<Task> listBean){
  	if(listBean == null || listBean.isEmpty())
  		return 0;
  	return this.taskMapper.insertOrUpdateBatch(listBean);
  }

  @Override
	/**
	 * @Description: 根据Id查询
	 */
  public Task getById(Long id){
  	return this.taskMapper.selectById(id);
  }

  @Override
	/**
	 * @Description: 根据Id更新
	 */
  public Integer updateById(Task bean, Long id){
  	return this.taskMapper.updateById(bean, id);
  }

  @Override
	/**
	 * @Description: 根据Id删除
	 */
  public Integer deleteById(Long id){
  	return this.taskMapper.deleteById(id);
  }




	@Override
	public List<Task> getNeedRemindTasks() {
		TaskQuery query = new TaskQuery();

		// 查询未开始或进行中的任务（可能需要提醒的任务）
		query.setCompletionStatus(0); // 未开始的任务
		List<Task> unstartedTasks = this.findListByQuery(query);

		query.setCompletionStatus(1); // 进行中的任务
		List<Task> inProgressTasks = this.findListByQuery(query);

		// 合并结果
		List<Task> allTasks = new ArrayList<>();
		allTasks.addAll(unstartedTasks);
		allTasks.addAll(inProgressTasks);

		// 过滤出今天创建的任务（作为示例，实际可以根据业务需求调整）
		List<Task> todayTasks = new ArrayList<>();
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String todayStr = sdf.format(today);

		for (Task task : allTasks) {
			if (task.getCreateTime() != null) {
				String taskDateStr = sdf.format(task.getCreateTime());
				if (todayStr.equals(taskDateStr)) {
					todayTasks.add(task);
				}
			}
		}

		return todayTasks;
	}




	/**
	 * 检查任务是否需要提醒
	 */
	private void checkTaskRemind(Task task) {
		try {
			// 判断是否需要提醒的简单逻辑
			if (shouldRemind(task)) {
				sendTaskRemind(task);
			}
		} catch (Exception e) {
			log.error("检查任务提醒失败，任务ID: {}", task.getId(), e);
		}
	}

	/**
	 * 判断任务是否需要提醒
	 */
	private boolean shouldRemind(Task task) {
		// 简单逻辑：未开始的任务需要提醒
		if (task.getCompletionStatus() != null && task.getCompletionStatus() == 0) {
			return true;
		}

		// 或者：今天创建的任务需要提醒
		if (task.getCreateTime() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String taskDate = sdf.format(task.getCreateTime());
			String today = sdf.format(new Date());
			return today.equals(taskDate);
		}

		return false;
	}

	/**
	 * 发送任务提醒（简单的控制台输出）
	 */
	private void sendTaskRemind(Task task) {
		try {
			String remindMessage = buildRemindMessage(task);
			log.info("发送任务提醒: {}", remindMessage);

			// 这里可以扩展为发送邮件、短信等
			// 目前先打印到日志
			System.out.println("====== 任务提醒 ======");
			System.out.println("任务ID: " + task.getId());
			System.out.println("用户ID: " + task.getUserId());
			System.out.println("任务类型: " + task.getTaskType());
			System.out.println("目标描述: " + task.getPersonalGoal());
			System.out.println("当前状态: " + getStatusDesc(task.getCompletionStatus()));
			System.out.println("提醒时间: " + new Date());
			System.out.println("====================");

			// TODO: 可以在这里集成邮件发送、短信发送等
			// sendEmailRemind(task);
			// sendSmsRemind(task);

		} catch (Exception e) {
			log.error("发送提醒失败，任务ID: {}", task.getId(), e);
		}
	}

	/**
	 * 构建提醒消息
	 */
	private String buildRemindMessage(Task task) {
		String statusDesc = getStatusDesc(task.getCompletionStatus());
		return String.format("用户[%s]的[%s]任务需要关注，当前状态：%s，目标：%s",
				task.getUserId(), task.getTaskType(), statusDesc, task.getPersonalGoal());
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
	@Override
	public void sendTaskRemind(Long taskId) {
		try {
			Task task = this.getById(taskId);
			if (task != null) {
				// 构建提醒消息 - 特别注明是新创建的任务
				String remindMessage = String.format("新任务提醒！用户[%s]创建了[%s]任务，目标：%s",
						task.getUserId(),
						task.getTaskType(),
						task.getPersonalGoal());

				// 记录提醒日志
				log.info("发送新任务提醒 - 任务ID: {}, 用户: {}, 任务类型: {}",
						taskId, task.getUserId(), task.getTaskType());

				// 控制台输出提醒信息
				printRemindInfo(task);
			}
		} catch (Exception e) {
			log.error("发送任务提醒失败，任务ID: {}", taskId, e);
		}
	}

	// 辅助方法：打印提醒信息到控制台
	private void printRemindInfo(Task task) {
		System.out.println("====== 🆕 新任务创建提醒 ======");
		System.out.println("📝 任务ID: " + task.getId());
		System.out.println("👤 用户ID: " + task.getUserId());
		System.out.println("📌 任务类型: " + task.getTaskType());
		System.out.println("🎯 目标描述: " + task.getPersonalGoal());
		System.out.println("📊 当前状态: " + getStatusDesc(task.getCompletionStatus()));
		System.out.println("📅 创建时间: " + task.getCreateTime());
		System.out.println("⏰ 持续天数: " + task.getContinuousDays() + "天");
		System.out.println("⏱️ 提醒时间: " + new Date());
		System.out.println("==========================");
	}
}