package com.wnquxing.service.impl;

import com.wnquxing.mappers.TaskMapper;
import com.wnquxing.entity.enums.PageSize;
import com.wnquxing.entity.po.Task;
import com.wnquxing.entity.query.SimplePage;
import com.wnquxing.entity.query.TaskQuery;
import com.wnquxing.entity.vo.PaginationResultVO;
import com.wnquxing.service.TaskService;

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
	public Integer quickAdd(String userId, String taskType, String personalGoal){
		Task bean = new Task();
		bean.setUserId(userId);
		bean.setTaskType(taskType);
		bean.setPersonalGoal(personalGoal);
		bean.setCompletionStatus(0);
		bean.setContinuousDays(0);
		bean.setCreateTime(new Date());
		return this.taskMapper.insert(bean);
	}

	@Override
	public Integer addWithStatus(String userId, String taskType, Integer completionStatus, String personalGoal){
		Task bean = new Task();
		bean.setUserId(userId);
		bean.setTaskType(taskType);
		bean.setCompletionStatus(completionStatus);
		bean.setPersonalGoal(personalGoal);
		bean.setContinuousDays(0);
		bean.setCreateTime(new Date());
		return this.taskMapper.insert(bean);
	}

	@Override
	public Integer addDailyTask(String userId, String taskType, String personalGoal){
		Task bean = new Task();
		bean.setUserId(userId);
		bean.setTaskType(taskType);
		bean.setPersonalGoal(personalGoal);
		bean.setCompletionStatus(0);
		bean.setContinuousDays(0);
		bean.setCreateTime(new Date());
		return this.taskMapper.insert(bean);
	}
}