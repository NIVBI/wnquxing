package com.wnquxing.controller;

import com.wnquxing.entity.po.CheckIn;
import com.wnquxing.entity.query.CheckInQuery;
import com.wnquxing.entity.vo.ResponseVO;
import com.wnquxing.service.CheckInService;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:用户打卡记录表Controller
 * 
 * @Date:2026-01-12
 * 
 */
@RestController
@RequestMapping("/checkIn")
public class CheckInController extends ABaseController{

  @Resource
  private CheckInService checkInService;

  @RequestMapping("loadDataList")
  public ResponseVO loadDataList(CheckInQuery query){
  	return getSuccessResponseVO(checkInService.findListByPage(query));
  }
	/**
	 * @Description: 新增
	 */
  @RequestMapping("add")
  public ResponseVO add(CheckIn bean){
  	this.checkInService.add(bean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 批量新增
	 */
  @RequestMapping("addBatch")
  public ResponseVO addBatch(@RequestBody List<CheckIn> listBean){
  	this.checkInService.addBatch(listBean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 批量新增或更新
	 */
  @RequestMapping("addOrUpdateBatch")
  public ResponseVO addOrUpdateBatch(List<CheckIn> listBean){
  	this.checkInService.addOrUpdateBatch(listBean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据Id查询
	 */
  @RequestMapping("getById")
  public ResponseVO getById(Long id){
  	return getSuccessResponseVO(this.checkInService.getById(id));
  }

	/**
	 * @Description: 根据Id更新
	 */
  @RequestMapping("updateById")
  public ResponseVO updateById(CheckIn bean, Long id){
  	this.checkInService.updateById(bean, id);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据Id删除
	 */
  @RequestMapping("deleteById")
  public ResponseVO deleteById(Long id){
  	this.checkInService.deleteById(id);
  	return getSuccessResponseVO(null);
  }

}