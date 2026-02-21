package com.wnquxing.controller;

import com.wnquxing.entity.po.Book;
import com.wnquxing.entity.query.BookQuery;
import com.wnquxing.entity.vo.ResponseVO;
import com.wnquxing.exception.BusinessException;
import com.wnquxing.service.BookService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.wnquxing.service.impl.BookServiceImpl;
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
 * @Description:书籍信息表Controller
 * 
 * @Date:2026-01-12
 * 
 */
@RestController
@RequestMapping("/book")
public class BookController extends ABaseController{

	private final Logger logger = LoggerFactory.getLogger(BookController.class);

  @Resource
  private BookService bookService;

  @RequestMapping("loadDataList")
  public ResponseVO loadDataList(BookQuery query){
  	return getSuccessResponseVO(bookService.findListByPage(query));
  }
	/**
	 * @Description: 新增
	 */
  @RequestMapping("add")
  public ResponseVO add(Book bean){
  	this.bookService.add(bean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 批量新增
	 */
  @RequestMapping("addBatch")
  public ResponseVO addBatch(@RequestBody List<Book> listBean){
  	this.bookService.addBatch(listBean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 批量新增或更新
	 */
  @RequestMapping("addOrUpdateBatch")
  public ResponseVO addOrUpdateBatch(List<Book> listBean){
  	this.bookService.addOrUpdateBatch(listBean);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据Id查询
	 */
  @RequestMapping("getById")
  public ResponseVO getById(Long id){
  	return getSuccessResponseVO(this.bookService.getById(id));
  }

	/**
	 * @Description: 根据Id更新
	 */
  @RequestMapping("updateById")
  public ResponseVO updateById(Book bean, Long id){
  	this.bookService.updateById(bean, id);
  	return getSuccessResponseVO(null);
  }

	/**
	 * @Description: 根据Id删除
	 */
  @RequestMapping("deleteById")
  public ResponseVO deleteById(Long id){
  	this.bookService.deleteById(id);
  	return getSuccessResponseVO(null);
  }
	// ==================== 新增的书城功能 ====================

	/**
	 * 搜索书籍
	 */
	@RequestMapping("search")
	public ResponseVO search(@RequestParam String keyword,
							 @RequestParam(required = false, defaultValue = "1") Integer pageNo,
							 @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
		try {
			return getSuccessResponseVO(bookService.searchBooks(keyword, pageNo, pageSize));
		} catch (Exception e) {
			logger.error("搜索书籍失败，关键词: {}", keyword, e);
			if (e instanceof BusinessException) {
				return getBusinessErrorResponseVO((BusinessException) e, null);
			} else {
				return getServerErrorResponseVO(null);
			}
		}
	}

	/**
	 * 获取热门书籍
	 */
	@RequestMapping("getHotBooks")
	public ResponseVO getHotBooks(@RequestParam(required = false, defaultValue = "10") Integer limit) {
		try {
			return getSuccessResponseVO(bookService.getHotBooks(limit));
		} catch (Exception e) {
			logger.error("获取热门书籍失败", e);
			if (e instanceof BusinessException) {
				return getBusinessErrorResponseVO((BusinessException) e, null);
			} else {
				return getServerErrorResponseVO(null);
			}
		}
	}

	/**
	 * 获取最新书籍
	 */
	@RequestMapping("getNewBooks")
	public ResponseVO getNewBooks(@RequestParam(required = false, defaultValue = "10") Integer limit) {
		try {
			return getSuccessResponseVO(bookService.getNewBooks(limit));
		} catch (Exception e) {
			logger.error("获取最新书籍失败", e);
			if (e instanceof BusinessException) {
				return getBusinessErrorResponseVO((BusinessException) e, null);
			} else {
				return getServerErrorResponseVO(null);
			}
		}
	}


	/**
	 * 获取所有书籍类别
	 */
	@RequestMapping("getCategories")
	public ResponseVO getCategories() {
		try {
			return getSuccessResponseVO(bookService.getAllCategories());
		} catch (Exception e) {
			logger.error("获取书籍类别失败", e);
			if (e instanceof BusinessException) {
				return getBusinessErrorResponseVO((BusinessException) e, null);
			} else {
				return getServerErrorResponseVO(null);
			}
		}
	}

	/**
	 * 下载书籍文件
	 */
	@RequestMapping("download")
	public void download(@RequestParam Long bookId, HttpServletResponse response) {
		InputStream inputStream = null;
		try {
			// 获取文件输入流
			inputStream = bookService.downloadBookFile(bookId);

			// 设置响应头
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=\"book.pdf\"");

			// 写入文件流
			byte[] buffer = new byte[8192];
			int bytesRead;
			OutputStream outputStream = response.getOutputStream();

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.flush();
			logger.info("书籍下载完成，ID: {}", bookId);

		} catch (Exception e) {
			logger.error("下载书籍失败，ID: {}", bookId, e);
			try {
				if (e instanceof BusinessException) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
				} else {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "下载失败: " + e.getMessage());
				}
			} catch (Exception ex) {
				// 忽略异常
			}
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("关闭文件流失败", e);
				}
			}
		}
	}

