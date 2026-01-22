package com.wnquxing.service.impl;

import com.wnquxing.entity.vo.TaskRemindVO;
import com.wnquxing.service.TaskRemindService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service("taskRemindService")
public class TaskRemindServiceImpl implements TaskRemindService {

    @Override
    public List<TaskRemindVO> getTodayRemindTasks(String userId) {
        // 实现今日提醒逻辑
        return new ArrayList<>();
    }

    @Override
    public List<TaskRemindVO> getUpcomingTasks(String userId) {
        // 实现即将到期任务逻辑
        return new ArrayList<>();
    }

    @Override
    public void sendTaskRemind(Long taskId) {
        // 实现发送单个提醒逻辑
    }

    @Override
    public void sendBatchRemind(List<Long> taskIds) {
        // 实现批量发送提醒逻辑
    }

}
