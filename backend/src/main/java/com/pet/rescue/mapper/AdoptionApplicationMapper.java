package com.pet.rescue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pet.rescue.entity.AdoptionApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdoptionApplicationMapper extends BaseMapper<AdoptionApplication> {

    /**
     * 根据宠物ID和申请人ID查询申请记录
     */
    AdoptionApplication findByPetIdAndApplicantId(@Param("petId") Long petId, @Param("applicantId") Long applicantId);

    /**
     * 根据状态查询申请列表
     */
    List<AdoptionApplication> findByStatus(@Param("status") Integer status);

    /**
     * 更新申请状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("reviewComment") String reviewComment);
}