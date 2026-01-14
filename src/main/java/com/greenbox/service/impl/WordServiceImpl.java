package com.greenbox.service.impl;

import com.greenbox.mappers.WordMapper;
import com.greenbox.entity.enums.PageSize;
import com.greenbox.entity.po.Word;
import com.greenbox.entity.query.SimplePage;
import com.greenbox.entity.query.WordQuery;
import com.greenbox.entity.vo.PaginationResultVO;
import com.greenbox.service.WordService;

import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 
 * @Auther:关山越
 * 
 * @Description:单词信息表ServiceImpl
 * 
 * @Date:2026-01-12
 * 
 */
@Service("wordService")
public class WordServiceImpl implements WordService{

  private static Logger log = LoggerFactory.getLogger(WordServiceImpl.class);

  @Resource
  private WordMapper<Word, WordQuery> wordMapper;

	/**
	 * @Description: 根据条件查询列表
	 */
  @Override
  public List<Word> findListByQuery(WordQuery query){
  	return this.wordMapper.selectList(query);
  }

  @Override
	/**
	 * @Description: 根据条件查询数量
	 */
  public Integer findCountByQuery(WordQuery query){
  	return this.wordMapper.selectCount(query);
  }

  @Override
	/**
	 * @Description: 根据条件更新
	 */
  public Integer updateByQuery(Word bean, WordQuery query){
  	return this.wordMapper.updateByQuery(bean, query);
  }

  @Override
	/**
	 * @Description: 根据条件删除
	 */
  public Integer deleteByQuery(WordQuery query){
  	return this.wordMapper.deleteByQuery(query);
  }

  @Override
	/**
	 * @Description: 分页查询
	 */

  public PaginationResultVO<Word> findListByPage(WordQuery query){  	Integer count = this.wordMapper.selectCount(query);
  	int pageSize = query.getPageSize() == null? PageSize.SIZE15.getSize() : query.getPageSize();
  	SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
  	query.setSimplePage(page);
  	List<Word> list = this.wordMapper.selectList(query);
  	PaginationResultVO<Word> result = new PaginationResultVO<Word>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
  	return result;  }

  @Override
	/**
	 * @Description: 新增
	 */
  public Integer add(Word bean){
  	return this.wordMapper.insert(bean);  }

  @Override
	/**
	 * @Description: 批量新增
	 */
  public Integer addBatch(List<Word> listBean){
  	if(listBean == null || listBean.isEmpty())
  		return 0;
  	return this.wordMapper.insertBatch(listBean);
  }

  @Override
	/**
	 * @Description: 批量新增或更新
	 */
  public Integer addOrUpdateBatch(List<Word> listBean){
  	if(listBean == null || listBean.isEmpty())
  		return 0;
  	return this.wordMapper.insertOrUpdateBatch(listBean);
  }

  @Override
	/**
	 * @Description: 根据Id查询
	 */
  public Word getById(Long id){
  	return this.wordMapper.selectById(id);
  }

  @Override
	/**
	 * @Description: 根据Id更新
	 */
  public Integer updateById(Word bean, Long id){
  	return this.wordMapper.updateById(bean, id);
  }

  @Override
	/**
	 * @Description: 根据Id删除
	 */
  public Integer deleteById(Long id){
  	return this.wordMapper.deleteById(id);
  }

  @Override
	/**
	 * @Description: 根据English查询
	 */
  public Word getByEnglish(String english){
  	return this.wordMapper.selectByEnglish(english);
  }

  @Override
	/**
	 * @Description: 根据English更新
	 */
  public Integer updateByEnglish(Word bean, String english){
  	return this.wordMapper.updateByEnglish(bean, english);
  }

  @Override
	/**
	 * @Description: 根据English删除
	 */
  public Integer deleteByEnglish(String english){
  	return this.wordMapper.deleteByEnglish(english);
  }

}