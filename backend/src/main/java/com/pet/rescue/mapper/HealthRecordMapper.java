package com.pet.rescue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pet.rescue.entity.HealthRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 健康档案Mapper接口
 */
@Mapper
public interface HealthRecordMapper extends BaseMapper<HealthRecord> {
}
