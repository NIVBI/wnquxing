package com.wnquxing.service;

import com.wnquxing.entity.po.User;
import com.wnquxing.entity.query.UserQuery;
import com.wnquxing.entity.vo.PaginationResultVO;

import java.util.List;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:用户基础信息表Service
 * 
 * @Date:2026-01-12
 * 
 */
public interface UserService{

	/**
	 * @Description: 根据条件查询列表
	 */
  List<User> findListByQuery(UserQuery query);

	/**
	 * @Description: 根据条件查询数量
	 */
  Integer findCountByQuery(UserQuery query);

	/**
	 * @Description: 根据条件更新
	 */
  Integer updateByQuery(User bean, UserQuery query);

	/**
	 * @Description: 根据条件删除
	 */
  Integer deleteByQuery(UserQuery query);

	/**
	 * @Description: 分页查询
	 */
  PaginationResultVO<User> findListByPage(UserQuery query);

	/**
	 * @Description: 新增
	 */
  Integer add(User bean);

	/**
	 * @Description: 批量新增
	 */
  Integer addBatch(List<User> listBean);

	/**
	 * @Description: 批量新增或更新
	 */
  Integer addOrUpdateBatch(List<User> listBean);

	/**
	 * @Description: 根据Id查询
	 */
  User getById(Long id);

	/**
	 * @Description: 根据Id更新
	 */
  Integer updateById(User bean, Long id);

	/**
	 * @Description: 根据Id删除
	 */
  Integer deleteById(Long id);

	/**
	 * @Description: 根据UserId查询
	 */
  User getByUserId(String userId);

	/**
	 * @Description: 根据UserId更新
	 */
  Integer updateByUserId(User bean, String userId);

	/**
	 * @Description: 根据UserId删除
	 */
  Integer deleteByUserId(String userId);

	/**
	 * @Description: 根据Email查询
	 */
  User getByEmail(String email);

	/**
	 * @Description: 根据Email更新
	 */
  Integer updateByEmail(User bean, String email);

	/**
	 * @Description: 根据Email删除
	 */
  Integer deleteByEmail(String email);
	/**
	 * @Description: 用户注册
	 */
	Integer register(User user);

	/**
	 * @Description: 用户登录
	 */
	User login(UserQuery loginQuery);

}