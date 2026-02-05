package com.wnquxing.service.impl;

import com.wnquxing.config.BookConfig;
import com.wnquxing.exception.BusinessException;
import com.wnquxing.mappers.BookMapper;
import com.wnquxing.entity.enums.PageSize;
import com.wnquxing.entity.po.Book;
import com.wnquxing.entity.query.SimplePage;
import com.wnquxing.entity.query.BookQuery;
import com.wnquxing.entity.vo.PaginationResultVO;
import com.wnquxing.service.BookService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import io.lettuce.core.dynamic.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

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

  private static Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);


  @Resource
  private BookMapper<Book, BookQuery> bookMapper;

	@Resource
	private BookConfig bookConfig;
	// 存储配置的变量
	private String storagePath;
	private long maxFileSize;
	private Set<String> allowedExtensionSet;
	@PostConstruct
	public void init() {
		try {
			// 从配置类获取配置
			storagePath = bookConfig.getStoragePath();
			maxFileSize = bookConfig.getMaxFileSize();
			String allowedExtensions = bookConfig.getAllowedExtensions();

			// 初始化允许的文件扩展名集合
			allowedExtensionSet = new HashSet<>(Arrays.asList(allowedExtensions.split(",")));

			// 创建存储目录
			Path path = Paths.get(storagePath);
			if (!Files.exists(path)) {
				Files.createDirectories(path);
				logger.info("创建书籍文件存储目录: {}", storagePath);
			}

			// 转换字节为MB显示
			long maxFileSizeMB = maxFileSize / (1024 * 1024);
			logger.info("书籍文件服务初始化完成，存储路径: {}, 最大文件大小: {}MB, 允许扩展名: {}",
					storagePath, maxFileSizeMB, allowedExtensions);
		} catch (IOException e) {
			logger.error("初始化书籍文件存储目录失败", e);
			throw new RuntimeException("书籍文件存储目录初始化失败", e);
		}
	}



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


	// ==================== 新增的书城功能 ====================

	/**
	 * 搜索书籍（按书名或作者模糊搜索）
	 */
	@Override
	public PaginationResultVO<Book> searchBooks(String keyword, Integer pageNo, Integer pageSize) {
		if (keyword == null || keyword.trim().isEmpty()) {
			// 如果关键词为空，返回所有书籍
			BookQuery query = new BookQuery();
			query.setPageNo(pageNo != null ? pageNo : 1);
			query.setPageSize(pageSize != null ? pageSize : DEFAULT_PAGE_SIZE);
			return findListByPage(query);
		}

		// 创建查询对象
		BookQuery query = new BookQuery();

		// 设置书名和作者的模糊搜索条件
		query.setBookNameFuzzy(keyword);
		query.setAuthorFuzzy(keyword);

		// 设置分页
		query.setPageNo(pageNo != null ? pageNo : 1);
		query.setPageSize(pageSize != null ? pageSize : DEFAULT_PAGE_SIZE);

		// 设置排序（按创建时间倒序）
		query.setOrderBy("create_time DESC");

		logger.info("搜索书籍，关键词: {}, 页码: {}, 页大小: {}", keyword, pageNo, pageSize);
		return findListByPage(query);
	}

	/**
	 * 获取热门书籍（暂时按创建时间排序）
	 */
	@Override
	public List<Book> getHotBooks(Integer limit) {
		BookQuery query = new BookQuery();
		query.setOrderBy("create_time DESC"); // 暂时按创建时间排序
		query.setPageSize(limit != null ? limit : 10);
		query.setPageNo(1);

		List<Book> result = findListByQuery(query);
		logger.debug("获取热门书籍，数量: {}", result.size());
		return result;
	}

	/**
	 * 获取最新书籍
	 */
	@Override
	public List<Book> getNewBooks(Integer limit) {
		BookQuery query = new BookQuery();
		query.setOrderBy("create_time DESC");
		query.setPageSize(limit != null ? limit : 10);
		query.setPageNo(1);

		List<Book> result = findListByQuery(query);
		logger.debug("获取最新书籍，数量: {}", result.size());
		return result;
	}

	/**
	 * 获取所有书籍类别
	 */
	@Override
	public List<String> getAllCategories() {
		// 获取所有书籍然后提取类别
		BookQuery query = new BookQuery();
		List<Book> allBooks = findListByQuery(query);

		// 使用Set去重
		Set<String> categories = new HashSet<>();
		for (Book book : allBooks) {
			if (book.getCategory() != null && !book.getCategory().trim().isEmpty()) {
				categories.add(book.getCategory().trim());
			}
		}

		// 转换为List并排序
		List<String> result = new ArrayList<>(categories);
		Collections.sort(result);

		logger.debug("获取书籍类别，数量: {}", result.size());
		return result;
	}

	/**
	 * 下载书籍文件
	 */
	@Override
	public Map<String, Object> downloadBookFile(Long bookId) {
		try {
			// 获取书籍信息
			Book book = getById(bookId);
			if (book == null) {
				throw new BusinessException("书籍不存在，ID: " + bookId);
			}

			// 检查文件是否存在
			if (!bookFileExists(bookId, book.getBookName())) {
				throw new BusinessException("书籍文件不存在，请确认文件已上传");
			}

			// 获取文件输入流
			InputStream fileInputStream = getBookFile(bookId, book.getBookName());

			// 获取文件信息
			FileInfo fileInfo = getBookFileInfo(bookId, book.getBookName());

			// 准备返回结果
			Map<String, Object> result = new HashMap<>();
			result.put("inputStream", fileInputStream);
			result.put("fileName", book.getBookName() + ".pdf");
			result.put("fileSize", fileInfo.size);
			result.put("lastModified", fileInfo.lastModified);
			result.put("book", book);

			logger.info("准备下载书籍，ID: {}, 书名: {}, 文件大小: {}字节", bookId, book.getBookName(), fileInfo.size);
			return result;

		} catch (BusinessException e) {
			// 重新抛出业务异常
            try {
                throw e;
            } catch (BusinessException ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception e) {
			logger.error("下载书籍文件失败，ID: {}", bookId, e);
            try {
                throw new BusinessException("下载失败: " + e.getMessage());
            } catch (BusinessException ex) {
                throw new RuntimeException(ex);
            }
        }
	}

	/**
	 * 预览书籍文件（与下载类似，但前端处理方式不同）
	 */
	@Override
	public Map<String, Object> previewBookFile(Long bookId) throws BusinessException {
		// 预览和下载使用相同的文件获取逻辑
		return downloadBookFile(bookId);
	}

	/**
	 * 检查书籍文件是否存在
	 */
	@Override
	public boolean checkBookFileExists(Long bookId) throws BusinessException {
		try {
			Book book = getById(bookId);
			if (book == null) {
				throw new BusinessException("书籍不存在，ID: " + bookId);
			}
			return bookFileExists(bookId, book.getBookName());
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			logger.error("检查书籍文件是否存在失败，ID: {}", bookId, e);
			throw new BusinessException("检查文件失败: " + e.getMessage());
		}
	}

	/**
	 * 获取书籍文件信息
	 */
	@Override
	public Map<String, Object> getBookFileInfo(Long bookId) throws BusinessException {
		try {
			Book book = getById(bookId);
			if (book == null) {
				throw new BusinessException("书籍不存在，ID: " + bookId);
			}

			Map<String, Object> fileInfo = new HashMap<>();
			fileInfo.put("bookId", bookId);
			fileInfo.put("bookName", book.getBookName());
			fileInfo.put("exists", bookFileExists(bookId, book.getBookName()));

			if (bookFileExists(bookId, book.getBookName())) {
				FileInfo info = getBookFileInfo(bookId, book.getBookName());
				fileInfo.put("fileSize", info.size);
				fileInfo.put("fileSizeFormatted", formatFileSize(info.size));
				fileInfo.put("lastModified", info.lastModified);
				fileInfo.put("downloadable", true);
			} else {
				fileInfo.put("downloadable", false);
				fileInfo.put("message", "书籍文件未找到");
			}

			return fileInfo;

		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			logger.error("获取书籍文件信息失败，ID: {}", bookId, e);
			throw new BusinessException("获取文件信息失败: " + e.getMessage());
		}
	}

	/**
	 * 获取可下载的书籍列表
	 */
	@Override
	public List<Book> getDownloadableBooks(Integer limit) throws BusinessException {
		try {
			BookQuery query = new BookQuery();
			List<Book> allBooks = findListByQuery(query);

			// 过滤出有文件的书籍
			List<Book> downloadableBooks = new ArrayList<>();
			for (Book book : allBooks) {
				if (bookFileExists(book.getId(), book.getBookName())) {
					downloadableBooks.add(book);
					if (limit != null && downloadableBooks.size() >= limit) {
						break;
					}
				}
			}

			return downloadableBooks;
		} catch (Exception e) {
			logger.error("获取可下载书籍列表失败", e);
			throw new BusinessException("获取可下载书籍失败: " + e.getMessage());
		}
	}

	/**
	 * 根据类别获取书籍
	 */
	@Override
	public PaginationResultVO<Book> getBooksByCategory(String category, Integer pageNo, Integer pageSize) throws BusinessException {
		try {
			BookQuery query = new BookQuery();
			query.setCategory(category);
			query.setPageNo(pageNo != null ? pageNo : 1);
			query.setPageSize(pageSize != null ? pageSize : DEFAULT_PAGE_SIZE);
			query.setOrderBy("create_time DESC");

			return findListByPage(query);
		} catch (Exception e) {
			logger.error("根据类别获取书籍失败，类别: {}", category, e);
			throw new BusinessException("获取类别书籍失败: " + e.getMessage());
		}
	}

	/**
	 * 获取书籍统计信息
	 */
	@Override
	public Map<String, Object> getBookStatistics() throws BusinessException {
		try {
			Map<String, Object> statistics = new HashMap<>();

			// 获取书籍总数
			BookQuery totalQuery = new BookQuery();
			Integer totalBooks = findCountByQuery(totalQuery);
			statistics.put("totalBooks", totalBooks);

			// 获取可下载的书籍数量
			List<Book> allBooks = findListByQuery(totalQuery);
			int downloadableCount = 0;
			for (Book book : allBooks) {
				if (bookFileExists(book.getId(), book.getBookName())) {
					downloadableCount++;
				}
			}
			statistics.put("downloadableBooks", downloadableCount);

			// 获取类别统计
			List<String> categories = getAllCategories();
			statistics.put("totalCategories", categories.size());
			statistics.put("categories", categories);

			// 获取存储目录信息
			statistics.put("storagePath", storagePath);
			statistics.put("maxFileSize", maxFileSize);
			statistics.put("maxFileSizeFormatted", formatFileSize(maxFileSize));

			// 获取实际存储的文件列表
			File[] storedFiles = getAllBookFiles();
			statistics.put("storedFilesCount", storedFiles != null ? storedFiles.length : 0);

			logger.debug("获取书籍统计信息完成");
			return statistics;

		} catch (Exception e) {
			logger.error("获取书籍统计信息失败", e);
			throw new BusinessException("获取统计信息失败: " + e.getMessage());
		}
	}

	// ==================== 文件处理内部方法 ====================

	/**
	 * 根据书籍信息获取文件路径
	 */
	private String getBookFilePath(Long bookId, String bookName) throws BusinessException {
		if (bookId == null || bookName == null) {
			throw new BusinessException("书籍ID和名称不能为空");
		}

		// 清理文件名中的非法字符
		String safeBookName = bookName.replaceAll("[\\\\/:*?\"<>|]", "_");
		String fileName = bookId + "_" + safeBookName + ".pdf";

		return Paths.get(storagePath, fileName).toString();
	}

	/**
	 * 获取书籍文件
	 */
	private InputStream getBookFile(Long bookId, String bookName) throws BusinessException {
		try {
			String filePath = getBookFilePath(bookId, bookName);
			File file = new File(filePath);

			if (!file.exists()) {
				throw new BusinessException("书籍文件不存在，书籍ID: " + bookId);
			}

			if (!file.isFile()) {
				throw new BusinessException("路径不是有效的文件，书籍ID: " + bookId);
			}

			return new FileInputStream(file);

		} catch (BusinessException e) {
			throw e;
		} catch (IOException e) {
			logger.error("读取书籍文件失败，书籍ID: {}", bookId, e);
			throw new BusinessException("读取书籍文件失败: " + e.getMessage());
		}
	}

	/**
	 * 检查文件是否存在
	 */
	private boolean bookFileExists(Long bookId, String bookName) throws BusinessException {
		String filePath = getBookFilePath(bookId, bookName);
		File file = new File(filePath);
		return file.exists() && file.isFile();
	}

	/**
	 * 文件信息内部类
	 */
	private static class FileInfo {
		long size;
		Long lastModified;
	}

	/**
	 * 获取文件信息
	 */
	private FileInfo getBookFileInfo(Long bookId, String bookName) throws BusinessException {
		FileInfo info = new FileInfo();
		String filePath = getBookFilePath(bookId, bookName);
		File file = new File(filePath);

		if (file.exists() && file.isFile()) {
			info.size = file.length();
			info.lastModified = file.lastModified();
		} else {
			info.size = -1;
			info.lastModified = null;
		}

		return info;
	}

	/**
	 * 获取目录下所有PDF文件
	 */
	private File[] getAllBookFiles() {
		File storageDir = new File(storagePath);
		if (!storageDir.exists() || !storageDir.isDirectory()) {
			return new File[0];
		}

		return storageDir.listFiles((dir, name) ->
				name.toLowerCase().endsWith(".pdf")
		);
	}

	/**
	 * 格式化文件大小
	 */
	private String formatFileSize(long size) {
		if (size < 1024) {
			return size + " B";
		} else if (size < 1024 * 1024) {
			return String.format("%.1f KB", size / 1024.0);
		} else if (size < 1024 * 1024 * 1024) {
			return String.format("%.1f MB", size / (1024.0 * 1024));
		} else {
			return String.format("%.1f GB", size / (1024.0 * 1024 * 1024));
		}
	}

	/**
	 * 验证文件扩展名
	 */
	private boolean isValidFileExtension(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			return false;
		}

		int lastDotIndex = fileName.lastIndexOf('.');
		if (lastDotIndex <= 0 || lastDotIndex >= fileName.length() - 1) {
			return false;
		}

		String extension = fileName.substring(lastDotIndex + 1);
		return allowedExtensionSet.contains(extension);
	}

}