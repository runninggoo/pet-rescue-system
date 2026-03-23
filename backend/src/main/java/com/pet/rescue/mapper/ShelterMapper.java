package com.pet.rescue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pet.rescue.entity.Shelter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 救助所Mapper接口
 * 用于数据库操作
 */
@Mapper
public interface ShelterMapper extends BaseMapper<Shelter> {

    /**
     * 根据区域编码查询救助所
     */
    @Select("SELECT * FROM shelter WHERE region_code = #{regionCode} AND status = 0 ORDER BY create_time DESC")
    List<Shelter> findByRegionCode(@Param("regionCode") String regionCode);

    /**
     * 根据状态查询救助所
     */
    @Select("SELECT * FROM shelter WHERE status = #{status} ORDER BY create_time DESC")
    List<Shelter> findByStatus(@Param("status") Integer status);

    /**
     * 根据入驻状态查询救助所
     */
    @Select("SELECT * FROM shelter WHERE entry_status = #{entryStatus} ORDER BY create_time DESC")
    List<Shelter> findByEntryStatus(@Param("entryStatus") Integer entryStatus);

    /**
     * 根据审核状态查询救助所
     */
    @Select("SELECT * FROM shelter WHERE audit_status = #{auditStatus} ORDER BY create_time DESC")
    List<Shelter> findByAuditStatus(@Param("auditStatus") Integer auditStatus);

    /**
     * 根据经纬度查询附近的救助所（按距离排序）
     * 使用Haversine公式计算距离
     */
    @Select("SELECT *, " +
            "(6371 * acos(cos(radians(#{lat})) * cos(radians(lat)) * " +
            "cos(radians(lon) - radians(#{lon})) + sin(radians(#{lat})) * " +
            "sin(radians(lat)))) AS distance " +
            "FROM shelter " +
            "WHERE status = 0 " +
            "ORDER BY distance ASC " +
            "LIMIT #{limit}")
    List<Shelter> findNearbyShelters(@Param("lat") Double lat, @Param("lon") Double lon, @Param("limit") Integer limit);

    /**
     * 根据容量状态查询救助所
     */
    @Select("SELECT * FROM shelter " +
            "WHERE max_capacity - current_capacity >= #{minCapacity} " +
            "AND status = 0 " +
            "ORDER BY (max_capacity - current_capacity) DESC")
    List<Shelter> findByAvailableCapacity(@Param("minCapacity") Integer minCapacity);

    /**
     * 根据医疗等级查询救助所
     */
    @Select("SELECT * FROM shelter WHERE medical_level >= #{minMedicalLevel} AND status = 0 ORDER BY medical_level DESC")
    List<Shelter> findByMedicalLevel(@Param("minMedicalLevel") Integer minMedicalLevel);

    /**
     * 更新救助所容量
     */
    @Select("UPDATE shelter SET current_capacity = #{currentCapacity} WHERE id = #{id}")
    int updateCapacity(@Param("id") Long id, @Param("currentCapacity") Integer currentCapacity);

    /**
     * 更新入驻状态
     */
    @Select("UPDATE shelter SET entry_status = #{entryStatus}, entry_time = #{entryTime} WHERE id = #{id}")
    int updateEntryStatus(@Param("id") Long id, @Param("entryStatus") Integer entryStatus, @Param("entryTime") java.util.Date entryTime);

    /**
     * 更新审核状态
     */
    @Select("UPDATE shelter SET audit_status = #{auditStatus}, audit_comment = #{auditComment} WHERE id = #{id}")
    int updateAuditStatus(@Param("id") Long id, @Param("auditStatus") Integer auditStatus, @Param("auditComment") String auditComment);

    /**
     * 统计各区域救助所数量
     */
    @Select("SELECT region_code, COUNT(*) as count FROM shelter WHERE status = 0 GROUP BY region_code")
    List<java.util.Map<String, Object>> countByRegion();

    /**
     * 统计各状态救助所数量
     */
    @Select("SELECT status, COUNT(*) as count FROM shelter GROUP BY status")
    List<java.util.Map<String, Object>> countByStatus();

    /**
     * 统计平均容量使用率
     */
    @Select("SELECT AVG(current_capacity / max_capacity) as avgRatio FROM shelter WHERE status = 0")
    Double getAverageCapacityRatio();
}