package com.wnquxing.controller;

import com.wnquxing.entity.vo.ResponseVO;
import com.wnquxing.service.TaskRemindService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @Auther:关山越
 *
 * @Description:任务提醒Controller
 *
 * @Date:2026-01-12
 *
 */
@RestController
@RequestMapping("/taskRemind")
public class TaskRemindController extends ABaseController {

    @Resource
    private TaskRemindService taskRemindService;

    /**
     * @Description: 获取今日提醒任务
     */
    @RequestMapping("getTodayRemindTasks")
    public ResponseVO getTodayRemindTasks(String userId) {
        return getSuccessResponseVO(taskRemindService.getTodayRemindTasks(userId));
    }

    /**
     * @Description: 获取即将到期任务
     */
    @RequestMapping("getUpcomingTasks")
    public ResponseVO getUpcomingTasks(String userId) {
        return getSuccessResponseVO(taskRemindService.getUpcomingTasks(userId));
    }

    /**
     * @Description: 发送任务提醒
     */
    @RequestMapping("sendTaskRemind")
    public ResponseVO sendTaskRemind(Long taskId) {
        taskRemindService.sendTaskRemind(taskId);
        return getSuccessResponseVO(null);
    }

    /**
     * @Description: 批量发送提醒
     */
    @RequestMapping("sendBatchRemind")
    public ResponseVO sendBatchRemind(String taskIds) {
        // 解析taskIds，如"1,2,3"
        List<Long> ids = Arrays.stream(taskIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        taskRemindService.sendBatchRemind(ids);
        return getSuccessResponseVO(null);
    }
}