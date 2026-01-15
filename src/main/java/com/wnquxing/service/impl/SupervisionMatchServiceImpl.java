package com.wnquxing.service.impl;

import com.wnquxing.mappers.SupervisionMatchMapper;
import com.wnquxing.entity.enums.PageSize;
import com.wnquxing.entity.po.SupervisionMatch;
import com.wnquxing.entity.query.SimplePage;
import com.wnquxing.entity.query.SupervisionMatchQuery;
import com.wnquxing.entity.vo.PaginationResultVO;
import com.wnquxing.service.SupervisionMatchService;

import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:监督匹配房间信息表ServiceImpl
 * 
 * @Date:2026-01-12
 * 
 */
@Service("supervisionMatchService")
public class SupervisionMatchServiceImpl implements SupervisionMatchService{

  private static Logger log = LoggerFactory.getLogger(SupervisionMatchServiceImpl.class);

  @Resource
  private SupervisionMatchMapper<SupervisionMatch, SupervisionMatchQuery> supervisionMatchMapper;

	/**
	 * @Description: 根据条件查询列表
	 */
  @Override
  public List<SupervisionMatch> findListByQuery(SupervisionMatchQuery query){
  	return this.supervisionMatchMapper.selectList(query);
  }

  @Override
	/**
	 * @Description: 根据条件查询数量
	 */
  public Integer findCountByQuery(SupervisionMatchQuery query){
  	return this.supervisionMatchMapper.selectCount(query);
  }

  @Override
	/**
	 * @Description: 根据条件更新
	 */
  public Integer updateByQuery(SupervisionMatch bean, SupervisionMatchQuery query){
  	return this.supervisionMatchMapper.updateByQuery(bean, query);
  }

  @Override
	/**
	 * @Description: 根据条件删除
	 */
  public Integer deleteByQuery(SupervisionMatchQuery query){
  	return this.supervisionMatchMapper.deleteByQuery(query);
  }

  @Override
	/**
	 * @Description: 分页查询
	 */

  public PaginationResultVO<SupervisionMatch> findListByPage(SupervisionMatchQuery query){  	Integer count = this.supervisionMatchMapper.selectCount(query);
  	int pageSize = query.getPageSize() == null? PageSize.SIZE15.getSize() : query.getPageSize();
  	SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
  	query.setSimplePage(page);
  	List<SupervisionMatch> list = this.supervisionMatchMapper.selectList(query);
  	PaginationResultVO<SupervisionMatch> result = new PaginationResultVO<SupervisionMatch>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
  	return result;  }

  @Override
	/**
	 * @Description: 新增
	 */
  public Integer add(SupervisionMatch bean){
  	return this.supervisionMatchMapper.insert(bean);  }

  @Override
	/**
	 * @Description: 批量新增
	 */
  public Integer addBatch(List<SupervisionMatch> listBean){
  	if(listBean == null || listBean.isEmpty())
  		return 0;
  	return this.supervisionMatchMapper.insertBatch(listBean);
  }

  @Override
	/**
	 * @Description: 批量新增或更新
	 */
  public Integer addOrUpdateBatch(List<SupervisionMatch> listBean){
  	if(listBean == null || listBean.isEmpty())
  		return 0;
  	return this.supervisionMatchMapper.insertOrUpdateBatch(listBean);
  }

  @Override
	/**
	 * @Description: 根据Id查询
	 */
  public SupervisionMatch getById(Long id){
  	return this.supervisionMatchMapper.selectById(id);
  }

  @Override
	/**
	 * @Description: 根据Id更新
	 */
  public Integer updateById(SupervisionMatch bean, Long id){
  	return this.supervisionMatchMapper.updateById(bean, id);
  }

  @Override
	/**
	 * @Description: 根据Id删除
	 */
  public Integer deleteById(Long id){
  	return this.supervisionMatchMapper.deleteById(id);
  }

  @Override
	/**
	 * @Description: 根据RoomName查询
	 */
  public SupervisionMatch getByRoomName(String roomName){
  	return this.supervisionMatchMapper.selectByRoomName(roomName);
  }

  @Override
	/**
	 * @Description: 根据RoomName更新
	 */
  public Integer updateByRoomName(SupervisionMatch bean, String roomName){
  	return this.supervisionMatchMapper.updateByRoomName(bean, roomName);
  }

  @Override
	/**
	 * @Description: 根据RoomName删除
	 */
  public Integer deleteByRoomName(String roomName){
  	return this.supervisionMatchMapper.deleteByRoomName(roomName);
  }

}