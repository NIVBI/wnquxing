package com.wnquxing.service.impl;

import com.wnquxing.mappers.UserMapper;
import com.wnquxing.entity.enums.PageSize;
import com.wnquxing.entity.po.User;
import com.wnquxing.entity.query.SimplePage;
import com.wnquxing.entity.query.UserQuery;
import com.wnquxing.entity.vo.PaginationResultVO;
import com.wnquxing.service.UserService;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:用户基础信息表ServiceImpl
 * 
 * @Date:2026-01-12
 * 
 */
@Service("userService")
public class UserServiceImpl implements UserService{

  private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

  @Resource
  private UserMapper<User, UserQuery> userMapper;

	/**
	 * @Description: 根据条件查询列表
	 */
  @Override
  public List<User> findListByQuery(UserQuery query){
  	return this.userMapper.selectList(query);
  }

  @Override
	/**
	 * @Description: 根据条件查询数量
	 */
  public Integer findCountByQuery(UserQuery query){
  	return this.userMapper.selectCount(query);
  }

  @Override
	/**
	 * @Description: 根据条件更新
	 */
  public Integer updateByQuery(User bean, UserQuery query){
  	return this.userMapper.updateByQuery(bean, query);
  }

  @Override
	/**
	 * @Description: 根据条件删除
	 */
  public Integer deleteByQuery(UserQuery query){
  	return this.userMapper.deleteByQuery(query);
  }

  @Override
	/**
	 * @Description: 分页查询
	 */

  public PaginationResultVO<User> findListByPage(UserQuery query){  	Integer count = this.userMapper.selectCount(query);
  	int pageSize = query.getPageSize() == null? PageSize.SIZE15.getSize() : query.getPageSize();
  	SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
  	query.setSimplePage(page);
  	List<User> list = this.userMapper.selectList(query);
  	PaginationResultVO<User> result = new PaginationResultVO<User>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
  	return result;  }

  @Override
	/**
	 * @Description: 新增
	 */
  public Integer add(User bean){
  	return this.userMapper.insert(bean);  }

  @Override
	/**
	 * @Description: 批量新增
	 */
  public Integer addBatch(List<User> listBean){
  	if(listBean == null || listBean.isEmpty())
  		return 0;
  	return this.userMapper.insertBatch(listBean);
  }

  @Override
	/**
	 * @Description: 批量新增或更新
	 */
  public Integer addOrUpdateBatch(List<User> listBean){
  	if(listBean == null || listBean.isEmpty())
  		return 0;
  	return this.userMapper.insertOrUpdateBatch(listBean);
  }

  @Override
	/**
	 * @Description: 根据Id查询
	 */
  public User getById(Long id){
  	return this.userMapper.selectById(id);
  }

  @Override
	/**
	 * @Description: 根据Id更新
	 */
  public Integer updateById(User bean, Long id){
  	return this.userMapper.updateById(bean, id);
  }

  @Override
	/**
	 * @Description: 根据Id删除
	 */
  public Integer deleteById(Long id){
  	return this.userMapper.deleteById(id);
  }

  @Override
	/**
	 * @Description: 根据UserId查询
	 */
  public User getByUserId(String userId){
  	return this.userMapper.selectByUserId(userId);
  }

  @Override
	/**
	 * @Description: 根据UserId更新
	 */
  public Integer updateByUserId(User bean, String userId){
  	return this.userMapper.updateByUserId(bean, userId);
  }

  @Override
	/**
	 * @Description: 根据UserId删除
	 */
  public Integer deleteByUserId(String userId){
  	return this.userMapper.deleteByUserId(userId);
  }

  @Override
	/**
	 * @Description: 根据Email查询
	 */
  public User getByEmail(String email){
  	return this.userMapper.selectByEmail(email);
  }

  @Override
	/**
	 * @Description: 根据Email更新
	 */
  public Integer updateByEmail(User bean, String email){
  	return this.userMapper.updateByEmail(bean, email);
  }

  @Override
	/**
	 * @Description: 根据Email删除
	 */
  public Integer deleteByEmail(String email){
  	return this.userMapper.deleteByEmail(email);
  }

	/**
	 * @Description: 用户注册
	 */
	@Override
	public Integer register(User user) {
		// 1. 密码加密（MD5，与Controller层逻辑保持一致）
		user.setPassword(DigestUtils.md5Hex(user.getPassword().trim()));
		// 2. 填充创建时间
		user.setCreateTime(new Date());
		// 3. 调用新增方法
		return this.add(user);
	}

	/**
	 * @Description: 用户登录
	 */
	@Override
	public User login(UserQuery loginQuery) {
		// 1. 根据用户标识查询用户
		User dbUser = this.getByUserId(loginQuery.getUserId());
		if (dbUser == null) {
			return null;
		}
		// 2. 密码加密后对比
		String encryptPwd = DigestUtils.md5Hex(loginQuery.getPassword().trim());
		if (!encryptPwd.equals(dbUser.getPassword())) {
			return null;
		}
		// 3. 隐藏密码后返回
		dbUser.setPassword(null);
		return dbUser;
	}

}