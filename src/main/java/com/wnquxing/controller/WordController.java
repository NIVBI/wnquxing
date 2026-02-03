package com.wnquxing.controller;

import com.wnquxing.entity.po.Word;
import com.wnquxing.entity.query.WordQuery;
import com.wnquxing.entity.vo.ResponseVO;
import com.wnquxing.service.WordService;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:单词信息表Controller
 * 
 * @Date:2026-01-12
 * 
 */
@RestController
@RequestMapping("/word")
public class WordController extends ABaseController{

  @Resource
  private WordService wordService;

  @RequestMapping("loadDataList")
  public ResponseVO loadDataList(WordQuery query){
  	return getSuccessResponseVO(wordService.findListByPage(query));
  }
	/**
	 * @Description: 新增
	 */
  @RequestMapping("add")
  public ResponseVO add(Word bean){
  	this.wordService.add(bean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 批量新增
	 */
  @RequestMapping("addBatch")
  public ResponseVO addBatch(@RequestBody List<Word> listBean){
  	this.wordService.addBatch(listBean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 批量新增或更新
	 */
  @RequestMapping("addOrUpdateBatch")
  public ResponseVO addOrUpdateBatch(List<Word> listBean){
  	this.wordService.addOrUpdateBatch(listBean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据Id查询
	 */
  @RequestMapping("getById")
  public ResponseVO getById(Long id){
  	return getSuccessResponseVO(this.wordService.getById(id));
  }

	/**
	 * @Description: 根据Id更新
	 */
  @RequestMapping("updateById")
  public ResponseVO updateById(Word bean, Long id){
  	this.wordService.updateById(bean, id);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据Id删除
	 */
  @RequestMapping("deleteById")
  public ResponseVO deleteById(Long id){
  	this.wordService.deleteById(id);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据English查询
	 */
  @RequestMapping("getByEnglish")
  public ResponseVO getByEnglish(String english){
  	return getSuccessResponseVO(this.wordService.getByEnglish(english));
  }

	/**
	 * @Description: 根据English更新
	 */
  @RequestMapping("updateByEnglish")
  public ResponseVO updateByEnglish(Word bean, String english){
  	this.wordService.updateByEnglish(bean, english);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据English删除
	 */
  @RequestMapping("deleteByEnglish")
  public ResponseVO deleteByEnglish(String english){
  	this.wordService.deleteByEnglish(english);
  	return getSuccessResponseVO(null);
  }

	// ==================== 新增的单词推送功能 ====================

	/**
	 * @Description: 根据单词等级获取推送单词列表
	 * @param wordLevel 单词等级（0:初中, 1:高中, 2:四级, 3:六级, 4:考研）
	 * @param count 推送数量（可选，默认10个）
	 * @return 单词列表（包含0-wordLevel级别的单词）
	 */
	@RequestMapping("getPushWordsByLevel")
	public ResponseVO getPushWordsByLevel(@RequestParam Integer wordLevel,
										  @RequestParam(required = false, defaultValue = "10") Integer count) {
		return getSuccessResponseVO(this.wordService.getPushWordsByLevel(wordLevel, count));
	}

	/**
	 * @Description: 获取分级推送单词（分阶段推送）
	 * @param wordLevel 目标单词等级
	 * @param stage 当前阶段（0:初中阶段, 1:高中阶段, 2:四级阶段, 3:六级阶段, 4:考研阶段）
	 * @param count 每阶段推送数量（可选，默认10个）
	 * @return 当前阶段的单词列表
	 */
	@RequestMapping("getPushWordsByStage")
	public ResponseVO getPushWordsByStage(@RequestParam Integer wordLevel,
										  @RequestParam Integer stage,
										  @RequestParam(required = false, defaultValue = "10") Integer count) {
		return getSuccessResponseVO(this.wordService.getPushWordsByStage(wordLevel, stage, count));
	}

	/**
	 * @Description: 获取单词统计信息
	 * @return 各等级单词数量统计
	 */
	@RequestMapping("getWordStatistics")
	public ResponseVO getWordStatistics() {
		return getSuccessResponseVO(this.wordService.getWordStatistics());
	}

	/**
	 * @Description: 获取推荐单词等级
	 * @param educationLevel 教育水平（0:初中, 1:高中, 2:大学, 3:研究生）
	 * @return 推荐的单词等级
	 */
	@RequestMapping("getRecommendedWordLevel")
	public ResponseVO getRecommendedWordLevel(@RequestParam(required = false) Integer educationLevel) {
		return getSuccessResponseVO(this.wordService.getRecommendedWordLevel(educationLevel));
	}

	/**
	 * @Description: 随机获取单词（用于测试或复习）
	 * @param wordLevel 单词等级（可选，不传则随机所有等级）
	 * @param count 获取数量（可选，默认10个）
	 * @return 随机单词列表
	 */
	@RequestMapping("getRandomWords")
	public ResponseVO getRandomWords(@RequestParam(required = false) Integer wordLevel,
									 @RequestParam(required = false, defaultValue = "10") Integer count) {
		return getSuccessResponseVO(this.wordService.getRandomWords(wordLevel, count));
	}

}

