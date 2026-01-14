package com.greenbox.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:用户任务信息表Mapper
 * 
 * @Date:2026-01-12
 * 
 */
public interface TaskMapper<Task,P> extends BaseMapper{

	/**
	 * @Description: 根据Id查询
	 */
  Task selectById(@Param("id") Long id);

	/**
	 * @Description: 根据Id更新
	 */
  Integer updateById(@Param("bean") Task bean, @Param("id") Long id);

	/**
	 * @Description: 根据Id删除
	 */
  Integer deleteById(@Param("id") Long id);

}