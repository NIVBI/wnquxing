package com.greenbox.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:监督匹配房间信息表Mapper
 * 
 * @Date:2026-01-12
 * 
 */
public interface SupervisionMatchMapper<SupervisionMatch,P> extends BaseMapper{

	/**
	 * @Description: 根据Id查询
	 */
  SupervisionMatch selectById(@Param("id") Long id);

	/**
	 * @Description: 根据Id更新
	 */
  Integer updateById(@Param("bean") SupervisionMatch bean, @Param("id") Long id);

	/**
	 * @Description: 根据Id删除
	 */
  Integer deleteById(@Param("id") Long id);

	/**
	 * @Description: 根据RoomName查询
	 */
  SupervisionMatch selectByRoomName(@Param("roomName") String roomName);

	/**
	 * @Description: 根据RoomName更新
	 */
  Integer updateByRoomName(@Param("bean") SupervisionMatch bean, @Param("roomName") String roomName);

	/**
	 * @Description: 根据RoomName删除
	 */
  Integer deleteByRoomName(@Param("roomName") String roomName);

}