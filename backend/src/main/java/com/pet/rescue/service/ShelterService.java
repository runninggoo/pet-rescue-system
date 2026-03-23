package com.pet.rescue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pet.rescue.entity.Shelter;

import java.util.List;
import java.util.Map;

/**
 * 救助所Service接口
 * 定义救助所管理的所有业务方法
 */
public interface ShelterService extends IService<Shelter> {

    /**
     * 添加救助所（管理员）
     */
    boolean addShelter(Shelter shelter);

    /**
     * 更新救助所信息
     */
    boolean updateShelter(Shelter shelter);

    /**
     * 删除救助所（逻辑删除）
     */
    boolean deleteShelter(Long id);

    /**
     * 根据ID查询救助所详情
     */
    Shelter getShelterById(Long id);

    /**
     * 根据区域查询救助所
     */
    List<Shelter> getSheltersByRegion(String regionCode);

    /**
     * 根据状态查询救助所
     */
    List<Shelter> getSheltersByStatus(Integer status);

    /**
     * 根据入驻状态查询救助所
     */
    List<Shelter> getSheltersByEntryStatus(Integer entryStatus);

    /**
     * 根据审核状态查询救助所
     */
    List<Shelter> getSheltersByAuditStatus(Integer auditStatus);

    /**
     * 智能匹配救助所
     * 根据位置、容量、医疗等级等因素推荐
     */
    List<Shelter> matchShelters(Double lat, Double lon, Integer minCapacity, Integer minMedicalLevel, Integer limit);

    /**
     * 根据经纬度查询附近的救助所
     */
    List<Shelter> getNearbyShelters(Double lat, Double lon, Integer limit);

    /**
     * 根据可用容量查询救助所
     */
    List<Shelter> getSheltersByCapacity(Integer minCapacity);

    /**
     * 根据医疗等级查询救助所
     */
    List<Shelter> getSheltersByMedicalLevel(Integer minMedicalLevel);

    /**
     * 更新救助所容量
     */
    boolean updateShelterCapacity(Long id, Integer currentCapacity);

    /**
     * 更新入驻状态
     */
    boolean updateShelterEntryStatus(Long id, Integer entryStatus);

    /**
     * 更新审核状态
     */
    boolean updateShelterAuditStatus(Long id, Integer auditStatus, String auditComment);

    /**
     * 获取各区域救助所统计
     */
    List<Map<String, Object>> getRegionStats();

    /**
     * 获取各状态救助所统计
     */
    List<Map<String, Object>> getStatusStats();

    /**
     * 获取平均容量使用率
     */
    Double getAverageCapacityRatio();

    /**
     * 获取救助所详情（包含距离计算）
     */
    Shelter getShelterWithDistance(Long id, Double lat, Double lon);

    /**
     * 获取救助所列表（带权限过滤）
     */
    List<Shelter> getSheltersWithPermission(Map<String, Object> params);
}