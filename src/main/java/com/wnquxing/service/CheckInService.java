package com.wnquxing.service;

import com.wnquxing.entity.po.CheckIn;
import com.wnquxing.entity.query.CheckInQuery;
import com.wnquxing.entity.vo.PaginationResultVO;

import java.util.List;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:用户打卡记录表Service
 * 
 * @Date:2026-01-12
 * 
 */
public interface CheckInService{

	/**
	 * @Description: 根据条件查询列表
	 */
  List<CheckIn> findListByQuery(CheckInQuery query);

	/**
	 * @Description: 根据条件查询数量
	 */
  Integer findCountByQuery(CheckInQuery query);

	/**
	 * @Description: 根据条件更新
	 */
  Integer updateByQuery(CheckIn bean, CheckInQuery query);

	/**
	 * @Description: 根据条件删除
	 */
  Integer deleteByQuery(CheckInQuery query);

	/**
	 * @Description: 分页查询
	 */
  PaginationResultVO<CheckIn> findListByPage(CheckInQuery query);

	/**
	 * @Description: 新增
	 */
  Integer add(CheckIn bean);

	/**
	 * @Description: 批量新增
	 */
  Integer addBatch(List<CheckIn> listBean);

	/**
	 * @Description: 批量新增或更新
	 */
  Integer addOrUpdateBatch(List<CheckIn> listBean);

	/**
	 * @Description: 根据Id查询
	 */
  CheckIn getById(Long id);

	/**
	 * @Description: 根据Id更新
	 */
  Integer updateById(CheckIn bean, Long id);

	/**
	 * @Description: 根据Id删除
	 */
  Integer deleteById(Long id);

}