package com.pet.rescue.controller;

import com.pet.rescue.entity.OperationLog;
import com.pet.rescue.security.UserContext;
import com.pet.rescue.service.OperationLogService;
import com.pet.rescue.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/operation-log")
public class OperationLogController {

    private final OperationLogService operationLogService;

    public OperationLogController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    /**
     * 记录操作日志（供其他服务调用）
     */
    @PostMapping("/log")
    public ResponseResult log(@RequestBody Map<String, Object> params) {
        try {
            Long userId = UserContext.getCurrentUserId();
            String username = UserContext.getCurrentUsername();
            String role = UserContext.getCurrentUserRole();
            String operation = (String) params.get("operation");
            String detail = (String) params.get("detail");
            String ipAddress = (String) params.getOrDefault("ipAddress", "unknown");
            operationLogService.logOperation(userId, username, operation, detail, ipAddress, role);
            return ResponseResult.ok("日志记录成功");
        } catch (Exception e) {
            return ResponseResult.error("日志记录失败：" + e.getMessage());
        }
    }

    /**
     * 查询日志列表（仅管理员可见）
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult list(@RequestParam(required = false) String operation,
                               @RequestParam(required = false) Long userId,
                               @RequestParam(defaultValue = "100") Integer limit) {
        try {
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("operation", operation);
            params.put("userId", userId);
            params.put("limit", limit);
            List<OperationLog> logs = operationLogService.findLogsByCondition(params);
            return ResponseResult.ok().data("logs", logs);
        } catch (Exception e) {
            return ResponseResult.error("查询失败：" + e.getMessage());
        }
    }
}
