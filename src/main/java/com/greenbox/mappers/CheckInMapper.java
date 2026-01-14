package com.greenbox.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:用户打卡记录表Mapper
 * 
 * @Date:2026-01-12
 * 
 */
public interface CheckInMapper<CheckIn,P> extends BaseMapper{

	/**
	 * @Description: 根据Id查询
	 */
  CheckIn selectById(@Param("id") Long id);

	/**
	 * @Description: 根据Id更新
	 */
  Integer updateById(@Param("bean") CheckIn bean, @Param("id") Long id);

	/**
	 * @Description: 根据Id删除
	 */
  Integer deleteById(@Param("id") Long id);

}