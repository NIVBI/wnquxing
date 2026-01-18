package com.wnquxing.controller;

import com.wnquxing.entity.enums.ResponseCodeEnum;
import com.wnquxing.entity.po.User;
import com.wnquxing.entity.query.UserQuery;
import com.wnquxing.entity.vo.ResponseVO;
import com.wnquxing.exception.BusinessException;
import com.wnquxing.service.UserService;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
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


	/**
	 * @Description: 注册
	 */
  @RequestMapping("register")
  public ResponseVO register(User bean){
	  // 1. 参数非空校验
	  if (bean.getUserId() == null || bean.getUserId().trim().isEmpty()) {
		  BusinessException e = new BusinessException(ResponseCodeEnum.CODE_600);
		  e.setMessage("用户标识不能为空");
		  return getBusinessErrorResponseVO(e, "注册失败");
	  }
	  if (bean.getPassword() == null || bean.getPassword().trim().isEmpty() || bean.getPassword().length() < 6) {
		  BusinessException e = new BusinessException(ResponseCodeEnum.CODE_600);
		  e.setMessage("密码不能为空且长度不能少于6位");
		  return getBusinessErrorResponseVO(e, "注册失败");
	  }
	  if (bean.getEmail() == null || bean.getEmail().trim().isEmpty() || !bean.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
		  BusinessException e = new BusinessException(ResponseCodeEnum.CODE_600);
		  e.setMessage("邮箱格式不正确");
		  return getBusinessErrorResponseVO(e, "注册失败");
	  }

	  // 2. 唯一性校验（userId/email）
	  if (userService.getByUserId(bean.getUserId()) != null) {
		  BusinessException e = new BusinessException(ResponseCodeEnum.CODE_601);
		  e.setMessage("该用户唯一标识已存在");
		  return getBusinessErrorResponseVO(e, "注册失败");
	  }
	  if (userService.getByEmail(bean.getEmail()) != null) {
		  BusinessException e = new BusinessException(ResponseCodeEnum.CODE_601);
		  e.setMessage("该邮箱已被注册");
		  return getBusinessErrorResponseVO(e, "注册失败");
	  }

	  // 3. 密码加密（示例：MD5，可替换为SHA256+盐值）
	  bean.setPassword(org.apache.commons.codec.digest.DigestUtils.md5Hex(bean.getPassword().trim()));

	  // 4. 自动填充创建时间
	  bean.setCreateTime(new Date());

	  // 5. 调用原有新增方法
	  userService.add(bean);
	  return getSuccessResponseVO("注册成功");
  }
	/**
	 * @Description: 用户登录
	 */
	@PostMapping("login")
	public ResponseVO login(@RequestBody @Valid UserQuery loginQuery) {
		// 1. 参数非空校验
		if (loginQuery.getUserId() == null || loginQuery.getUserId().trim().isEmpty()) {
			BusinessException e = new BusinessException("用户标识不能为空");
			e.setCode(ResponseCodeEnum.CODE_600.getCode());
			return getBusinessErrorResponseVO(e, "登录失败");
		}
		if (loginQuery.getPassword() == null || loginQuery.getPassword().trim().isEmpty()) {
			BusinessException e = new BusinessException("密码不能为空");
			e.setCode(ResponseCodeEnum.CODE_600.getCode());
			return getBusinessErrorResponseVO(e, "登录失败");
		}

		// 2. 查询用户是否存在
		User dbUser = userService.getByUserId(loginQuery.getUserId());
		if (dbUser == null) {
			BusinessException e = new BusinessException("用户不存在");
			e.setCode(ResponseCodeEnum.CODE_404.getCode());
			return getBusinessErrorResponseVO(e, "登录失败");
		}

		// 3. 密码校验（MD5加密后对比）
		String encryptPwd = org.apache.commons.codec.digest.DigestUtils.md5Hex(loginQuery.getPassword().trim());
		if (!encryptPwd.equals(dbUser.getPassword())) {
			BusinessException e = new BusinessException("密码错误");
			e.setCode(ResponseCodeEnum.CODE_600.getCode());
			return getBusinessErrorResponseVO(e, "登录失败");
		}

		// 4. 登录成功（可扩展生成token、更新最后登录时间等）
		// 示例：仅返回用户基础信息（隐藏密码）
		dbUser.setPassword(null);
		return getSuccessResponseVO(dbUser);
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