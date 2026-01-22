package com.wnquxing.service;

import com.wnquxing.entity.vo.TaskRemindVO;
import java.util.List;

/**
 *
 * @Auther:关山越
 *
 * @Description:任务提醒服务
 *
 * @Date:2026-01-12
 *
 */
public interface TaskRemindService {

    /**
     * @Description: 获取用户今日提醒任务
     */
    List<TaskRemindVO> getTodayRemindTasks(String userId);

    /**
     * @Description: 获取用户即将到期的任务
     */
    List<TaskRemindVO> getUpcomingTasks(String userId);

    /**
     * @Description: 发送任务提醒
     */
    void sendTaskRemind(Long taskId);

    /**
     * @Description: 批量发送提醒
     */
    void sendBatchRemind(List<Long> taskIds);
}
