package com.wnquxing.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:书籍信息表Mapper
 * 
 * @Date:2026-01-12
 * 
 */
public interface BookMapper<Book,P> extends BaseMapper{

	/**
	 * @Description: 根据Id查询
	 */
  Book selectById(@Param("id") Long id);

	/**
	 * @Description: 根据Id更新
	 */
  Integer updateById(@Param("bean") Book bean, @Param("id") Long id);

	/**
	 * @Description: 根据Id删除
	 */
  Integer deleteById(@Param("id") Long id);

}