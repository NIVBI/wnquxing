package com.greenbox.service;

import com.greenbox.entity.po.Book;
import com.greenbox.entity.query.BookQuery;
import com.greenbox.entity.vo.PaginationResultVO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.greenbox.utils.DateUtils;
import com.greenbox.entity.enums.DateTimePatternEnum;
import java.util.Date;
/**
 * 
 * @Auther:关山越
 * 
 * @Description:书籍信息表Service
 * 
 * @Date:2026-01-12
 * 
 */
public interface BookService{

	/**
	 * @Description: 根据条件查询列表
	 */
  List<Book> findListByQuery(BookQuery query);

	/**
	 * @Description: 根据条件查询数量
	 */
  Integer findCountByQuery(BookQuery query);

	/**
	 * @Description: 根据条件更新
	 */
  Integer updateByQuery(Book bean, BookQuery query);

	/**
	 * @Description: 根据条件删除
	 */
  Integer deleteByQuery(BookQuery query);

	/**
	 * @Description: 分页查询
	 */
  PaginationResultVO<Book> findListByPage(BookQuery query);

	/**
	 * @Description: 新增
	 */
  Integer add(Book bean);

	/**
	 * @Description: 批量新增
	 */
  Integer addBatch(List<Book> listBean);

	/**
	 * @Description: 批量新增或更新
	 */
  Integer addOrUpdateBatch(List<Book> listBean);

	/**
	 * @Description: 根据Id查询
	 */
  Book getById(Long id);

	/**
	 * @Description: 根据Id更新
	 */
  Integer updateById(Book bean, Long id);

	/**
	 * @Description: 根据Id删除
	 */
  Integer deleteById(Long id);

}