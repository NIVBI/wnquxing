package com.wnquxing.service;

import com.wnquxing.entity.po.Word;
import com.wnquxing.entity.query.WordQuery;
import com.wnquxing.entity.vo.PaginationResultVO;

import java.util.List;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:单词信息表Service
 * 
 * @Date:2026-01-12
 * 
 */
public interface WordService{

	/**
	 * @Description: 根据条件查询列表
	 */
  List<Word> findListByQuery(WordQuery query);

	/**
	 * @Description: 根据条件查询数量
	 */
  Integer findCountByQuery(WordQuery query);

	/**
	 * @Description: 根据条件更新
	 */
  Integer updateByQuery(Word bean, WordQuery query);

	/**
	 * @Description: 根据条件删除
	 */
  Integer deleteByQuery(WordQuery query);

	/**
	 * @Description: 分页查询
	 */
  PaginationResultVO<Word> findListByPage(WordQuery query);

	/**
	 * @Description: 新增
	 */
  Integer add(Word bean);

	/**
	 * @Description: 批量新增
	 */
  Integer addBatch(List<Word> listBean);

	/**
	 * @Description: 批量新增或更新
	 */
  Integer addOrUpdateBatch(List<Word> listBean);

	/**
	 * @Description: 根据Id查询
	 */
  Word getById(Long id);

	/**
	 * @Description: 根据Id更新
	 */
  Integer updateById(Word bean, Long id);

	/**
	 * @Description: 根据Id删除
	 */
  Integer deleteById(Long id);

	/**
	 * @Description: 根据English查询
	 */
  Word getByEnglish(String english);

	/**
	 * @Description: 根据English更新
	 */
  Integer updateByEnglish(Word bean, String english);

	/**
	 * @Description: 根据English删除
	 */
  Integer deleteByEnglish(String english);

}