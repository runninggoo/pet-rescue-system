package com.pet.rescue.controller;

import com.pet.rescue.entity.HealthRecord;
import com.pet.rescue.security.UserContext;
import com.pet.rescue.service.HealthRecordService;
import com.pet.rescue.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/health-record")
public class HealthRecordController {

    private final HealthRecordService healthRecordService;

    public HealthRecordController(HealthRecordService healthRecordService) {
        this.healthRecordService = healthRecordService;
    }

    /**
     * 获取健康档案列表（根据角色过滤）
     */
    @GetMapping("/list")
    public ResponseResult list(@RequestParam Map<String, Object> params) {
        try {
            // 支持按宠物ID查询
            List<HealthRecord> records = healthRecordService.findByCondition(params);
            return ResponseResult.ok().data("records", records);
        } catch (Exception e) {
            return ResponseResult.error("获取健康档案列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取宠物健康档案
     */
    @GetMapping("/pet/{petId}")
    public ResponseResult getByPetId(@PathVariable Long petId) {
        try {
            List<HealthRecord> records = healthRecordService.findByPetId(petId);
            return ResponseResult.ok().data("records", records);
        } catch (Exception e) {
            return ResponseResult.error("获取健康档案失败：" + e.getMessage());
        }
    }

    /**
     * 获取健康档案详情
     */
    @GetMapping("/detail/{recordId}")
    public ResponseResult detail(@PathVariable Long recordId) {
        try {
            HealthRecord record = healthRecordService.findDetail(recordId);
            if (record != null) {
                return ResponseResult.ok().data("record", record);
            } else {
                return ResponseResult.error("健康档案不存在");
            }
        } catch (Exception e) {
            return ResponseResult.error("获取健康档案详情失败：" + e.getMessage());
        }
    }

    /**
     * 添加健康档案（管理员/机构管理员）
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult add(@RequestBody HealthRecord record) {
        try {
            if (record.getRecordDate() == null) {
                record.setRecordDate(new Date());
            }
            boolean success = healthRecordService.addRecord(record);
            if (success) {
                return ResponseResult.ok("添加健康档案成功");
            } else {
                return ResponseResult.error("添加健康档案失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("添加健康档案失败：" + e.getMessage());
        }
    }

    /**
     * 更新健康档案
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult update(@RequestBody HealthRecord record) {
        try {
            boolean success = healthRecordService.updateRecord(record);
            if (success) {
                return ResponseResult.ok("更新健康档案成功");
            } else {
                return ResponseResult.error("更新健康档案失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新健康档案失败：" + e.getMessage());
        }
    }

    /**
     * 删除健康档案
     */
    @DeleteMapping("/delete/{recordId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult delete(@PathVariable Long recordId) {
        try {
            boolean success = healthRecordService.deleteRecord(recordId);
            if (success) {
                return ResponseResult.ok("删除健康档案成功");
            } else {
                return ResponseResult.error("删除健康档案失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("删除健康档案失败：" + e.getMessage());
        }
    }

    /**
     * 获取即将到期的健康提醒
     */
    @GetMapping("/reminders")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult getReminders(@RequestParam(defaultValue = "7") int days) {
        try {
            List<HealthRecord> reminders = healthRecordService.findUpcomingReminders(days);
            return ResponseResult.ok().data("reminders", reminders);
        } catch (Exception e) {
            return ResponseResult.error("获取健康提醒失败：" + e.getMessage());
        }
    }

    /**
     * 获取健康档案统计
     */
    @GetMapping("/stats/{petId}")
    public ResponseResult getStats(@PathVariable Long petId) {
        try {
            Map<String, Object> stats = healthRecordService.getStats(petId);
            return ResponseResult.ok().data("stats", stats);
        } catch (Exception e) {
            return ResponseResult.error("获取健康档案统计失败：" + e.getMessage());
        }
    }
}