	/**
	 * 检查书籍文件是否存在
	 */
	@RequestMapping("checkFileExists")
	public ResponseVO checkFileExists(@RequestParam Long bookId) {
		try {
			return getSuccessResponseVO(bookService.checkBookFileExists(bookId));
		} catch (Exception e) {
			logger.error("检查文件是否存在失败，ID: {}", bookId, e);
			if (e instanceof BusinessException) {
				return getBusinessErrorResponseVO((BusinessException) e, null);
			} else {
				return getServerErrorResponseVO(null);
			}
		}
	}

	/**
	 * 获取书籍文件信息
	 */
	@RequestMapping("getFileInfo")
	public ResponseVO getFileInfo(@RequestParam Long bookId) {
		try {
			return getSuccessResponseVO(bookService.getBookFileInfo(bookId));
		} catch (Exception e) {
			logger.error("获取文件信息失败，ID: {}", bookId, e);
			if (e instanceof BusinessException) {
				return getBusinessErrorResponseVO((BusinessException) e, null);
			} else {
				return getServerErrorResponseVO(null);
			}
		}
	}

	/**
	 * 获取可下载的书籍列表
	 */
	@RequestMapping("getDownloadableBooks")
	public ResponseVO getDownloadableBooks(@RequestParam(required = false, defaultValue = "20") Integer limit) {
		try {
			return getSuccessResponseVO(bookService.getDownloadableBooks(limit));
		} catch (Exception e) {
			logger.error("获取可下载书籍列表失败", e);
			if (e instanceof BusinessException) {
				return getBusinessErrorResponseVO((BusinessException) e, null);
			} else {
				return getServerErrorResponseVO(null);
			}
		}
	}

	/**
	 * 根据类别获取书籍
	 */
	@RequestMapping("getByCategory")
	public ResponseVO getByCategory(@RequestParam String category,
									@RequestParam(required = false, defaultValue = "1") Integer pageNo,
									@RequestParam(required = false, defaultValue = "10") Integer pageSize) {
		try {
			return getSuccessResponseVO(bookService.getBooksByCategory(category, pageNo, pageSize));
		} catch (Exception e) {
			logger.error("根据类别获取书籍失败，类别: {}", category, e);
			if (e instanceof BusinessException) {
				return getBusinessErrorResponseVO((BusinessException) e, null);
			} else {
				return getServerErrorResponseVO(null);
			}
		}
	}

	/**
	 * 获取书籍统计信息
	 */
	@RequestMapping("getStatistics")
	public ResponseVO getStatistics() {
		try {
			return getSuccessResponseVO(bookService.getBookStatistics());
		} catch (Exception e) {
			logger.error("获取统计信息失败", e);
			if (e instanceof BusinessException) {
				return getBusinessErrorResponseVO((BusinessException) e, null);
			} else {
				return getServerErrorResponseVO(null);
			}
		}
	}

	/**
	 * 预览书籍（在线查看PDF）
	 */
	@RequestMapping("preview")
	public void preview(@RequestParam Long bookId, HttpServletResponse response) {
		InputStream inputStream = null;
		try {
			// 获取书籍信息
			Book book = bookService.getById(bookId);
			if (book == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "书籍不存在");
				return;
			}

			// 获取文件输入流
			inputStream = (InputStream) bookService.previewBookFile(bookId);

			// 获取文件信息（用于Content-Length）
			Map<String, Object> fileInfo = bookService.getBookFileInfo(bookId);
			Long fileSize = (Long) fileInfo.get("fileSize");

			// 设置响应头为在线预览
			String fileName = book.getBookName() + ".pdf";
			fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");

			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
			response.setHeader("Content-Length", String.valueOf(fileSize));

			// 写入文件流
			byte[] buffer = new byte[8192];
			int bytesRead;
			OutputStream outputStream = response.getOutputStream();

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.flush();
			logger.info("书籍预览完成，ID: {}, 文件名: {}", bookId, fileName);

		} catch (Exception e) {
			logger.error("预览书籍失败，ID: {}", bookId, e);
			try {
				if (e instanceof BusinessException) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
				} else {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "预览失败: " + e.getMessage());
				}
			} catch (Exception ex) {
				// 忽略异常
			}
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("关闭文件流失败", e);
				}
			}
		}
	}
}


