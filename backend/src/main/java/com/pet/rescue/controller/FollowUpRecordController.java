package com.pet.rescue.controller;

import com.pet.rescue.entity.FollowUpRecord;
import com.pet.rescue.security.UserContext;
import com.pet.rescue.service.FollowUpRecordService;
import com.pet.rescue.vo.ResponseResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/follow-up")
public class FollowUpRecordController {

    private final FollowUpRecordService followUpRecordService;

    public FollowUpRecordController(FollowUpRecordService followUpRecordService) {
        this.followUpRecordService = followUpRecordService;
    }

    /**
     * 根据申请ID获取所有回访记录（按日期倒序）
     */
    @GetMapping("/list/{adoptionId}")
    public ResponseResult listByAdoptionId(@PathVariable Long adoptionId) {
        try {
            List<FollowUpRecord> records = followUpRecordService.findByAdoptionId(adoptionId);
            return ResponseResult.ok().data("records", records);
        } catch (Exception e) {
            return ResponseResult.error("获取回访记录失败：" + e.getMessage());
        }
    }

    /**
     * 获取回访记录详情
     */
    @GetMapping("/detail/{recordId}")
    public ResponseResult detail(@PathVariable Long recordId) {
        try {
            FollowUpRecord record = followUpRecordService.findDetail(recordId);
            if (record != null) {
                return ResponseResult.ok().data("record", record);
            } else {
                return ResponseResult.error("回访记录不存在");
            }
        } catch (Exception e) {
            return ResponseResult.error("获取回访记录详情失败：" + e.getMessage());
        }
    }

    /**
     * 添加回访记录（管理员/机构管理员/志愿者）
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN') or hasRole('VOLUNTEER')")
    public ResponseResult add(@RequestBody FollowUpRecord record) {
        try {
            // 自动填充回访人ID
            record.setFollowerId(UserContext.getCurrentUserId());
            if (record.getFollowUpDate() == null) {
                record.setFollowUpDate(new Date());
            }
            boolean success = followUpRecordService.addRecord(record);
            if (success) {
                return ResponseResult.ok("回访记录添加成功");
            } else {
                return ResponseResult.error("添加回访记录失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("添加回访记录失败：" + e.getMessage());
        }
    }

    /**
     * 更新回访记录（管理员/机构管理员）
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult update(@RequestBody FollowUpRecord record) {
        try {
            boolean success = followUpRecordService.updateRecord(record);
            if (success) {
                return ResponseResult.ok("回访记录更新成功");
            } else {
                return ResponseResult.error("更新回访记录失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新回访记录失败：" + e.getMessage());
        }
    }

    /**
     * 删除回访记录（逻辑删除，仅管理员）
     */
    @DeleteMapping("/delete/{recordId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult delete(@PathVariable Long recordId) {
        try {
            boolean success = followUpRecordService.deleteRecord(recordId);
            if (success) {
                return ResponseResult.ok("回访记录删除成功");
            } else {
                return ResponseResult.error("删除回访记录失败，记录不存在");
            }
        } catch (Exception e) {
            return ResponseResult.error("删除回访记录失败：" + e.getMessage());
        }
    }
}
