package com.pet.rescue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pet.rescue.entity.OperationLog;

import java.util.List;
import java.util.Map;

public interface OperationLogService extends IService<OperationLog> {

    /** 记录操作日志 */
    void logOperation(Long userId, String username, String operation, String detail, String ipAddress, String userRole);

    /** 分页查询日志列表 */
    List<OperationLog> findLogsByCondition(Map<String, Object> params);
}
