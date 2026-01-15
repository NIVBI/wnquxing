package com.wnquxing.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:用户基础信息表Mapper
 * 
 * @Date:2026-01-12
 * 
 */
public interface UserMapper<User,P> extends BaseMapper{

	/**
	 * @Description: 根据Id查询
	 */
  User selectById(@Param("id") Long id);

	/**
	 * @Description: 根据Id更新
	 */
  Integer updateById(@Param("bean") User bean, @Param("id") Long id);

	/**
	 * @Description: 根据Id删除
	 */
  Integer deleteById(@Param("id") Long id);

	/**
	 * @Description: 根据UserId查询
	 */
  User selectByUserId(@Param("userId") String userId);

	/**
	 * @Description: 根据UserId更新
	 */
  Integer updateByUserId(@Param("bean") User bean, @Param("userId") String userId);

	/**
	 * @Description: 根据UserId删除
	 */
  Integer deleteByUserId(@Param("userId") String userId);

	/**
	 * @Description: 根据Email查询
	 */
  User selectByEmail(@Param("email") String email);

	/**
	 * @Description: 根据Email更新
	 */
  Integer updateByEmail(@Param("bean") User bean, @Param("email") String email);

	/**
	 * @Description: 根据Email删除
	 */
  Integer deleteByEmail(@Param("email") String email);

}