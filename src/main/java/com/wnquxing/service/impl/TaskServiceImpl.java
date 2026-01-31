package com.wnquxing.service.impl;

import com.wnquxing.entity.Constants;
import com.wnquxing.entity.dto.MessageSendDto;
import com.wnquxing.entity.enums.TaskStatusEnum;
import com.wnquxing.mappers.TaskMapper;
import com.wnquxing.entity.enums.PageSize;
import com.wnquxing.entity.po.Task;
import com.wnquxing.entity.query.SimplePage;
import com.wnquxing.entity.query.TaskQuery;
import com.wnquxing.entity.vo.PaginationResultVO;
import com.wnquxing.service.TaskService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.wnquxing.websocket.ChannelContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
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


	// 修改这部分：
	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Resource
	private ChannelContextUtil channelContextUtil;


	// ================ 任务提醒相关方法 ================

	/**
	 * 创建任务并设置每日提醒
	 */
	public Integer createTaskWithDailyReminder(Task task, Date remindStartTime, Date remindEndTime, Integer remindHour) {
		if (remindHour == null) {
			remindHour = 8; // 默认早上8点
		}

		// 1. 验证时间参数
		if (remindStartTime == null || remindEndTime == null) {
			throw new IllegalArgumentException("提醒开始时间和结束时间不能为空");
		}

		if (remindEndTime.before(remindStartTime)) {
			throw new IllegalArgumentException("提醒结束时间不能早于开始时间");
		}

		Date now = new Date();
		if (remindEndTime.before(now)) {
			throw new IllegalArgumentException("提醒结束时间不能早于当前时间");
		}

		// 2. 设置任务默认状态
		if (task.getCompletionStatus() == null) {
			task.setCompletionStatus(TaskStatusEnum.UNSTARTED.getStatus());
		}

		// 3. 设置创建时间
		task.setCreateTime(now);

		// 4. 插入任务
		Integer result = taskMapper.insert(task);

		// 5. 设置每日提醒
		if (result > 0) {
			setDailyTaskReminder(task.getId(), task.getUserId(), task.getPersonalGoal(),
					remindStartTime, remindEndTime, remindHour);
			log.info("任务创建成功并设置每日提醒，任务ID: {}, 用户ID: {}, 提醒时间: {}点",
					task.getId(), task.getUserId(), remindHour);
		}

		return result;
	}

	/**
	 * 设置每日任务提醒
	 */
	private void setDailyTaskReminder(Long taskId, String userId, String taskInfo,
									  Date startTime, Date endTime, Integer remindHour) {
		if (redisTemplate == null) {
			log.error("RedisTemplate未初始化，无法设置任务提醒");
			return;
		}

		String key = Constants.REDIS_KEY_TASK_DAILY_REMINDER + taskId;
		HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();

		Map<Object, Object> reminderData = new HashMap<>();
		reminderData.put("userId", userId);
		reminderData.put("taskInfo", taskInfo);
		reminderData.put("remindHour", remindHour);
		reminderData.put("remindStartTime", startTime.getTime());
		reminderData.put("remindEndTime", endTime.getTime());
		reminderData.put("lastRemindDate", "");
		reminderData.put("remindCount", 0);

		hashOps.putAll(key, reminderData);

		// 设置过期时间（任务结束时间后7天过期）
		long expireSeconds = (endTime.getTime() - System.currentTimeMillis()) / 1000 + 7 * 24 * 3600;
		if (expireSeconds > 0) {
			redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
		}

		log.debug("已设置每日任务提醒，任务ID: {}, 提醒时段: {} 到 {}, 每天{}点提醒",
				taskId, startTime, endTime, remindHour);
	}

	/**
	 * 检查并发送每日任务提醒（定时任务调用）
	 */
	@Scheduled(cron = "0 0 8 * * ?") // 每天8点执行
	public void checkAndSendDailyTaskReminders() {
		if (redisTemplate == null) {
			log.warn("RedisTemplate未初始化，跳过任务提醒检查");
			return;
		}

		log.info("开始执行每日任务提醒检查...");

		try {
			// 使用RedisTemplate获取所有设置了每日提醒的任务
			Set<String> reminderKeys = redisTemplate.keys(Constants.REDIS_KEY_TASK_DAILY_REMINDER + "*");

			if (reminderKeys == null || reminderKeys.isEmpty()) {
				log.info("没有需要提醒的每日任务");
				return;
			}

			int sentCount = 0;
			int skippedCount = 0;
			Date today = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String todayStr = dateFormat.format(today);
			HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();

			for (String key : reminderKeys) {
				try {
					// 从key中提取taskId
					String taskIdStr = key.replace(Constants.REDIS_KEY_TASK_DAILY_REMINDER, "");
					Long taskId = Long.parseLong(taskIdStr);

					// 获取提醒信息
					Map<Object, Object> reminderInfo = hashOps.entries(key);

					if (reminderInfo == null || reminderInfo.isEmpty()) {
						redisTemplate.delete(key);
						continue;
					}

					// 检查任务状态（从数据库查询）
					Task task = taskMapper.selectById(taskId);
					if (task == null || TaskStatusEnum.COMPLETED.getStatus().equals(task.getCompletionStatus())) {
						// 任务已完成或不存在，删除提醒
						redisTemplate.delete(key);
						log.debug("任务已完成或不存在，删除提醒配置，任务ID: {}", taskId);
						continue;
					}

					// 检查提醒时间范围
					Long remindStartTime = Long.parseLong(reminderInfo.get("remindStartTime").toString());
					Long remindEndTime = Long.parseLong(reminderInfo.get("remindEndTime").toString());
					Long currentTime = System.currentTimeMillis();

					if (currentTime < remindStartTime || currentTime > remindEndTime) {
						// 不在提醒时间范围内
						skippedCount++;
						continue;
					}

					// 检查今天是否已经提醒过
					String lastRemindDate = (String) reminderInfo.get("lastRemindDate");
					if (todayStr.equals(lastRemindDate)) {
						// 今天已经提醒过了
						skippedCount++;
						continue;
					}

					// 发送提醒
					sendTaskReminder(taskId);

					// 更新最后提醒日期
					hashOps.put(key, "lastRemindDate", todayStr);

					// 更新提醒次数
					Object remindCountObj = reminderInfo.get("remindCount");
					Integer remindCount = remindCountObj != null ? Integer.parseInt(remindCountObj.toString()) : 0;
					hashOps.put(key, "remindCount", remindCount + 1);

					sentCount++;
					log.debug("已发送每日任务提醒，任务ID: {}", taskId);

				} catch (Exception e) {
					log.error("处理每日任务提醒时出错，key: {}", key, e);
				}
			}

			log.info("每日任务提醒检查完成，共发送 {} 个提醒，跳过 {} 个", sentCount, skippedCount);

		} catch (Exception e) {
			log.error("执行每日任务提醒检查时发生异常", e);
		}
	}

	/**
	 * 发送任务提醒消息
	 */
	private void sendTaskReminder(Long taskId) {
		try {
			Task task = taskMapper.selectById(taskId);
			if (task == null) {
				log.error("任务不存在，无法发送提醒，任务ID: {}", taskId);
				return;
			}

			// 准备提醒消息内容
			Map<String, Object> messageContent = new HashMap<>();
			messageContent.put("type", "TASK_REMINDER");
			messageContent.put("taskId", task.getId());
			messageContent.put("taskTitle", task.getPersonalGoal());
			messageContent.put("taskType", task.getTaskType());
			messageContent.put("completionStatus", task.getCompletionStatus());
			messageContent.put("continuousDays", task.getContinuousDays());
			messageContent.put("remindTime", System.currentTimeMillis());
			messageContent.put("message", "您的任务提醒：" + task.getPersonalGoal());

			// 创建消息DTO
			MessageSendDto messageSendDto = new MessageSendDto();
			messageSendDto.setMessageSend2Type(1); // 1表示发送给用户
			messageSendDto.setSendUserId("system");
			messageSendDto.setSendUserNickname("系统提醒");
			messageSendDto.setReceiveUserId(task.getUserId());
			messageSendDto.setMessageType(1001); // 任务提醒消息类型
			messageSendDto.setMessageContent(messageContent);
			messageSendDto.setSendTime(System.currentTimeMillis());

			// 通过WebSocket发送消息
			if (channelContextUtil != null) {
				channelContextUtil.sendMsg(messageSendDto);
				log.info("任务提醒消息已发送，任务ID: {}, 用户ID: {}", taskId, task.getUserId());
			} else {
				log.warn("ChannelContextUtil未注入，无法发送WebSocket消息");
			}

		} catch (Exception e) {
			log.error("发送任务提醒消息失败，任务ID: {}", taskId, e);
		}
	}

	/**
	 * 更新任务每日提醒设置
	 */
	public Boolean updateTaskDailyReminder(Long taskId, Date newStartTime, Date newEndTime, Integer newRemindHour) {
		try {
			Task task = taskMapper.selectById(taskId);
			if (task == null) {
				log.error("任务不存在，无法更新提醒设置，任务ID: {}", taskId);
				return false;
			}

			if (redisTemplate == null) {
				log.error("RedisTemplate未初始化，无法更新提醒设置");
				return false;
			}

			String key = Constants.REDIS_KEY_TASK_DAILY_REMINDER + taskId;
			HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
			Map<Object, Object> existingInfo = hashOps.entries(key);

			if (existingInfo == null || existingInfo.isEmpty()) {
				log.error("任务没有设置每日提醒，无法更新，任务ID: {}", taskId);
				return false;
			}

			// 更新提醒设置
			if (newStartTime != null) {
				hashOps.put(key, "remindStartTime", newStartTime.getTime());
			}

			if (newEndTime != null) {
				hashOps.put(key, "remindEndTime", newEndTime.getTime());

				// 更新过期时间
				long expireSeconds = (newEndTime.getTime() - System.currentTimeMillis()) / 1000 + 7 * 24 * 3600;
				if (expireSeconds > 0) {
					redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
				}
			}

			if (newRemindHour != null) {
				hashOps.put(key, "remindHour", newRemindHour);
			}

			log.info("任务每日提醒设置已更新，任务ID: {}", taskId);
			return true;

		} catch (Exception e) {
			log.error("更新任务每日提醒设置失败，任务ID: {}", taskId, e);
			return false;
		}
	}

	/**
	 * 取消任务每日提醒
	 */
	public Boolean cancelTaskDailyReminder(Long taskId) {
		try {
			if (redisTemplate == null) {
				log.error("RedisTemplate未初始化，无法取消提醒");
				return false;
			}

			String key = Constants.REDIS_KEY_TASK_DAILY_REMINDER + taskId;
			Boolean deleted = redisTemplate.delete(key);

			if (Boolean.TRUE.equals(deleted)) {
				log.info("任务每日提醒已取消，任务ID: {}", taskId);
				return true;
			} else {
				log.debug("任务每日提醒不存在，任务ID: {}", taskId);
				return false;
			}
		} catch (Exception e) {
			log.error("取消任务每日提醒失败，任务ID: {}", taskId, e);
			return false;
		}
	}

	/**
	 * 获取任务提醒设置信息
	 */
	public Map<String, Object> getTaskReminderInfo(Long taskId) {
		if (redisTemplate == null) {
			log.error("RedisTemplate未初始化，无法获取提醒信息");
			return null;
		}

		String key = Constants.REDIS_KEY_TASK_DAILY_REMINDER + taskId;
		HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
		Map<Object, Object> reminderInfo = hashOps.entries(key);

		if (reminderInfo == null || reminderInfo.isEmpty()) {
			return null;
		}

		Map<String, Object> result = new HashMap<>();
		for (Map.Entry<Object, Object> entry : reminderInfo.entrySet()) {
			result.put(entry.getKey().toString(), entry.getValue());
		}

		// 添加任务基本信息
		Task task = taskMapper.selectById(taskId);
		if (task != null) {
			result.put("taskInfo", task);
		}

		return result;
	}

	/**
	 * 获取用户的所有任务提醒设置
	 */
	public List<Map<String, Object>> getUserTaskReminders(String userId) {
		List<Map<String, Object>> result = new ArrayList<>();

		try {
			if (redisTemplate == null) {
				log.error("RedisTemplate未初始化，无法获取用户提醒列表");
				return result;
			}

			Set<String> reminderKeys = redisTemplate.keys(Constants.REDIS_KEY_TASK_DAILY_REMINDER + "*");

			if (reminderKeys != null) {
				HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();

				for (String key : reminderKeys) {
					Map<Object, Object> reminderInfo = hashOps.entries(key);

					if (reminderInfo != null && !reminderInfo.isEmpty()) {
						String taskUserId = (String) reminderInfo.get("userId");

						if (userId.equals(taskUserId)) {
							// 提取taskId
							String taskIdStr = key.replace(Constants.REDIS_KEY_TASK_DAILY_REMINDER, "");
							Long taskId = Long.parseLong(taskIdStr);

							Map<String, Object> item = new HashMap<>();
							for (Map.Entry<Object, Object> entry : reminderInfo.entrySet()) {
								item.put(entry.getKey().toString(), entry.getValue());
							}

							// 添加任务详情
							Task task = taskMapper.selectById(taskId);
							if (task != null) {
								item.put("taskDetail", task);
								result.add(item);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("获取用户任务提醒列表失败，用户ID: {}", userId, e);
		}

		return result;
	}

	/**
	 * 手动触发任务提醒（用于测试）
	 */
	public void triggerTaskReminder(Long taskId) {
		sendTaskReminder(taskId);
	}

	/**
	 * 检查当前任务是否需要提醒（用于API调用）
	 */
	public Boolean checkTaskNeedRemind(Long taskId) {
		try {
			if (redisTemplate == null) {
				log.error("RedisTemplate未初始化，无法检查提醒状态");
				return false;
			}

			String key = Constants.REDIS_KEY_TASK_DAILY_REMINDER + taskId;
			HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
			Map<Object, Object> reminderInfo = hashOps.entries(key);

			if (reminderInfo == null || reminderInfo.isEmpty()) {
				return false;
			}

			// 检查任务状态
			Task task = taskMapper.selectById(taskId);
			if (task == null || TaskStatusEnum.COMPLETED.getStatus().equals(task.getCompletionStatus())) {
				return false;
			}

			// 检查时间范围
			Long remindStartTime = Long.parseLong(reminderInfo.get("remindStartTime").toString());
			Long remindEndTime = Long.parseLong(reminderInfo.get("remindEndTime").toString());
			Long currentTime = System.currentTimeMillis();

			if (currentTime < remindStartTime || currentTime > remindEndTime) {
				return false;
			}

			// 检查今天是否已经提醒过
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String todayStr = dateFormat.format(new Date());
			String lastRemindDate = (String) reminderInfo.get("lastRemindDate");

			return !todayStr.equals(lastRemindDate);

		} catch (Exception e) {
			log.error("检查任务是否需要提醒时出错，任务ID: {}", taskId, e);
			return false;
		}
	}


}