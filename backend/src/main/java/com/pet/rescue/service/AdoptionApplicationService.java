package com.pet.rescue.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
     * 审核申请（支持状态机验证）
     */
    boolean reviewApplication(Long applicationId, Integer status, String reviewComment);

    /**
     * 检查用户是否已对该宠物提交申请（仅检查待审核和待补充状态）
     */
    boolean hasApplied(Long petId, Long applicantId);

    /**
     * 更新申请状态
     */
    boolean updateStatus(Long applicationId, Integer status);

    /**
     * 根据条件查询申请列表（带权限过滤，分页）
     */
    IPage<AdoptionApplication> findByCondition(Map<String, Object> params);

    /**
     * 根据ID查询申请详情
     */
    AdoptionApplication findById(Long applicationId);

    /**
     * 签署领养协议（状态4→5）
     */
    boolean signContract(Long applicationId);

    /**
     * 获取各状态申请数量统计
     */
    Map<Integer, Long> getStatusCounts();

    /**
     * 检查申请状态是否可以执行状态转换
     */
    boolean canTransition(Integer currentStatus, Integer targetStatus);
}
