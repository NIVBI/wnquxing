package com.wnquxing.service;

import com.wnquxing.entity.po.Task;
import com.wnquxing.entity.query.TaskQuery;
import com.wnquxing.entity.vo.PaginationResultVO;

import java.util.List;

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




}