package com.pet.rescue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pet.rescue.entity.VolunteerTask;

import java.util.List;
import java.util.Map;

/**
 * 志愿者任务服务接口
 */
public interface VolunteerTaskService extends IService<VolunteerTask> {

    /**
     * 根据条件查询任务列表
     */
    List<VolunteerTask> findByCondition(Map<String, Object> params);

    /**
     * 获取任务详情（含扩展信息）
     */
    VolunteerTask findDetail(Long taskId);

    /**
     * 创建新任务（管理员/机构）
     */
    boolean createTask(VolunteerTask task);

    /**
     * 更新任务
     */
    boolean updateTask(VolunteerTask task);

    /**
     * 删除任务
     */
    boolean deleteTask(Long taskId);

    /**
     * 志愿者领取任务
     */
    boolean claimTask(Long taskId, Long volunteerId);

    /**
     * 完成志愿任务
     * @return 包含完成状态和积分发放结果的Map
     */
    Map<String, Object> completeTask(Long taskId, String result);

    /**
     * 取消任务
     */
    boolean cancelTask(Long taskId);

    /**
     * 获取我的任务（志愿者视角）
     */
    List<VolunteerTask> findMyTasks(Long volunteerId);

    /**
     * 获取任务统计
     */
    Map<String, Object> getStats(Long userId);
}
