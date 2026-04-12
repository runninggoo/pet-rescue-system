package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet.rescue.entity.Shelter;
import com.pet.rescue.entity.VolunteerTask;
import com.pet.rescue.mapper.ShelterMapper;
import com.pet.rescue.mapper.VolunteerTaskMapper;
import com.pet.rescue.service.VolunteerGrowthService;
import com.pet.rescue.service.VolunteerTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class VolunteerTaskServiceImpl extends ServiceImpl<VolunteerTaskMapper, VolunteerTask> implements VolunteerTaskService {

    private final VolunteerTaskMapper volunteerTaskMapper;
    private final ShelterMapper shelterMapper;
    private final VolunteerGrowthService volunteerGrowthService;

    public VolunteerTaskServiceImpl(VolunteerTaskMapper volunteerTaskMapper,
                                   ShelterMapper shelterMapper,
                                   VolunteerGrowthService volunteerGrowthService) {
        this.volunteerTaskMapper = volunteerTaskMapper;
        this.shelterMapper = shelterMapper;
        this.volunteerGrowthService = volunteerGrowthService;
    }

    @Override
    public List<VolunteerTask> findByCondition(Map<String, Object> params) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<VolunteerTask> wrapper =
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();

        if (params != null) {
            if (params.containsKey("status") && params.get("status") != null) {
                wrapper.eq(VolunteerTask::getStatus, params.get("status"));
            }
            if (params.containsKey("taskType") && params.get("taskType") != null
                    && !params.get("taskType").toString().isEmpty()) {
                wrapper.eq(VolunteerTask::getTaskType, params.get("taskType"));
            }
            if (params.containsKey("priority") && params.get("priority") != null) {
                wrapper.eq(VolunteerTask::getPriority, params.get("priority"));
            }
            if (params.containsKey("keyword") && params.get("keyword") != null) {
                String kw = params.get("keyword").toString().trim();
                if (!kw.isEmpty()) {
                    wrapper.and(w -> w
                        .like(VolunteerTask::getTitle, kw)
                        .or()
                        .like(VolunteerTask::getDescription, kw)
                        .or()
                        .like(VolunteerTask::getLocation, kw)
                    );
                }
            }
            if (params.containsKey("volunteerId") && params.get("volunteerId") != null) {
                wrapper.eq(VolunteerTask::getVolunteerId, params.get("volunteerId"));
            }
        }

        wrapper.orderByDesc(VolunteerTask::getPublishDate);
        List<VolunteerTask> tasks = baseMapper.selectList(wrapper);

        // 填充扩展字段
        for (VolunteerTask task : tasks) {
            if (task.getShelterId() != null) {
                Shelter shelter = shelterMapper.selectById(task.getShelterId());
                if (shelter != null) {
                    task.setShelterName(shelter.getName());
                }
            }
        }

        return tasks;
    }

    @Override
    public VolunteerTask findDetail(Long taskId) {
        VolunteerTask task = baseMapper.selectById(taskId);
        if (task != null && task.getShelterId() != null) {
            Shelter shelter = shelterMapper.selectById(task.getShelterId());
            if (shelter != null) {
                task.setShelterName(shelter.getName());
            }
        }
        return task;
    }

    @Override
    public boolean createTask(VolunteerTask task) {
        if (task.getStatus() == null) {
            task.setStatus(0);
        }
        if (task.getPublishDate() == null) {
            task.setPublishDate(new java.util.Date());
        }
        if (task.getSignedPeople() == null) {
            task.setSignedPeople(0);
        }
        return baseMapper.insert(task) > 0;
    }

    @Override
    public boolean updateTask(VolunteerTask task) {
        return baseMapper.updateById(task) > 0;
    }

    @Override
    public boolean deleteTask(Long taskId) {
        return baseMapper.deleteById(taskId) > 0;
    }

    @Override
    public boolean claimTask(Long taskId, Long volunteerId) {
        VolunteerTask task = baseMapper.selectById(taskId);
        if (task == null || task.getStatus() != 0) {
            return false;
        }
        task.setVolunteerId(volunteerId);
        task.setStatus(1);
        return baseMapper.updateById(task) > 0;
    }

    @Override
    public Map<String, Object> completeTask(Long taskId, String result) {
        VolunteerTask task = baseMapper.selectById(taskId);
        if (task == null) {
            Map<String, Object> r = new HashMap<>();
            r.put("success", false);
            r.put("message", "任务不存在");
            return r;
        }
        task.setStatus(2);
        task.setCompletedDate(new java.util.Date());
        task.setResult(result);
        baseMapper.updateById(task);

        // 自动发放积分
        Map<String, Object> growthResult = new HashMap<>();
        if (task.getVolunteerId() != null) {
            int points = task.getRewardPoints() != null ? task.getRewardPoints() : 20;
            growthResult = volunteerGrowthService.awardPointsForTask(
                task.getVolunteerId(), taskId, task.getTitle(), points
            );
        }

        Map<String, Object> r = new HashMap<>();
        r.put("success", true);
        r.put("message", "任务完成，积分已发放！");
        r.put("task", task);
        r.put("growthResult", growthResult);
        return r;
    }

    @Override
    public boolean cancelTask(Long taskId) {
        VolunteerTask task = baseMapper.selectById(taskId);
        if (task == null) {
            return false;
        }
        task.setStatus(3);
        return baseMapper.updateById(task) > 0;
    }

    @Override
    public List<VolunteerTask> findMyTasks(Long volunteerId) {
        return baseMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<VolunteerTask>()
                .eq(VolunteerTask::getVolunteerId, volunteerId)
                .orderByDesc(VolunteerTask::getPublishDate)
        );
    }

    @Override
    public Map<String, Object> getStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<VolunteerTask> baseWrapper =
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<VolunteerTask>()
                .eq(VolunteerTask::getVolunteerId, userId);

        long total = baseMapper.selectCount(baseWrapper);
        long pending = baseMapper.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<VolunteerTask>()
                .eq(VolunteerTask::getVolunteerId, userId)
                .eq(VolunteerTask::getStatus, 0)
        );
        long inProgress = baseMapper.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<VolunteerTask>()
                .eq(VolunteerTask::getVolunteerId, userId)
                .eq(VolunteerTask::getStatus, 1)
        );
        long completed = baseMapper.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<VolunteerTask>()
                .eq(VolunteerTask::getVolunteerId, userId)
                .eq(VolunteerTask::getStatus, 2)
        );

        stats.put("total", total);
        stats.put("pending", pending);
        stats.put("inProgress", inProgress);
        stats.put("completed", completed);

        return stats;
    }

    @Override
    public Map<String, Object> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();
        // 全系统总任务数（无过滤）
        long total = baseMapper.selectCount(null);
        // 待领取
        long pending = baseMapper.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<VolunteerTask>()
                .eq(VolunteerTask::getStatus, 0)
        );
        // 进行中
        long inProgress = baseMapper.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<VolunteerTask>()
                .eq(VolunteerTask::getStatus, 1)
        );
        // 已完成
        long completed = baseMapper.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<VolunteerTask>()
                .eq(VolunteerTask::getStatus, 2)
        );
        // 已取消
        long cancelled = baseMapper.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<VolunteerTask>()
                .eq(VolunteerTask::getStatus, 3)
        );

        stats.put("total", total);
        stats.put("pending", pending);
        stats.put("inProgress", inProgress);
        stats.put("completed", completed);
        stats.put("cancelled", cancelled);

        return stats;
    }
}
