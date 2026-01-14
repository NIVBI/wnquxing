package com.greenbox.service;

import com.greenbox.entity.po.SupervisionMatch;
import com.greenbox.entity.query.SupervisionMatchQuery;
import com.greenbox.entity.vo.PaginationResultVO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.greenbox.utils.DateUtils;
import com.greenbox.entity.enums.DateTimePatternEnum;
import java.util.Date;
/**
 * 
 * @Auther:关山越
 * 
 * @Description:监督匹配房间信息表Service
 * 
 * @Date:2026-01-12
 * 
 */
public interface SupervisionMatchService{

	/**
	 * @Description: 根据条件查询列表
	 */
  List<SupervisionMatch> findListByQuery(SupervisionMatchQuery query);

	/**
	 * @Description: 根据条件查询数量
	 */
  Integer findCountByQuery(SupervisionMatchQuery query);

	/**
	 * @Description: 根据条件更新
	 */
  Integer updateByQuery(SupervisionMatch bean, SupervisionMatchQuery query);

	/**
	 * @Description: 根据条件删除
	 */
  Integer deleteByQuery(SupervisionMatchQuery query);

	/**
	 * @Description: 分页查询
	 */
  PaginationResultVO<SupervisionMatch> findListByPage(SupervisionMatchQuery query);

	/**
	 * @Description: 新增
	 */
  Integer add(SupervisionMatch bean);

	/**
	 * @Description: 批量新增
	 */
  Integer addBatch(List<SupervisionMatch> listBean);

	/**
	 * @Description: 批量新增或更新
	 */
  Integer addOrUpdateBatch(List<SupervisionMatch> listBean);

	/**
	 * @Description: 根据Id查询
	 */
  SupervisionMatch getById(Long id);

	/**
	 * @Description: 根据Id更新
	 */
  Integer updateById(SupervisionMatch bean, Long id);

	/**
	 * @Description: 根据Id删除
	 */
  Integer deleteById(Long id);

	/**
	 * @Description: 根据RoomName查询
	 */
  SupervisionMatch getByRoomName(String roomName);

	/**
	 * @Description: 根据RoomName更新
	 */
  Integer updateByRoomName(SupervisionMatch bean, String roomName);

	/**
	 * @Description: 根据RoomName删除
	 */
  Integer deleteByRoomName(String roomName);

}