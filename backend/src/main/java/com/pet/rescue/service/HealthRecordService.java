package com.pet.rescue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pet.rescue.entity.HealthRecord;

import java.util.List;
import java.util.Map;

/**
 * 健康档案服务接口
 */
public interface HealthRecordService extends IService<HealthRecord> {

    /**
     * 根据条件查询健康档案
     */
    List<HealthRecord> findByCondition(Map<String, Object> params);

    /**
     * 查询宠物所有健康档案
     */
    List<HealthRecord> findByPetId(Long petId);

    /**
     * 获取健康档案详情
     */
    HealthRecord findDetail(Long recordId);

    /**
     * 添加健康档案
     */
    boolean addRecord(HealthRecord record);

    /**
     * 更新健康档案
     */
    boolean updateRecord(HealthRecord record);

    /**
     * 删除健康档案
     */
    boolean deleteRecord(Long recordId);

    /**
     * 获取即将到期的提醒（疫苗、体检等）
     */
    List<HealthRecord> findUpcomingReminders(int days);

    /**
     * 获取健康档案统计
     */
    Map<String, Object> getStats(Long petId);
}
