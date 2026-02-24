package com.wnquxing.controller;

import com.wnquxing.entity.po.SupervisionMatch;
import com.wnquxing.entity.query.SupervisionMatchQuery;
import com.wnquxing.entity.vo.ResponseVO;
import com.wnquxing.exception.BusinessException;
import com.wnquxing.service.SupervisionMatchService;

import java.util.List;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	private static final Logger log = LoggerFactory.getLogger(SupervisionMatchController.class);
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

	// ==================== 新增的随机匹配功能接口 ====================

	/**
	 * @Description: 随机匹配房间
	 */
	@RequestMapping("randomMatch")
	public ResponseVO randomMatch(@RequestParam Long userId) {
		try {
			SupervisionMatch room = supervisionMatchService.randomMatch(userId);
			return getSuccessResponseVO(room);
		} catch (Exception e) {
			log.error("随机匹配失败，用户ID: {}", userId, e);
			return getServerErrorResponseVO(null);
		}
	}

	/**
	 * @Description: 更新监督时长
	 */
	@RequestMapping("updateDuration")
	public ResponseVO updateDuration(@RequestParam String roomName, @RequestParam Integer duration) {
		try {
			Integer result = supervisionMatchService.updateSupervisionDuration(roomName, duration);
			return getSuccessResponseVO(result);
		} catch (Exception e) {
			log.error("更新监督时长失败，房间名: {}", roomName, e);
			return getServerErrorResponseVO(null);
		}
	}

	/**
	 * @Description: 取消匹配
	 */
	@RequestMapping("cancelMatch")
	public ResponseVO cancelMatch(@RequestParam Long userId) {
		try {
			Integer result = supervisionMatchService.cancelMatch(userId);
			return getSuccessResponseVO(result);
		} catch (Exception e) {
			log.error("取消匹配失败，用户ID: {}", userId, e);
			return getServerErrorResponseVO(null);
		}
	}

	/**
	 * @Description: 退出房间
	 */
	@RequestMapping("leaveRoom")
	public ResponseVO leaveRoom(@RequestParam String roomName, @RequestParam Long userId) {
		try {
			Integer result = supervisionMatchService.leaveRoom(roomName, userId);
			return getSuccessResponseVO(result);
		} catch (Exception e) {
			log.error("退出房间失败，房间名: {}, 用户ID: {}", roomName, userId, e);
			return getServerErrorResponseVO(null);
		}
	}

	/**
	 * @Description: 查询匹配状态
	 */
	@RequestMapping("getMatchStatus")
	public ResponseVO getMatchStatus(@RequestParam Long userId) {
		try {
			Integer status = supervisionMatchService.getMatchStatus(userId);
			return getSuccessResponseVO(status);
		} catch (Exception e) {
			log.error("查询匹配状态失败，用户ID: {}", userId, e);
			return getServerErrorResponseVO(null);
		}
	}

	/**
	 * @Description: 获取用户所在房间
	 */
	@RequestMapping("getUserRoom")
	public ResponseVO getUserRoom(@RequestParam Long userId) {
		try {
			SupervisionMatch room = supervisionMatchService.getUserRoom(userId);
			return getSuccessResponseVO(room);
		} catch (Exception e) {
			log.error("获取用户房间信息失败，用户ID: {}", userId, e);
			return getServerErrorResponseVO(null);
		}
	}
}