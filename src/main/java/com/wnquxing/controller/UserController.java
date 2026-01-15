package com.wnquxing.controller;

import com.wnquxing.entity.po.User;
import com.wnquxing.entity.query.UserQuery;
import com.wnquxing.entity.vo.ResponseVO;
import com.wnquxing.service.UserService;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:用户基础信息表Controller
 * 
 * @Date:2026-01-12
 * 
 */
@RestController
@RequestMapping("/user")
public class UserController extends ABaseController{

  @Resource
  private UserService userService;

  @RequestMapping("loadDataList")
  public ResponseVO loadDataList(UserQuery query){
  	return getSuccessResponseVO(userService.findListByPage(query));
  }
	/**
	 * @Description: 新增
	 */
  @RequestMapping("add")
  public ResponseVO add(User bean){
  	this.userService.add(bean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 批量新增
	 */
  @RequestMapping("addBatch")
  public ResponseVO addBatch(@RequestBody List<User> listBean){
  	this.userService.addBatch(listBean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 批量新增或更新
	 */
  @RequestMapping("addOrUpdateBatch")
  public ResponseVO addOrUpdateBatch(List<User> listBean){
  	this.userService.addOrUpdateBatch(listBean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据Id查询
	 */
  @RequestMapping("getById")
  public ResponseVO getById(Long id){
  	return getSuccessResponseVO(this.userService.getById(id));
  }

	/**
	 * @Description: 根据Id更新
	 */
  @RequestMapping("updateById")
  public ResponseVO updateById(User bean, Long id){
  	this.userService.updateById(bean, id);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据Id删除
	 */
  @RequestMapping("deleteById")
  public ResponseVO deleteById(Long id){
  	this.userService.deleteById(id);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据UserId查询
	 */
  @RequestMapping("getByUserId")
  public ResponseVO getByUserId(String userId){
  	return getSuccessResponseVO(this.userService.getByUserId(userId));
  }

	/**
	 * @Description: 根据UserId更新
	 */
  @RequestMapping("updateByUserId")
  public ResponseVO updateByUserId(User bean, String userId){
  	this.userService.updateByUserId(bean, userId);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据UserId删除
	 */
  @RequestMapping("deleteByUserId")
  public ResponseVO deleteByUserId(String userId){
  	this.userService.deleteByUserId(userId);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据Email查询
	 */
  @RequestMapping("getByEmail")
  public ResponseVO getByEmail(String email){
  	return getSuccessResponseVO(this.userService.getByEmail(email));
  }

	/**
	 * @Description: 根据Email更新
	 */
  @RequestMapping("updateByEmail")
  public ResponseVO updateByEmail(User bean, String email){
  	this.userService.updateByEmail(bean, email);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据Email删除
	 */
  @RequestMapping("deleteByEmail")
  public ResponseVO deleteByEmail(String email){
  	this.userService.deleteByEmail(email);
  	return getSuccessResponseVO(null);
  }

}