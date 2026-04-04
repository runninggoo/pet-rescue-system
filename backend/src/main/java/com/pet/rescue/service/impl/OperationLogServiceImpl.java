package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet.rescue.entity.OperationLog;
import com.pet.rescue.mapper.OperationLogMapper;
import com.pet.rescue.service.OperationLogService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Override
    public void logOperation(Long userId, String username, String operation, String detail, String ipAddress, String userRole) {
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setOperation(operation);
        log.setDetail(detail);
        log.setIpAddress(ipAddress);
        log.setUserRole(userRole);
        log.setCreatedAt(new Date());
        baseMapper.insert(log);
    }

    @Override
    public List<OperationLog> findLogsByCondition(Map<String, Object> params) {
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(OperationLog::getCreatedAt);

        if (params != null) {
            if (params.containsKey("userId") && params.get("userId") != null) {
                wrapper.eq(OperationLog::getUserId, params.get("userId"));
            }
            if (params.containsKey("operation") && params.get("operation") != null
                    && !params.get("operation").toString().isEmpty()) {
                wrapper.eq(OperationLog::getOperation, params.get("operation"));
            }
            // 分页参数
            if (params.containsKey("limit")) {
                wrapper.last("LIMIT " + params.get("limit"));
            }
        }

        return baseMapper.selectList(wrapper);
    }
}
