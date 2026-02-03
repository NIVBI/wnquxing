package com.wnquxing.service.impl;

import com.wnquxing.entity.enums.WordLevelEnum;
import com.wnquxing.mappers.WordMapper;
import com.wnquxing.entity.enums.PageSize;
import com.wnquxing.entity.po.Word;
import com.wnquxing.entity.query.SimplePage;
import com.wnquxing.entity.query.WordQuery;
import com.wnquxing.entity.vo.PaginationResultVO;
import com.wnquxing.service.WordService;

import java.util.*;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.wnquxing.entity.Constants.DEFAULT_PUSH_COUNT;

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

	/**
	 * 根据单词等级获取推送单词列表
	 */
	@Override
	public List<Word> getPushWordsByLevel(Integer wordLevel, Integer count) {
		// 验证参数
		validateWordLevel(wordLevel);
		if (count == null || count <= 0) {
			count = DEFAULT_PUSH_COUNT;
		}

		// 生成要查询的单词等级列表（0到wordLevel）
		List<Integer> levelList = generateLevelList(wordLevel);

		// 创建查询对象
		WordQuery query = new WordQuery();
		query.setWordLevelList(levelList);
		query.setLimitCount(count);
		query.setOrderBy("id ASC"); // 按ID顺序推送

		log.info("获取单词推送，等级范围: 0-{}, 数量: {}", wordLevel, count);

		return this.wordMapper.selectList(query);
	}

	/**
	 * 获取分级推送单词（分阶段推送）
	 */
	@Override
	public List<Word> getPushWordsByStage(Integer wordLevel, Integer stage, Integer count) {
		// 验证参数
		validateWordLevel(wordLevel);
		if (stage == null || stage < 0 || stage > wordLevel) {
			throw new IllegalArgumentException("阶段参数无效，必须在0到" + wordLevel + "之间");
		}
		if (count == null || count <= 0) {
			count = DEFAULT_PUSH_COUNT;
		}

		// 只查询当前阶段的单词等级
		List<Integer> levelList = Collections.singletonList(stage);

		// 创建查询对象
		WordQuery query = new WordQuery();
		query.setWordLevelList(levelList);
		query.setLimitCount(count);
		query.setOrderBy("id ASC"); // 按ID顺序推送

		log.info("获取分级推送单词，目标等级: {}, 当前阶段: {}, 数量: {}", wordLevel, stage, count);

		return this.wordMapper.selectList(query);
	}

	/**
	 * 获取单词统计信息
	 */
	@Override
	public Map<String, Object> getWordStatistics() {
		Map<String, Object> statistics = new HashMap<>();

		// 获取各等级单词数量
		for (WordLevelEnum level : WordLevelEnum.values()) {
			WordQuery query = new WordQuery();
			query.setWordLevel(level.getCode());
			Integer count = this.wordMapper.selectCount(query);
			statistics.put(level.getName() + "数量", count);

			// 添加等级详细信息
			Map<String, Object> levelInfo = new HashMap<>();
			levelInfo.put("code", level.getCode());
			levelInfo.put("name", level.getName());
			levelInfo.put("description", level.getDescription());
			levelInfo.put("count", count);
			statistics.put("level" + level.getCode(), levelInfo);
		}

		// 获取总单词数量
		WordQuery totalQuery = new WordQuery();
		Integer totalCount = this.wordMapper.selectCount(totalQuery);
		statistics.put("总单词数量", totalCount);

		log.debug("获取单词统计信息完成");
		return statistics;
	}

	/**
	 * 获取推荐单词等级
	 */
	@Override
	public Integer getRecommendedWordLevel(Integer educationLevel) {
		WordLevelEnum recommendedLevel = WordLevelEnum.recommendStartLevel(educationLevel);
		return recommendedLevel != null ? recommendedLevel.getCode() : 0;
	}

	/**
	 * 随机获取单词
	 */
	@Override
	public List<Word> getRandomWords(Integer wordLevel, Integer count) {
		if (count == null || count <= 0) {
			count = DEFAULT_PUSH_COUNT;
		}

		// 获取符合条件的单词总数
		WordQuery countQuery = new WordQuery();
		if (wordLevel != null) {
			countQuery.setWordLevel(wordLevel);
		}
		Integer totalCount = this.wordMapper.selectCount(countQuery);

		if (totalCount == null || totalCount == 0) {
			return Collections.emptyList();
		}

		// 如果请求数量大于总数，则返回全部
		if (count >= totalCount) {
			WordQuery query = new WordQuery();
			if (wordLevel != null) {
				query.setWordLevel(wordLevel);
			}
			query.setOrderBy("RAND()"); // 随机排序
			return this.wordMapper.selectList(query);
		}

		// 随机选择count个单词
		List<Word> result = new ArrayList<>();
		Set<Long> selectedIds = new HashSet<>();
		Random random = new Random();

		while (result.size() < count && selectedIds.size() < totalCount) {
			// 生成随机偏移量
			int offset = random.nextInt(totalCount);

			// 创建查询
			WordQuery query = new WordQuery();
			if (wordLevel != null) {
				query.setWordLevel(wordLevel);
			}
			query.setOrderBy("id ASC");
			query.setSimplePage(new SimplePage(1, totalCount, offset + 1));

			// 获取一页数据
			List<Word> pageWords = this.wordMapper.selectList(query);
			if (pageWords != null && !pageWords.isEmpty()) {
				for (Word word : pageWords) {
					if (!selectedIds.contains(word.getId()) && result.size() < count) {
						result.add(word);
						selectedIds.add(word.getId());
					}
				}
			}
		}

		// 随机打乱结果
		Collections.shuffle(result);

		log.info("随机获取单词，等级: {}, 数量: {}", wordLevel, result.size());
		return result;
	}

	/**
	 * 验证单词等级参数
	 */
	private void validateWordLevel(Integer wordLevel) {
		if (wordLevel == null) {
			throw new IllegalArgumentException("单词等级不能为空");
		}
		if (wordLevel < 0 || wordLevel > 4) {
			throw new IllegalArgumentException("单词等级必须在0到4之间");
		}
	}

	/**
	 * 生成单词等级列表（0到指定等级）
	 */
	private List<Integer> generateLevelList(Integer maxLevel) {
		List<Integer> levelList = new ArrayList<>();
		for (int i = 0; i <= maxLevel; i++) {
			levelList.add(i);
		}
		return levelList;
	}

	/**
	 * 辅助方法：获取指定等级的单词（按ID顺序）
	 */
	private List<Word> getWordsByLevel(Integer level, Integer count, Long lastWordId) {
		WordQuery query = new WordQuery();
		query.setWordLevel(level);
		query.setOrderBy("id ASC");
		query.setLimitCount(count);

		// 如果提供了最后单词ID，则从该ID之后开始
		if (lastWordId != null) {
			query.setId(lastWordId);
			// 这里假设id是递增的，实际使用时可能需要更复杂的逻辑
		}

		return this.wordMapper.selectList(query);
	}

	/**
	 * 获取下一个推送批次的单词
	 * 这个方法是辅助方法，用于实现连续推送
	 */
	public List<Word> getNextPushBatch(Integer wordLevel, Integer count, Map<Integer, Long> lastWordIds) {
		validateWordLevel(wordLevel);
		if (count == null || count <= 0) {
			count = DEFAULT_PUSH_COUNT;
		}

		List<Word> result = new ArrayList<>();
		List<Integer> levelList = generateLevelList(wordLevel);

		// 为每个等级分配大致相等的数量
		int perLevelCount = Math.max(1, count / levelList.size());

		for (Integer level : levelList) {
			if (result.size() >= count) {
				break;
			}

			Long lastWordId = lastWordIds != null ? lastWordIds.get(level) : null;
			List<Word> levelWords = getWordsByLevel(level, perLevelCount, lastWordId);

			if (levelWords != null && !levelWords.isEmpty()) {
				result.addAll(levelWords);
				// 更新最后单词ID
				if (lastWordIds != null) {
					Word lastWord = levelWords.get(levelWords.size() - 1);
					lastWordIds.put(level, lastWord.getId());
				}
			}

			// 如果已经达到数量要求，则退出
			if (result.size() >= count) {
				result = result.subList(0, count);
				break;
			}
		}

		log.info("获取下一个推送批次，等级: {}, 数量: {}, 实际返回: {}", wordLevel, count, result.size());
		return result;
	}
}
