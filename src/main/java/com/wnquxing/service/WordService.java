package com.wnquxing.service;

import com.wnquxing.entity.po.Word;
import com.wnquxing.entity.query.WordQuery;
import com.wnquxing.entity.vo.PaginationResultVO;

import java.util.List;
import java.util.Map;

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

	/**
	 * @Description: 根据单词等级获取推送单词列表
	 * @param wordLevel 单词等级（0:初中, 1:高中, 2:四级, 3:六级, 4:考研）
	 * @param count 推送数量
	 * @return 单词列表（包含0-wordLevel级别的单词）
	 */
	List<Word> getPushWordsByLevel(Integer wordLevel, Integer count);

	/**
	 * @Description: 获取分级推送单词（分阶段推送）
	 * @param wordLevel 目标单词等级
	 * @param stage 当前阶段（0:初中阶段, 1:高中阶段, 2:四级阶段, 3:六级阶段, 4:考研阶段）
	 * @param count 每阶段推送数量
	 * @return 当前阶段的单词列表
	 */
	List<Word> getPushWordsByStage(Integer wordLevel, Integer stage, Integer count);

	/**
	 * @Description: 获取单词统计信息
	 * @return 各等级单词数量统计
	 */
	Map<String, Object> getWordStatistics();

	/**
	 * @Description: 获取推荐单词等级
	 * @param educationLevel 教育水平（0:初中, 1:高中, 2:大学, 3:研究生）
	 * @return 推荐的单词等级
	 */
	Integer getRecommendedWordLevel(Integer educationLevel);

	/**
	 * @Description: 随机获取单词（用于测试或复习）
	 * @param wordLevel 单词等级（可选，不传则随机所有等级）
	 * @param count 获取数量
	 * @return 随机单词列表
	 */
	List<Word> getRandomWords(Integer wordLevel, Integer count);

}