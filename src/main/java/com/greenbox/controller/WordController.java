package com.greenbox.controller;

import com.greenbox.entity.po.Word;
import com.greenbox.entity.query.WordQuery;
import com.greenbox.entity.vo.ResponseVO;
import com.greenbox.service.WordService;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

}