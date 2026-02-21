package com.wnquxing.service;

import com.wnquxing.entity.po.Book;
import com.wnquxing.entity.query.BookQuery;
import com.wnquxing.entity.vo.PaginationResultVO;
import com.wnquxing.exception.BusinessException;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

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

	// ==================== 新增的书城功能 ====================

	/**
	 * @Description: 搜索书籍（按书名或作者模糊搜索）
	 * @param keyword 搜索关键词
	 * @param pageNo 页码
	 * @param pageSize 每页大小
	 * @return 分页结果
	 */
	PaginationResultVO<Book> searchBooks(String keyword, Integer pageNo, Integer pageSize);

	/**
	 * @Description: 获取热门书籍
	 * @param limit 返回数量限制
	 * @return 热门书籍列表
	 */
	List<Book> getHotBooks(Integer limit);

	/**
	 * @Description: 获取最新书籍
	 * @param limit 返回数量限制
	 * @return 最新书籍列表
	 */
	List<Book> getNewBooks(Integer limit);

	/**
	 * @Description: 获取所有书籍类别
	 * @return 书籍类别列表
	 */
	List<String> getAllCategories();

	/**
	 * @Description: 下载书籍文件
	 * @param bookId 书籍ID
	 * @return 文件输入流
	 */
	InputStream downloadBookFile(Long bookId);

	/**
	 * @Description: 检查书籍文件是否存在
	 * @param bookId 书籍ID
	 * @return 文件是否存在
	 */
	boolean checkBookFileExists(Long bookId) throws BusinessException;

	/**
	 * @Description: 获取书籍文件信息
	 * @param bookId 书籍ID
	 * @return 文件信息（大小、修改时间等）
	 */
	Map<String, Object> getBookFileInfo(Long bookId) throws BusinessException;

	/**
	 * @Description: 获取可下载的书籍列表
	 * @param limit 返回数量限制
	 * @return 可下载的书籍列表
	 */
	List<Book> getDownloadableBooks(Integer limit) throws BusinessException;

	/**
	 * @Description: 根据类别获取书籍
	 * @param category 书籍类别
	 * @param pageNo 页码
	 * @param pageSize 每页大小
	 * @return 分页结果
	 */
	PaginationResultVO<Book> getBooksByCategory(String category, Integer pageNo, Integer pageSize) throws BusinessException;

	/**
	 * @Description: 获取书籍统计信息
	 * @return 统计信息
	 */
	Map<String, Object> getBookStatistics() throws BusinessException;

	/**
	 * @Description: 预览书籍文件
	 * @param bookId 书籍ID
	 * @return 文件输入流
	 */
	InputStream previewBookFile(Long bookId) throws BusinessException;

}