package com.pet.rescue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.pet.rescue.entity.AdoptionApplication;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdoptionApplicationMapper extends BaseMapper<AdoptionApplication> {

    /**
     * 根据宠物ID和申请人ID查询申请记录（仅查询待审核和待补充状态的申请，防止重复申请）
     */
    @InterceptorIgnore(tenantLine = "true")
    @Select("SELECT * FROM adoption_application WHERE applicant_id = #{applicantId} ORDER BY apply_date DESC")
    List<AdoptionApplication> selectByApplicantId(@Param("applicantId") Long applicantId);

    /**
     * 根据宠物ID查询申请记录（全部状态，用于管理）
     */
    @InterceptorIgnore(tenantLine = "true")
    @Select("SELECT * FROM adoption_application WHERE pet_id = #{petId} ORDER BY apply_date DESC")
    List<AdoptionApplication> selectByPetId(@Param("petId") Long petId);

    /**
     * 检查用户是否已申请某宠物（仅检查待审核和待补充状态）
     */
    @InterceptorIgnore(tenantLine = "true")
    @Select("SELECT * FROM adoption_application WHERE pet_id = #{petId} AND applicant_id = #{applicantId} AND status IN (0, 3) LIMIT 1")
    AdoptionApplication findByPetIdAndApplicantId(@Param("petId") Long petId, @Param("applicantId") Long applicantId);

    /**
     * 根据状态查询申请列表
     */
    @InterceptorIgnore(tenantLine = "true")
    @Select("SELECT * FROM adoption_application WHERE status = #{status} ORDER BY apply_date DESC")
    List<AdoptionApplication> findByStatus(@Param("status") Integer status);

    /**
     * 统计各状态的申请数量（用于前端状态标签栏）
     */
    @InterceptorIgnore(tenantLine = "true")
    @Select("SELECT status, COUNT(*) as count FROM adoption_application GROUP BY status")
    List<Map<String, Object>> countByStatus();
}
