package com.pet.rescue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pet.rescue.entity.AdoptionApplication;

import java.util.List;
import java.util.Map;

public interface AdoptionApplicationService extends IService<AdoptionApplication> {

    /**
     * 提交领养申请
     */
    boolean submitApplication(AdoptionApplication application);

    /**
     * 根据用户ID查询申请列表
     */
    List<AdoptionApplication> findByApplicantId(Long applicantId);

    /**
     * 根据宠物ID查询申请列表
     */
    List<AdoptionApplication> findByPetId(Long petId);

    /**
     * 根据状态查询申请列表
     */
    List<AdoptionApplication> findByStatus(Integer status);

    /**
     * 审核申请
     */
    boolean reviewApplication(Long applicationId, Integer status, String reviewComment);

    /**
     * 检查用户是否已对该宠物提交申请
     */
    boolean hasApplied(Long petId, Long applicantId);

    /**
     * 更新申请状态
     */
    boolean updateStatus(Long applicationId, Integer status);

    /**
     * 根据条件查询申请列表（带权限过滤）
     */
    List<AdoptionApplication> findByCondition(Map<String, Object> params);
}