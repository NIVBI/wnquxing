package com.greenbox.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:单词信息表Mapper
 * 
 * @Date:2026-01-12
 * 
 */
public interface WordMapper<Word,P> extends BaseMapper{

	/**
	 * @Description: 根据Id查询
	 */
  Word selectById(@Param("id") Long id);

	/**
	 * @Description: 根据Id更新
	 */
  Integer updateById(@Param("bean") Word bean, @Param("id") Long id);

	/**
	 * @Description: 根据Id删除
	 */
  Integer deleteById(@Param("id") Long id);

	/**
	 * @Description: 根据English查询
	 */
  Word selectByEnglish(@Param("english") String english);

	/**
	 * @Description: 根据English更新
	 */
  Integer updateByEnglish(@Param("bean") Word bean, @Param("english") String english);

	/**
	 * @Description: 根据English删除
	 */
  Integer deleteByEnglish(@Param("english") String english);

}