package com.wnquxing.controller;

import com.wnquxing.entity.po.SupervisionMatch;
import com.wnquxing.entity.query.SupervisionMatchQuery;
import com.wnquxing.entity.vo.ResponseVO;
import com.wnquxing.service.SupervisionMatchService;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:监督匹配房间信息表Controller
 * 
 * @Date:2026-01-12
 * 
 */
@RestController
@RequestMapping("/supervisionMatch")
public class SupervisionMatchController extends ABaseController{

  @Resource
  private SupervisionMatchService supervisionMatchService;

  @RequestMapping("loadDataList")
  public ResponseVO loadDataList(SupervisionMatchQuery query){
  	return getSuccessResponseVO(supervisionMatchService.findListByPage(query));
  }
	/**
	 * @Description: 新增
	 */
  @RequestMapping("add")
  public ResponseVO add(SupervisionMatch bean){
  	this.supervisionMatchService.add(bean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 批量新增
	 */
  @RequestMapping("addBatch")
  public ResponseVO addBatch(@RequestBody List<SupervisionMatch> listBean){
  	this.supervisionMatchService.addBatch(listBean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 批量新增或更新
	 */
  @RequestMapping("addOrUpdateBatch")
  public ResponseVO addOrUpdateBatch(List<SupervisionMatch> listBean){
  	this.supervisionMatchService.addOrUpdateBatch(listBean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据Id查询
	 */
  @RequestMapping("getById")
  public ResponseVO getById(Long id){
  	return getSuccessResponseVO(this.supervisionMatchService.getById(id));
  }

	/**
	 * @Description: 根据Id更新
	 */
  @RequestMapping("updateById")
  public ResponseVO updateById(SupervisionMatch bean, Long id){
  	this.supervisionMatchService.updateById(bean, id);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据Id删除
	 */
  @RequestMapping("deleteById")
  public ResponseVO deleteById(Long id){
  	this.supervisionMatchService.deleteById(id);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据RoomName查询
	 */
  @RequestMapping("getByRoomName")
  public ResponseVO getByRoomName(String roomName){
  	return getSuccessResponseVO(this.supervisionMatchService.getByRoomName(roomName));
  }

	/**
	 * @Description: 根据RoomName更新
	 */
  @RequestMapping("updateByRoomName")
  public ResponseVO updateByRoomName(SupervisionMatch bean, String roomName){
  	this.supervisionMatchService.updateByRoomName(bean, roomName);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据RoomName删除
	 */
  @RequestMapping("deleteByRoomName")
  public ResponseVO deleteByRoomName(String roomName){
  	this.supervisionMatchService.deleteByRoomName(roomName);
  	return getSuccessResponseVO(null);
  }

}