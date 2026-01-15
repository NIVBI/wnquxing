package com.wnquxing.service.impl;

import com.wnquxing.mappers.CheckInMapper;
import com.wnquxing.entity.enums.PageSize;
import com.wnquxing.entity.po.CheckIn;
import com.wnquxing.entity.query.SimplePage;
import com.wnquxing.entity.query.CheckInQuery;
import com.wnquxing.entity.vo.PaginationResultVO;
import com.wnquxing.service.CheckInService;

import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:用户打卡记录表ServiceImpl
 * 
 * @Date:2026-01-12
 * 
 */
@Service("checkInService")
public class CheckInServiceImpl implements CheckInService{

  private static Logger log = LoggerFactory.getLogger(CheckInServiceImpl.class);

  @Resource
  private CheckInMapper<CheckIn, CheckInQuery> checkInMapper;

	/**
	 * @Description: 根据条件查询列表
	 */
  @Override
  public List<CheckIn> findListByQuery(CheckInQuery query){
  	return this.checkInMapper.selectList(query);
  }

  @Override
	/**
	 * @Description: 根据条件查询数量
	 */
  public Integer findCountByQuery(CheckInQuery query){
  	return this.checkInMapper.selectCount(query);
  }

  @Override
	/**
	 * @Description: 根据条件更新
	 */
  public Integer updateByQuery(CheckIn bean, CheckInQuery query){
  	return this.checkInMapper.updateByQuery(bean, query);
  }

  @Override
	/**
	 * @Description: 根据条件删除
	 */
  public Integer deleteByQuery(CheckInQuery query){
  	return this.checkInMapper.deleteByQuery(query);
  }

  @Override
	/**
	 * @Description: 分页查询
	 */

  public PaginationResultVO<CheckIn> findListByPage(CheckInQuery query){  	Integer count = this.checkInMapper.selectCount(query);
  	int pageSize = query.getPageSize() == null? PageSize.SIZE15.getSize() : query.getPageSize();
  	SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
  	query.setSimplePage(page);
  	List<CheckIn> list = this.checkInMapper.selectList(query);
  	PaginationResultVO<CheckIn> result = new PaginationResultVO<CheckIn>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
  	return result;  }

  @Override
	/**
	 * @Description: 新增
	 */
  public Integer add(CheckIn bean){
  	return this.checkInMapper.insert(bean);  }

  @Override
	/**
	 * @Description: 批量新增
	 */
  public Integer addBatch(List<CheckIn> listBean){
  	if(listBean == null || listBean.isEmpty())
  		return 0;
  	return this.checkInMapper.insertBatch(listBean);
  }

  @Override
	/**
	 * @Description: 批量新增或更新
	 */
  public Integer addOrUpdateBatch(List<CheckIn> listBean){
  	if(listBean == null || listBean.isEmpty())
  		return 0;
  	return this.checkInMapper.insertOrUpdateBatch(listBean);
  }

  @Override
	/**
	 * @Description: 根据Id查询
	 */
  public CheckIn getById(Long id){
  	return this.checkInMapper.selectById(id);
  }

  @Override
	/**
	 * @Description: 根据Id更新
	 */
  public Integer updateById(CheckIn bean, Long id){
  	return this.checkInMapper.updateById(bean, id);
  }

  @Override
	/**
	 * @Description: 根据Id删除
	 */
  public Integer deleteById(Long id){
  	return this.checkInMapper.deleteById(id);
  }

}