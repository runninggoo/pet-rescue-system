package com.pet.rescue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pet.rescue.entity.FollowUpRecord;

import java.util.List;

/**
 * 回访记录服务接口
 */
public interface FollowUpRecordService extends IService<FollowUpRecord> {

    /**
     * 根据申请ID查询所有回访记录（按回访日期倒序）
     */
    List<FollowUpRecord> findByAdoptionId(Long adoptionId);

    /**
     * 获取回访记录详情（含申请人信息）
     */
    FollowUpRecord findDetail(Long recordId);

    /**
     * 添加回访记录
     */
    boolean addRecord(FollowUpRecord record);

    /**
     * 更新回访记录
     */
    boolean updateRecord(FollowUpRecord record);

    /**
     * 删除回访记录（逻辑删除）
     */
    boolean deleteRecord(Long recordId);
}
