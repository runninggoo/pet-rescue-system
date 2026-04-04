package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet.rescue.dto.ShelterRecommendationRequest;
import com.pet.rescue.entity.Shelter;
import com.pet.rescue.mapper.ShelterMapper;
import com.pet.rescue.service.ShelterService;
import com.pet.rescue.utils.DistanceCalculator;
import com.pet.rescue.vo.ShelterRecommendationVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShelterServiceImpl extends ServiceImpl<ShelterMapper, Shelter> implements ShelterService {

    private final ShelterMapper shelterMapper;

    public ShelterServiceImpl(ShelterMapper shelterMapper) {
        this.shelterMapper = shelterMapper;
    }

    @Override
    public boolean addShelter(Shelter shelter) {
        if (shelter.getEntryStatus() == null) {
            shelter.setEntryStatus(0);
        }
        if (shelter.getAuditStatus() == null) {
            shelter.setAuditStatus(0);
        }
        if (shelter.getStatus() == null) {
            shelter.setStatus(0);
        }
        return baseMapper.insert(shelter) > 0;
    }

    @Override
    public boolean updateShelter(Shelter shelter) {
        return baseMapper.updateById(shelter) > 0;
    }

    @Override
    public boolean deleteShelter(Long id) {
        Shelter shelter = new Shelter();
        shelter.setId(id);
        shelter.setStatus(3); // 3表示关闭
        return baseMapper.updateById(shelter) > 0;
    }

    @Override
    public Shelter getShelterById(Long id) {
        return baseMapper.selectById(id);
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
        LambdaQueryWrapper<Shelter> queryWrapper = new LambdaQueryWrapper<>();
        
        // 状态正常
        queryWrapper.eq(Shelter::getStatus, 0);
        
        // 已入驻
        queryWrapper.eq(Shelter::getEntryStatus, 1);
        
        // 已审核通过
        queryWrapper.eq(Shelter::getAuditStatus, 1);
        
        // 最小容量筛选
        if (minCapacity != null && minCapacity > 0) {
            queryWrapper.apply("max_capacity - current_capacity >= {0}", minCapacity);
        }
        
        // 医疗等级筛选
        if (minMedicalLevel != null && minMedicalLevel > 0) {
            queryWrapper.ge(Shelter::getMedicalLevel, minMedicalLevel);
        }
        
        List<Shelter> shelters = baseMapper.selectList(queryWrapper);
        
        // 计算距离并排序
        for (Shelter shelter : shelters) {
            if (shelter.getLat() != null && shelter.getLon() != null) {
                double distance = DistanceCalculator.calculateDistance(lat, lon, shelter.getLat(), shelter.getLon());
                shelter.setDistance(distance);
            }
        }
        
        // 按距离排序
        shelters.sort(Comparator.comparingDouble(s -> s.getDistance() != null ? s.getDistance() : Double.MAX_VALUE));
        
        // 限制返回数量
        if (limit != null && limit > 0 && shelters.size() > limit) {
            return shelters.subList(0, limit);
        }
        
        return shelters;
    }

    @Override
    public List<Shelter> getNearbyShelters(Double lat, Double lon, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return shelterMapper.findNearbyShelters(lat, lon, limit);
    }

    @Override
    public List<Shelter> getSheltersByCapacity(Integer minCapacity) {
        if (minCapacity == null || minCapacity <= 0) {
            minCapacity = 1;
        }
        return shelterMapper.findByAvailableCapacity(minCapacity);
    }

    @Override
    public List<Shelter> getSheltersByMedicalLevel(Integer minMedicalLevel) {
        if (minMedicalLevel == null || minMedicalLevel < 0) {
            minMedicalLevel = 0;
        }
        return shelterMapper.findByMedicalLevel(minMedicalLevel);
    }

    @Override
    public boolean updateShelterCapacity(Long id, Integer currentCapacity) {
        return shelterMapper.updateCapacity(id, currentCapacity) > 0;
    }

    @Override
    public boolean updateShelterEntryStatus(Long id, Integer entryStatus) {
        Date entryTime = entryStatus == 1 ? new Date() : null;
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
        Shelter shelter = baseMapper.selectById(id);
        if (shelter != null && shelter.getLat() != null && shelter.getLon() != null && lat != null && lon != null) {
            double distance = DistanceCalculator.calculateDistance(lat, lon, shelter.getLat(), shelter.getLon());
            shelter.setDistance(distance);
        }
        return shelter;
    }

    @Override
    public List<Shelter> getSheltersWithPermission(Map<String, Object> params) {
        LambdaQueryWrapper<Shelter> queryWrapper = new LambdaQueryWrapper<>();
        
        if (params != null) {
            // 按状态筛选
            if (params.containsKey("status")) {
                Object status = params.get("status");
                if (status instanceof Integer) {
                    queryWrapper.eq(Shelter::getStatus, status);
                } else if (status instanceof String) {
                    queryWrapper.eq(Shelter::getStatus, Integer.parseInt((String) status));
                }
            }
            
            // 按入驻状态筛选
            if (params.containsKey("entryStatus")) {
                Object entryStatus = params.get("entryStatus");
                if (entryStatus instanceof Integer) {
                    queryWrapper.eq(Shelter::getEntryStatus, entryStatus);
                } else if (entryStatus instanceof String) {
                    queryWrapper.eq(Shelter::getEntryStatus, Integer.parseInt((String) entryStatus));
                }
            }
            
            // 按审核状态筛选
            if (params.containsKey("auditStatus")) {
                Object auditStatus = params.get("auditStatus");
                if (auditStatus instanceof Integer) {
                    queryWrapper.eq(Shelter::getAuditStatus, auditStatus);
                } else if (auditStatus instanceof String) {
                    queryWrapper.eq(Shelter::getAuditStatus, Integer.parseInt((String) auditStatus));
                }
            }
            
            // 按区域筛选
            if (params.containsKey("regionCode") && StringUtils.hasText((String) params.get("regionCode"))) {
                queryWrapper.eq(Shelter::getRegionCode, (String) params.get("regionCode"));
            }
            
            // 按名称模糊搜索
            if (params.containsKey("name") && StringUtils.hasText((String) params.get("name"))) {
                queryWrapper.like(Shelter::getName, (String) params.get("name"));
            }
        }
        
        queryWrapper.orderByDesc(Shelter::getCreatedTime);
        
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ShelterRecommendationVO> recommendShelters(ShelterRecommendationRequest request) {
        // 参数校验
        if (request == null || request.getLatitude() == null || request.getLongitude() == null) {
            throw new IllegalArgumentException("位置信息不能为空");
        }
        
        Double lat = request.getLatitude();
        Double lon = request.getLongitude();
        Double maxDistance = request.getMaxDistance() != null ? request.getMaxDistance() : 10.0;
        Boolean needCapacity = request.getNeedCapacity() != null ? request.getNeedCapacity() : true;
        String district = request.getDistrict();
        Integer limit = request.getLimit() != null ? request.getLimit() : 10;
        
        // 校验坐标范围
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException("纬度必须在-90到90之间");
        }
        if (lon < -180 || lon > 180) {
            throw new IllegalArgumentException("经度必须在-180到180之间");
        }
        
        // 构建查询条件
        LambdaQueryWrapper<Shelter> queryWrapper = new LambdaQueryWrapper<>();
        
        // 只查询状态正常的救助站
        queryWrapper.eq(Shelter::getStatus, 0);
        
        // 只查询已入驻的救助站
        queryWrapper.eq(Shelter::getEntryStatus, 1);
        
        // 只查询审核通过的救助站
        queryWrapper.eq(Shelter::getAuditStatus, 1);
        
        // 如果只返回有容量的救助站
        if (needCapacity) {
            queryWrapper.apply("max_capacity - current_capacity > 0");
        }
        
        // 区域筛选
        if (StringUtils.hasText(district)) {
            String regionCode = getRegionCodeByName(district);
            if (StringUtils.hasText(regionCode)) {
                queryWrapper.eq(Shelter::getRegionCode, regionCode);
            }
        }
        
        List<Shelter> shelters = baseMapper.selectList(queryWrapper);

        // 计算距离和评分
        List<ShelterRecommendationVO> recommendations = new ArrayList<>();
        
        for (Shelter shelter : shelters) {
            if (shelter.getLat() == null || shelter.getLon() == null) {
                continue;
            }
            
            // 计算距离
            double distance = DistanceCalculator.calculateDistance(lat, lon, shelter.getLat(), shelter.getLon());
            
            // 筛选距离内的救助站
            if (distance <= maxDistance) {
                ShelterRecommendationVO vo = convertToRecommendationVO(shelter, lat, lon, distance);
                
                // 计算智能评分（权重算法）
                // 权重：距离40% + 容量30% + 医疗等级30%
                vo.calculateScore(maxDistance, shelter.getMedicalLevel());
                
                recommendations.add(vo);
            }
        }
        
        // 按综合评分降序排序（分数越高越靠前）
        recommendations.sort((a, b) -> {
            // 先按总分降序
            int scoreCompare = Double.compare(
                b.getTotalScore() != null ? b.getTotalScore() : 0,
                a.getTotalScore() != null ? a.getTotalScore() : 0
            );
            if (scoreCompare != 0) return scoreCompare;
            // 总分相同则按距离升序
            return Double.compare(
                a.getDistance() != null ? a.getDistance() : Double.MAX_VALUE,
                b.getDistance() != null ? b.getDistance() : Double.MAX_VALUE
            );
        });
        
        // 限制返回数量
        if (recommendations.size() > limit) {
            return recommendations.subList(0, limit);
        }
        
        return recommendations;
    }
    
    /**
     * 将Shelter实体转换为ShelterRecommendationVO
     */
    private ShelterRecommendationVO convertToRecommendationVO(Shelter shelter, Double userLat, Double userLon, Double distance) {
        ShelterRecommendationVO vo = new ShelterRecommendationVO();
        
        // 基本信息
        vo.setShelterId(shelter.getId());
        vo.setName(shelter.getName());
        vo.setAddress(shelter.getAddress());
        vo.setPhone(shelter.getPhone());
        vo.setDescription(shelter.getDescription());
        
        // 容量信息
        vo.setMaxCapacity(shelter.getMaxCapacity());
        vo.setCurrentCapacity(shelter.getCurrentCapacity());
        vo.setRemainingCapacity(shelter.getAvailableCapacity());
        
        // 距离信息
        vo.setDistance(distance);
        vo.setFormattedDistance(DistanceCalculator.formatDistance(distance));
        
        // 方向信息
        double bearing = DistanceCalculator.calculateBearing(userLat, userLon, shelter.getLat(), shelter.getLon());
        vo.setDirection(DistanceCalculator.getDirectionDescription(bearing));
        
        // 坐标
        vo.setLatitude(shelter.getLat());
        vo.setLongitude(shelter.getLon());
        
        // 容量状态
        vo.calculateCapacityStatus();
        
        // 生成导航链接
        vo.generateMapUrls(userLat, userLon);
        
        return vo;
    }
    
    /**
     * 根据区域名称获取区域编码
     */
    private String getRegionCodeByName(String district) {
        if (district == null) {
            return null;
        }
        
        // 上海各区区域编码映射
        switch (district.trim()) {
            case "浦东新区":
            case "浦东":
                return "310115";
            case "徐汇区":
            case "徐汇":
                return "310104";
            case "闵行区":
            case "闵行":
                return "310112";
            case "静安区":
            case "静安":
                return "310106";
            case "长宁区":
            case "长宁":
                return "310105";
            case "普陀区":
            case "普陀":
                return "310107";
            case "虹口区":
            case "虹口":
                return "310109";
            case "杨浦区":
            case "杨浦":
                return "310110";
            case "宝山区":
            case "宝山":
                return "310113";
            case "嘉定区":
            case "嘉定":
                return "310114";
            case "金山区":
            case "金山":
                return "310116";
            case "松江区":
            case "松江":
                return "310117";
            case "青浦区":
            case "青浦":
                return "310118";
            case "奉贤区":
            case "奉贤":
                return "310120";
            case "崇明区":
            case "崇明":
                return "310151";
            case "黄浦区":
            case "黄浦":
                return "310101";
            default:
                return null;
        }
    }
}
