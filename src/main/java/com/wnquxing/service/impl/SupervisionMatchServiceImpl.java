package com.wnquxing.service.impl;

import com.wnquxing.mappers.SupervisionMatchMapper;
import com.wnquxing.entity.enums.PageSize;
import com.wnquxing.entity.po.SupervisionMatch;
import com.wnquxing.entity.query.SimplePage;
import com.wnquxing.entity.query.SupervisionMatchQuery;
import com.wnquxing.entity.vo.PaginationResultVO;
import com.wnquxing.redis.RedisUtils;
import com.wnquxing.service.SupervisionMatchService;
import com.wnquxing.exception.BusinessException;
import com.wnquxing.redis.RedisComponent;
import com.wnquxing.entity.Constants;
import com.wnquxing.websocket.MessageHandler;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.UUID;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Resource;
import javax.annotation.PostConstruct;

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
public class SupervisionMatchServiceImpl implements SupervisionMatchService{@Resource



	// 并发量控制 - 最大同时匹配用户数
	private static final int MAX_CONCURRENT_MATCH = 10;
	private final AtomicInteger currentMatchCount = new AtomicInteger(0);


	private static Logger log = LoggerFactory.getLogger(SupervisionMatchServiceImpl.class);

  @Resource
  private SupervisionMatchMapper<SupervisionMatch, SupervisionMatchQuery> supervisionMatchMapper;@Resource

	private RedisComponent redisComponent;

	@Resource
	private RedisUtils redisUtils;

	@Resource
	private MessageHandler messageHandler;

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
// ==================== 新增的随机匹配功能 ====================

