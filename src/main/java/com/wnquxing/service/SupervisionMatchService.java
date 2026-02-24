package com.wnquxing.service;

import com.wnquxing.entity.po.SupervisionMatch;
import com.wnquxing.entity.query.SupervisionMatchQuery;
import com.wnquxing.entity.vo.PaginationResultVO;

import java.util.List;

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

	/**
	 * @Description: 随机匹配房间
	 * @param userId 用户ID
	 * @return 匹配到的房间信息，返回null表示正在等待匹配
	 */
	SupervisionMatch randomMatch(Long userId);

	/**
	 * @Description: 更新监督时长
	 * @param roomName 房间名称
	 * @param duration 监督时长（分钟）
	 * @return 操作结果
	 */
	Integer updateSupervisionDuration(String roomName, Integer duration);

	/**
	 * @Description: 取消匹配
	 * @param userId 用户ID
	 * @return 操作结果
	 */
	Integer cancelMatch(Long userId);

	/**
	 * @Description: 退出房间
	 * @param roomName 房间名称
	 * @param userId 用户ID
	 * @return 操作结果
	 */
	Integer leaveRoom(String roomName, Long userId);

	/**
	 * @Description: 查询匹配状态
	 * @param userId 用户ID
	 * @return 匹配状态（0-等待中 1-匹配成功 2-对方已离开）
	 */
	Integer getMatchStatus(Long userId);

	/**
	 * @Description: 获取用户所在房间
	 * @param userId 用户ID
	 * @return 房间信息
	 */
	SupervisionMatch getUserRoom(Long userId);
}