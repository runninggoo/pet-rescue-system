package com.pet.rescue.controller;

import com.pet.rescue.entity.VolunteerTask;
import com.pet.rescue.security.UserContext;
import com.pet.rescue.service.VolunteerTaskService;
import com.pet.rescue.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/volunteer-task")
public class VolunteerTaskController {

    private final VolunteerTaskService volunteerTaskService;

    public VolunteerTaskController(VolunteerTaskService volunteerTaskService) {
        this.volunteerTaskService = volunteerTaskService;
    }

    /**
     * 获取任务列表（根据角色过滤）
     */
    @GetMapping("/list")
    public ResponseResult list(@RequestParam Map<String, Object> params) {
        try {
            // /my-tasks 专门返回志愿者自己的任务（已领取/进行中/已完成）
            // /list 不区分角色，返回所有任务（适合管理员浏览全部，也适合志愿者浏览全局）
            List<VolunteerTask> tasks = volunteerTaskService.findByCondition(params);
            return ResponseResult.ok().data("tasks", tasks);
        } catch (Exception e) {
            return ResponseResult.error("获取任务列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有可用任务（志愿者可领取的任务）
     */
    @GetMapping("/available")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public ResponseResult getAvailableTasks() {
        try {
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("status", 0); // 只查询待领取的任务
            List<VolunteerTask> tasks = volunteerTaskService.findByCondition(params);
            return ResponseResult.ok().data("tasks", tasks);
        } catch (Exception e) {
            return ResponseResult.error("获取可用任务失败：" + e.getMessage());
        }
    }

    /**
     * 获取任务详情
     */
    @GetMapping("/detail/{taskId}")
    public ResponseResult detail(@PathVariable Long taskId) {
        try {
            VolunteerTask task = volunteerTaskService.findDetail(taskId);
            if (task != null) {
                return ResponseResult.ok().data("task", task);
            } else {
                return ResponseResult.error("任务不存在");
            }
        } catch (Exception e) {
            return ResponseResult.error("获取任务详情失败：" + e.getMessage());
        }
    }

    /**
     * 创建任务（管理员/机构管理员）
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult create(@RequestBody VolunteerTask task) {
        try {
            Long userId = UserContext.getCurrentUserId();
            task.setPublisherId(userId);
            if (task.getPublishDate() == null) {
                task.setPublishDate(new Date());
            }
            boolean success = volunteerTaskService.createTask(task);
            if (success) {
                return ResponseResult.ok("创建任务成功");
            } else {
                return ResponseResult.error("创建任务失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("创建任务失败：" + e.getMessage());
        }
    }

    /**
     * 更新任务
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult update(@RequestBody VolunteerTask task) {
        try {
            boolean success = volunteerTaskService.updateTask(task);
            if (success) {
                return ResponseResult.ok("更新任务成功");
            } else {
                return ResponseResult.error("更新任务失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新任务失败：" + e.getMessage());
        }
    }

    /**
     * 删除任务
     */
    @DeleteMapping("/delete/{taskId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult delete(@PathVariable Long taskId) {
        try {
            boolean success = volunteerTaskService.deleteTask(taskId);
            if (success) {
                return ResponseResult.ok("删除任务成功");
            } else {
                return ResponseResult.error("删除任务失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("删除任务失败：" + e.getMessage());
        }
    }

    /**
     * 志愿者领取任务
     */
    @PostMapping("/claim/{taskId}")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public ResponseResult claimTask(@PathVariable Long taskId) {
        try {
            Long volunteerId = UserContext.getCurrentUserId();
            boolean success = volunteerTaskService.claimTask(taskId, volunteerId);
            if (success) {
                return ResponseResult.ok("领取任务成功");
            } else {
                return ResponseResult.error("领取任务失败，任务可能已被领取或不存在");
            }
        } catch (Exception e) {
            return ResponseResult.error("领取任务失败：" + e.getMessage());
        }
    }

    /**
     * 完成志愿任务（自动发放积分）
     */
    @PostMapping("/complete/{taskId}")
    @PreAuthorize("hasRole('VOLUNTEER') or hasRole('ADMIN')")
    public ResponseResult completeTask(@PathVariable Long taskId, @RequestBody Map<String, String> params) {
        try {
            String result = params.getOrDefault("result", "");
            Map<String, Object> completeResult = volunteerTaskService.completeTask(taskId, result);
            if ((Boolean) completeResult.getOrDefault("success", false)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> growthResult = (Map<String, Object>) completeResult.get("growthResult");
                return ResponseResult.ok("任务已完成，积分已发放！")
                        .data("growthResult", growthResult);
            } else {
                return ResponseResult.error((String) completeResult.getOrDefault("message", "完成任务失败"));
            }
        } catch (Exception e) {
            return ResponseResult.error("完成任务失败：" + e.getMessage());
        }
    }

    /**
     * 取消任务
     */
    @PostMapping("/cancel/{taskId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult cancelTask(@PathVariable Long taskId) {
        try {
            boolean success = volunteerTaskService.cancelTask(taskId);
            if (success) {
                return ResponseResult.ok("任务已取消");
            } else {
                return ResponseResult.error("取消任务失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("取消任务失败：" + e.getMessage());
        }
    }

    /**
     * 获取我的任务（志愿者视角）
     */
    @GetMapping("/my-tasks")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public ResponseResult getMyTasks() {
        try {
            Long volunteerId = UserContext.getCurrentUserId();
            List<VolunteerTask> tasks = volunteerTaskService.findMyTasks(volunteerId);
            return ResponseResult.ok().data("tasks", tasks);
        } catch (Exception e) {
            return ResponseResult.error("获取我的任务失败：" + e.getMessage());
        }
    }

    /**
     * 获取任务统计
     */
    @GetMapping("/stats")
    public ResponseResult getStats() {
        try {
            Long userId = UserContext.getCurrentUserId();
            Map<String, Object> stats = volunteerTaskService.getStats(userId);
            return ResponseResult.ok().data("stats", stats);
        } catch (Exception e) {
            return ResponseResult.error("获取任务统计失败：" + e.getMessage());
        }
    }
}