	/**
	 * 随机匹配房间
	 */
	@Override
	public SupervisionMatch randomMatch(Long userId) {
		if (userId == null) {
            try {
                throw new BusinessException("用户ID不能为空");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }

		// 检查并发量
		if (currentMatchCount.get() >= MAX_CONCURRENT_MATCH) {
			log.warn("当前匹配人数已达上限，用户 {} 匹配失败", userId);
            try {
                throw new BusinessException("系统繁忙，请稍后再试");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }

		log.info("用户 {} 开始随机匹配", userId);

		// 检查用户是否已在匹配中或已在房间中
		Map<String, String> userMatchInfo = redisComponent.getUserMatchInfo(userId);
		if (userMatchInfo != null) {
			Integer status = Integer.parseInt(userMatchInfo.get("status"));
			if (status == Constants.MATCH_STATUS_WAITING) {
                try {
                    throw new BusinessException("您已在匹配队列中，请勿重复匹配");
                } catch (BusinessException e) {
                    throw new RuntimeException(e);
                }
            } else if (status == Constants.MATCH_STATUS_SUCCESS) {
				// 用户已在房间中，返回房间信息
				String roomName = userMatchInfo.get("roomName");
				return getByRoomName(roomName);
			}
		}

		// 增加并发计数
		currentMatchCount.incrementAndGet();

		try {
			// 先清理超时的用户
			cleanupTimeoutUsers();

			// 检查等待队列
			Long waitingUserId = redisComponent.getRandomFromWaitingQueue();

			if (waitingUserId != null && !waitingUserId.equals(userId)) {
				// 检查被匹配的用户是否超时
				if (redisComponent.isUserTimeout(waitingUserId)) {
					// 如果超时，移除该用户并重新匹配
					redisComponent.removeFromWaitingQueue(waitingUserId);
					redisComponent.deleteUserMatchInfo(waitingUserId);
					redisComponent.removeUserTimeout(waitingUserId);
					return randomMatch(userId); // 递归重新匹配
				}

				// 从队列中移除匹配到的用户
				redisComponent.removeFromWaitingQueue(waitingUserId);
				// 移除超时标记
				redisComponent.removeUserTimeout(waitingUserId);

				// 创建新房间
				String roomName = generateRoomName();

				// 创建房间记录
				SupervisionMatch room = new SupervisionMatch();
				room.setRoomName(roomName);
				room.setSupervisionDuration(0);
				room.setCreateTime(new Date());

				// 保存到数据库
				add(room);

				// 在Redis中创建房间
				redisComponent.createRoom(roomName, userId, waitingUserId);

				// 保存用户匹配信息
				redisComponent.saveUserMatchInfo(userId, Constants.MATCH_STATUS_SUCCESS, roomName, waitingUserId);
				redisComponent.saveUserMatchInfo(waitingUserId, Constants.MATCH_STATUS_SUCCESS, roomName, userId);

				log.info("匹配成功：用户 {} 和 {} 进入房间 {}", userId, waitingUserId, roomName);

				// 发送WebSocket通知
				messageHandler.sendMatchSuccessMessage(String.valueOf(userId), roomName, waitingUserId);
				messageHandler.sendMatchSuccessMessage(String.valueOf(waitingUserId), roomName, userId);

				return room;
			} else {
				// 没有等待的用户，加入等待队列（带超时）
				redisComponent.addToWaitingQueueWithTimeout(userId);

				// 保存匹配状态
				redisComponent.saveUserMatchInfo(userId, Constants.MATCH_STATUS_WAITING, null, null);

				log.info("用户 {} 加入等待队列，当前等待人数: {}", userId, redisComponent.getWaitingQueueSize());

				return null; // 返回null表示正在等待
			}
		} finally {
			// 减少并发计数
			currentMatchCount.decrementAndGet();
		}
	}

	/**
	 * 清理超时的等待用户
	 */
	private void cleanupTimeoutUsers() {
		try {
			List<Long> timeoutUsers = redisComponent.getTimeoutUsers();
			if (!timeoutUsers.isEmpty()) {
				log.info("清理超时等待用户: {}", timeoutUsers);

				// 对每个超时用户发送通知
				for (Long userId : timeoutUsers) {
					messageHandler.sendTimeoutMessage(String.valueOf(userId));
				}

				// 从Redis中移除
				redisComponent.removeTimeoutUsers(timeoutUsers);
			}
		} catch (Exception e) {
			log.error("清理超时用户失败", e);
		}
	}

	/**
	 * 更新监督时长
	 */
	@Override
	public Integer updateSupervisionDuration(String roomName, Integer duration) {
		if (roomName == null || duration == null) {
            try {
                throw new BusinessException("房间名和监督时长不能为空");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }

		log.info("更新房间 {} 的监督时长: {}分钟", roomName, duration);

		// 获取房间信息
		SupervisionMatch room = getByRoomName(roomName);
		if (room == null) {
            try {
                throw new BusinessException("房间不存在");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }

		// 更新监督时长
		room.setSupervisionDuration(duration);

		// 更新Redis中的房间信息
		redisComponent.updateRoomDuration(roomName, duration);

		return updateByRoomName(room, roomName);
	}

	/**
	 * 取消匹配
	 */
	@Override
	public Integer cancelMatch(Long userId) {
		log.info("用户 {} 取消匹配", userId);

		Map<String, String> userMatchInfo = redisComponent.getUserMatchInfo(userId);
		if (userMatchInfo != null) {
			Integer status = Integer.parseInt(userMatchInfo.get("status"));
			if (status == Constants.MATCH_STATUS_WAITING) {
				// 从等待队列中移除
				redisComponent.removeFromWaitingQueue(userId);

				// 删除匹配信息
				redisComponent.deleteUserMatchInfo(userId);

				// 移除超时标记
				redisComponent.removeUserTimeout(userId);

				log.info("用户 {} 已取消匹配", userId);
				return 1;
			}
		}

		return 0;
	}

	/**
	 * 退出房间
	 */
	@Override
	public Integer leaveRoom(String roomName, Long userId) {
		if (roomName == null || userId == null) {
            try {
                throw new BusinessException("房间名和用户ID不能为空");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }

		log.info("用户 {} 退出房间 {}", userId, roomName);

		// 从Redis房间中移除用户
		redisComponent.removeUserFromRoom(roomName, userId);

		// 删除用户的匹配信息
		redisComponent.deleteUserMatchInfo(userId);

		// 获取房间剩余用户
		Set<String> remainingUsers = redisComponent.getRoomUsers(roomName);

		if (remainingUsers == null || remainingUsers.isEmpty()) {
			// 房间为空，删除数据库记录
			deleteByRoomName(roomName);
			log.info("房间 {} 已关闭", roomName);
		} else {
			// 还有用户在线，通知对方
			String remainingUserId = remainingUsers.iterator().next();

			// 更新剩余用户的匹配状态
			Map<String, String> userInfo = redisComponent.getUserMatchInfo(Long.parseLong(remainingUserId));
			if (userInfo != null) {
				redisComponent.updateUserMatchStatus(Long.parseLong(remainingUserId), Constants.MATCH_STATUS_PEER_LEFT);
			}

			// 发送WebSocket通知
			messageHandler.sendPeerLeftMessage(remainingUserId, roomName);
		}

		return 1;
	}

	/**
	 * 查询匹配状态
	 */
	@Override
	public Integer getMatchStatus(Long userId) {
		Map<String, String> userMatchInfo = redisComponent.getUserMatchInfo(userId);
		if (userMatchInfo == null) {
			return null;
		}
		return Integer.parseInt(userMatchInfo.get("status"));
	}

	/**
	 * 获取用户所在房间
	 */
	@Override
	public SupervisionMatch getUserRoom(Long userId) {
		Map<String, String> userMatchInfo = redisComponent.getUserMatchInfo(userId);
		if (userMatchInfo != null) {
			Integer status = Integer.parseInt(userMatchInfo.get("status"));
			if (status == Constants.MATCH_STATUS_SUCCESS) {
				String roomName = userMatchInfo.get("roomName");
				if (roomName != null && !roomName.isEmpty()) {
					return getByRoomName(roomName);
				}
			}
		}
		return null;
	}

	/**
	 * 生成唯一房间名
	 */
	private String generateRoomName() {
		return "room_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
	}

	/**
	 * 初始化定时任务
	 */
	@PostConstruct
	public void init() {
		// 启动定时清理超时用户的线程
		new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(5000); // 每5秒检查一次
					cleanupTimeoutUsers();
				} catch (InterruptedException e) {
					log.error("清理超时用户线程被中断", e);
					break;
				} catch (Exception e) {
					log.error("清理超时用户时发生错误", e);
				}
			}
		}, "match-timeout-cleaner").start();

		log.info("视频随机匹配服务初始化完成，最大并发匹配数: {}, 匹配超时时间: {}秒",
				MAX_CONCURRENT_MATCH, Constants.MATCH_TIMEOUT);
	}

}