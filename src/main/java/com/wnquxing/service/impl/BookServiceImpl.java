package com.wnquxing.service.impl;

import com.wnquxing.mappers.BookMapper;
import com.wnquxing.entity.enums.PageSize;
import com.wnquxing.entity.po.Book;
import com.wnquxing.entity.query.SimplePage;
import com.wnquxing.entity.query.BookQuery;
import com.wnquxing.entity.vo.PaginationResultVO;
import com.wnquxing.service.BookService;

import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:书籍信息表ServiceImpl
 * 
 * @Date:2026-01-12
 * 
 */
@Service("bookService")
public class BookServiceImpl implements BookService{

  private static Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

  @Resource
  private BookMapper<Book, BookQuery> bookMapper;

	/**
	 * @Description: 根据条件查询列表
	 */
  @Override
  public List<Book> findListByQuery(BookQuery query){
  	return this.bookMapper.selectList(query);
  }

  @Override
	/**
	 * @Description: 根据条件查询数量
	 */
  public Integer findCountByQuery(BookQuery query){
  	return this.bookMapper.selectCount(query);
  }

  @Override
	/**
	 * @Description: 根据条件更新
	 */
  public Integer updateByQuery(Book bean, BookQuery query){
  	return this.bookMapper.updateByQuery(bean, query);
  }

  @Override
	/**
	 * @Description: 根据条件删除
	 */
  public Integer deleteByQuery(BookQuery query){
  	return this.bookMapper.deleteByQuery(query);
  }

  @Override
	/**
	 * @Description: 分页查询
	 */

  public PaginationResultVO<Book> findListByPage(BookQuery query){  	Integer count = this.bookMapper.selectCount(query);
  	int pageSize = query.getPageSize() == null? PageSize.SIZE15.getSize() : query.getPageSize();
  	SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
  	query.setSimplePage(page);
  	List<Book> list = this.bookMapper.selectList(query);
  	PaginationResultVO<Book> result = new PaginationResultVO<Book>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
  	return result;  }

  @Override
	/**
	 * @Description: 新增
	 */
  public Integer add(Book bean){
  	return this.bookMapper.insert(bean);  }

  @Override
	/**
	 * @Description: 批量新增
	 */
  public Integer addBatch(List<Book> listBean){
  	if(listBean == null || listBean.isEmpty())
  		return 0;
  	return this.bookMapper.insertBatch(listBean);
  }

  @Override
	/**
	 * @Description: 批量新增或更新
	 */
  public Integer addOrUpdateBatch(List<Book> listBean){
  	if(listBean == null || listBean.isEmpty())
  		return 0;
  	return this.bookMapper.insertOrUpdateBatch(listBean);
  }

  @Override
	/**
	 * @Description: 根据Id查询
	 */
  public Book getById(Long id){
  	return this.bookMapper.selectById(id);
  }

  @Override
	/**
	 * @Description: 根据Id更新
	 */
  public Integer updateById(Book bean, Long id){
  	return this.bookMapper.updateById(bean, id);
  }

  @Override
	/**
	 * @Description: 根据Id删除
	 */
  public Integer deleteById(Long id){
  	return this.bookMapper.deleteById(id);
  }

}