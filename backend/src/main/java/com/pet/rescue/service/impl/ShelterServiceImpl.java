package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet.rescue.entity.Shelter;
import com.pet.rescue.mapper.ShelterMapper;
import com.pet.rescue.service.ShelterService;
import com.pet.rescue.utils.DistanceCalculator;
import com.pet.rescue.security.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 救助所Service实现类
 * 实现所有救助所管理业务逻辑
 */
@Service
@Transactional
public class ShelterServiceImpl extends ServiceImpl<ShelterMapper, Shelter> implements ShelterService {

    private final ShelterMapper shelterMapper;

    public ShelterServiceImpl(ShelterMapper shelterMapper) {
        this.shelterMapper = shelterMapper;
    }

    @Override
    public boolean addShelter(Shelter shelter) {
        // 设置默认值
        if (shelter.getEntryStatus() == null) {
            shelter.setEntryStatus(0); // 未入驻
        }
        if (shelter.getAuditStatus() == null) {
            shelter.setAuditStatus(0); // 待审核
        }
        if (shelter.getStatus() == null) {
            shelter.setStatus(0); // 正常
        }
        if (shelter.getCurrentCapacity() == null) {
            shelter.setCurrentCapacity(0); // 默认0
        }

        return shelterMapper.insert(shelter) > 0;
    }

    @Override
    public boolean updateShelter(Shelter shelter) {
        return shelterMapper.updateById(shelter) > 0;
    }

    @Override
    public boolean deleteShelter(Long id) {
        // 逻辑删除，设置状态为关闭
        Shelter shelter = new Shelter();
        shelter.setId(id);
        shelter.setStatus(3); // 关闭状态
        return shelterMapper.updateById(shelter) > 0;
    }

    @Override
    public Shelter getShelterById(Long id) {
        return shelterMapper.selectById(id);
    }

    @Override
    public List<Shelter> getSheltersByRegion(String regionCode) {
        return shelterMapper.findByRegionCode(regionCode);
    }

    @Override
    public List<Shelter> getSheltersByStatus(Integer status) {
        return shelterMapper.findByStatus(status);
    }

    @Override
    public List<Shelter> getSheltersByEntryStatus(Integer entryStatus) {
        return shelterMapper.findByEntryStatus(entryStatus);
    }

    @Override
    public List<Shelter> getSheltersByAuditStatus(Integer auditStatus) {
        return shelterMapper.findByAuditStatus(auditStatus);
    }

    @Override
    public List<Shelter> matchShelters(Double lat, Double lon, Integer minCapacity, Integer minMedicalLevel, Integer limit) {
        if (limit == null) {
            limit = 3; // 默认推荐3个
        }

        // 获取附近救助所
        List<Shelter> nearbyShelters = shelterMapper.findNearbyShelters(lat, lon, 20); // 先查20个最近的

        // 过滤和评分
        List<ShelterScore> scoredShelters = new ArrayList<>();
        for (Shelter shelter : nearbyShelters) {
            // 容量检查
            if (shelter.getAvailableCapacity() < minCapacity) {
                continue;
            }

            // 医疗等级检查
            if (shelter.getMedicalLevel() < minMedicalLevel) {
                continue;
            }

            // 计算评分
            double score = calculateMatchScore(shelter, lat, lon);
            scoredShelters.add(new ShelterScore(shelter, score));
        }

        // 按评分排序，取前limit个
        scoredShelters.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        List<Shelter> result = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, scoredShelters.size()); i++) {
            result.add(scoredShelters.get(i).getShelter());
        }

        return result;
    }

    @Override
    public List<Shelter> getNearbyShelters(Double lat, Double lon, Integer limit) {
        if (limit == null) {
            limit = 10;
        }
        return shelterMapper.findNearbyShelters(lat, lon, limit);
    }

    @Override
    public List<Shelter> getSheltersByCapacity(Integer minCapacity) {
        return shelterMapper.findByAvailableCapacity(minCapacity);
    }

    @Override
    public List<Shelter> getSheltersByMedicalLevel(Integer minMedicalLevel) {
        return shelterMapper.findByMedicalLevel(minMedicalLevel);
    }

    @Override
    public boolean updateShelterCapacity(Long id, Integer currentCapacity) {
        return shelterMapper.updateCapacity(id, currentCapacity) > 0;
    }

    @Override
    public boolean updateShelterEntryStatus(Long id, Integer entryStatus) {
        Shelter shelter = shelterMapper.selectById(id);
        if (shelter == null) {
            return false;
        }

        java.util.Date entryTime = new java.util.Date();
        return shelterMapper.updateEntryStatus(id, entryStatus, entryTime) > 0;
    }

    @Override
    public boolean updateShelterAuditStatus(Long id, Integer auditStatus, String auditComment) {
        return shelterMapper.updateAuditStatus(id, auditStatus, auditComment) > 0;
    }

    @Override
    public List<Map<String, Object>> getRegionStats() {
        return shelterMapper.countByRegion();
    }

    @Override
    public List<Map<String, Object>> getStatusStats() {
        return shelterMapper.countByStatus();
    }

    @Override
    public Double getAverageCapacityRatio() {
        return shelterMapper.getAverageCapacityRatio();
    }

    @Override
    public Shelter getShelterWithDistance(Long id, Double lat, Double lon) {
        Shelter shelter = shelterMapper.selectById(id);
        if (shelter != null && lat != null && lon != null) {
            double distance = DistanceCalculator.calculateDistance(
                    lat, lon, shelter.getLat(), shelter.getLon());
            shelter.setDistance(distance);
        }
        return shelter;
    }

    @Override
    public List<Shelter> getSheltersWithPermission(Map<String, Object> params) {
        // 权限控制：管理员查看所有，机构管理员查看本区域
        Long currentUserId = UserContext.getCurrentUserId();
        String role = UserContext.getCurrentUserRole();

        if ("admin".equals(role)) {
            // 管理员：查看所有救助所
            return shelterMapper.selectList(null);
        } else if ("institution_admin".equals(role)) {
            // 机构管理员：查看本区域救助所
            Long institutionId = UserContext.getCurrentUserInstitutionId();
            // 这里简化处理，实际应该通过用户信息获取区域编码
            return shelterMapper.findByRegionCode("310115"); // 默认浦东新区
        } else {
            // 其他角色：只能查看已入驻且审核通过的救助所
            return shelterMapper.findByEntryStatus(1);
        }
    }

    /**
     * 计算匹配分数
     */
    private double calculateMatchScore(Shelter shelter, Double lat, Double lon) {
        // 距离评分（40%权重）
        double distance = DistanceCalculator.calculateDistance(
                lat, lon, shelter.getLat(), shelter.getLon());
        double distanceScore = 1.0 / (1.0 + distance / 10.0);

        // 容量评分（30%权重）
        double capacityRatio = (double) shelter.getAvailableCapacity() / shelter.getMaxCapacity();
        double capacityScore = capacityRatio;

        // 医疗等级评分（30%权重）
        double medicalScore = shelter.getMedicalLevel() * 0.25; // 0-0.75分

        // 综合评分
        return distanceScore * 0.4 + capacityScore * 0.3 + medicalScore * 0.3;
    }

    /**
     * 救助所评分类（内部使用）
     */
    private static class ShelterScore {
        private Shelter shelter;
        private double score;

        public ShelterScore(Shelter shelter, double score) {
            this.shelter = shelter;
            this.score = score;
        }

        public Shelter getShelter() {
            return shelter;
        }

        public double getScore() {
            return score;
        }
    }
}