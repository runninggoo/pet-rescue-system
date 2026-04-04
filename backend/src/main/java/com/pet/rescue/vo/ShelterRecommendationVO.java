package com.pet.rescue.vo;

/**
 * 救助站推荐结果VO
 * 用于返回推荐的救助站信息，包含距离、容量状态和智能评分
 */
public class ShelterRecommendationVO {

    // ========== 基本信息 ==========
    
    /**
     * 救助站ID
     */
    private Long shelterId;

    /**
     * 救助站名称
     */
    private String name;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 运营描述
     */
    private String description;

    // ========== 容量信息 ==========
    
    /**
     * 最大容量
     */
    private Integer maxCapacity;

    /**
     * 当前容量
     */
    private Integer currentCapacity;

    /**
     * 剩余容量
     */
    private Integer remainingCapacity;

    /**
     * 剩余容量百分比（0-100）
     */
    private Integer remainingCapacityPercent;

    // ========== 距离信息 ==========
    
    /**
     * 距离（公里）
     */
    private Double distance;

    /**
     * 格式化距离（如：2.5公里、800米）
     */
    private String formattedDistance;

    /**
     * 方向描述（如：东北方向）
     */
    private String direction;

    // ========== 容量状态 ==========
    
    /**
     * 容量状态：0-已满，1-紧张，2-充足
     */
    private Integer capacityStatus;

    /**
     * 容量状态描述
     */
    private String capacityStatusText;

    // ========== 智能评分（权重算法） ==========
    
    /**
     * 综合评分（0-100分）
     * 计算公式：距离得分×40% + 容量得分×30% + 医疗等级得分×30%
     */
    private Double totalScore;
    
    /**
     * 距离得分（0-100分，越近越高）
     */
    private Double distanceScore;
    
    /**
     * 容量得分（0-100分，剩余容量越多越高）
     */
    private Double capacityScore;
    
    /**
     * 医疗等级得分（0-100分，等级越高越高）
     */
    private Double medicalScore;
    
    /**
     * 评分等级描述（优秀/良好/一般/较差）
     */
    private String scoreLevel;
    
    /**
     * 评分详情（各维度得分说明）
     */
    private String scoreDetail;

    // ========== 导航链接 ==========
    
    /**
     * 高德地图导航链接
     */
    private String amapUrl;

    /**
     * 百度地图导航链接
     */
    private String baiduUrl;

    // ========== 坐标信息 ==========
    
    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 经度
     */
    private Double longitude;

    public ShelterRecommendationVO() {
    }

    // Getters and Setters
    public Long getShelterId() {
        return shelterId;
    }

    public void setShelterId(Long shelterId) {
        this.shelterId = shelterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Integer getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(Integer currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public Integer getRemainingCapacity() {
        return remainingCapacity;
    }

    public void setRemainingCapacity(Integer remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

    public Integer getRemainingCapacityPercent() {
        return remainingCapacityPercent;
    }

    public void setRemainingCapacityPercent(Integer remainingCapacityPercent) {
        this.remainingCapacityPercent = remainingCapacityPercent;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getFormattedDistance() {
        return formattedDistance;
    }

    public void setFormattedDistance(String formattedDistance) {
        this.formattedDistance = formattedDistance;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Integer getCapacityStatus() {
        return capacityStatus;
    }

    public void setCapacityStatus(Integer capacityStatus) {
        this.capacityStatus = capacityStatus;
    }

    public String getCapacityStatusText() {
        return capacityStatusText;
    }

    public void setCapacityStatusText(String capacityStatusText) {
        this.capacityStatusText = capacityStatusText;
    }

    public String getAmapUrl() {
        return amapUrl;
    }

    public void setAmapUrl(String amapUrl) {
        this.amapUrl = amapUrl;
    }

    public String getBaiduUrl() {
        return baiduUrl;
    }

    public void setBaiduUrl(String baiduUrl) {
        this.baiduUrl = baiduUrl;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    // ========== 评分字段 Getter/Setter ==========
    
    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public Double getDistanceScore() {
        return distanceScore;
    }

    public void setDistanceScore(Double distanceScore) {
        this.distanceScore = distanceScore;
    }

    public Double getCapacityScore() {
        return capacityScore;
    }

    public void setCapacityScore(Double capacityScore) {
        this.capacityScore = capacityScore;
    }

    public Double getMedicalScore() {
        return medicalScore;
    }

    public void setMedicalScore(Double medicalScore) {
        this.medicalScore = medicalScore;
    }

    public String getScoreLevel() {
        return scoreLevel;
    }

    public void setScoreLevel(String scoreLevel) {
        this.scoreLevel = scoreLevel;
    }

    public String getScoreDetail() {
        return scoreDetail;
    }

    public void setScoreDetail(String scoreDetail) {
        this.scoreDetail = scoreDetail;
    }

    // ========== 评分计算方法 ==========
    
    /**
     * 计算综合评分
     * @param maxSearchDistance 最大搜索距离（用于计算距离得分）
     * @param medicalLevel 医疗等级（0-3）
     */
    public void calculateScore(Double maxSearchDistance, Integer medicalLevel) {
        // 1. 计算距离得分（0-100分，越近越高）
        // 使用指数衰减函数，距离越远得分越低
        this.distanceScore = calculateDistanceScore(this.distance, maxSearchDistance);
        
        // 2. 计算容量得分（0-100分，剩余容量越多越高）
        this.capacityScore = calculateCapacityScore(this.remainingCapacity, this.maxCapacity);
        
        // 3. 计算医疗等级得分（0-100分）
        this.medicalScore = calculateMedicalScore(medicalLevel);
        
        // 4. 计算综合评分（加权平均）
        // 权重：距离40% + 容量30% + 医疗等级30%
        double total = this.distanceScore * 0.4 + this.capacityScore * 0.3 + this.medicalScore * 0.3;
        this.totalScore = Math.round(total * 10) / 10.0; // 保留一位小数
        
        // 5. 设置评分等级
        this.scoreLevel = getScoreLevel(this.totalScore);
        
        // 6. 生成评分详情
        this.scoreDetail = generateScoreDetail();
    }
    
    /**
     * 计算距离得分
     * 使用指数衰减：score = 100 * e^(-distance/maxDistance)
     * @param distance 实际距离（公里）
     * @param maxDistance 最大搜索距离（公里）
     * @return 得分（0-100）
     */
    private Double calculateDistanceScore(Double distance, Double maxDistance) {
        if (distance == null || maxDistance == null || maxDistance <= 0) {
            return 0.0;
        }
        // 指数衰减计算
        double score = 100 * Math.exp(-3 * distance / maxDistance);
        return Math.round(score * 10) / 10.0;
    }
    
    /**
     * 计算容量得分
     * @param remainingCapacity 剩余容量
     * @param maxCapacity 最大容量
     * @return 得分（0-100）
     */
    private Double calculateCapacityScore(Integer remainingCapacity, Integer maxCapacity) {
        if (maxCapacity == null || maxCapacity <= 0) {
            return 0.0;
        }
        // 计算剩余容量比例
        double ratio = remainingCapacity / (double) maxCapacity;
        // 基础分50 + 比例加分（最多50分）
        double score = 50 + ratio * 50;
        return Math.round(score * 10) / 10.0;
    }
    
    /**
     * 计算医疗等级得分
     * @param medicalLevel 医疗等级（0-3）
     * @return 得分（0-100）
     */
    private Double calculateMedicalScore(Integer medicalLevel) {
        if (medicalLevel == null) {
            return 25.0; // 无等级给25分
        }
        // 等级 0->25分, 1->50分, 2->75分, 3->100分
        double score = (medicalLevel + 1) * 25.0;
        return score;
    }
    
    /**
     * 根据总分获取等级描述
     */
    private String getScoreLevel(Double score) {
        if (score >= 80) {
            return "优秀";
        } else if (score >= 60) {
            return "良好";
        } else if (score >= 40) {
            return "一般";
        } else {
            return "较差";
        }
    }
    
    /**
     * 生成评分详情描述
     */
    private String generateScoreDetail() {
        return String.format(
            "距离%.1fkm得分%.1f(权重40%%) + 容量得分%.1f(权重30%%) + 医疗得分%.1f(权重30%%) = 总分%.1f",
            distance != null ? distance : 0,
            distanceScore != null ? distanceScore : 0,
            capacityScore != null ? capacityScore : 0,
            medicalScore != null ? medicalScore : 0,
            totalScore != null ? totalScore : 0
        );
    }

    /**
     * 计算容量状态
     * 0-已满(<10%), 1-紧张(10%-30%), 2-充足(>=30%)
     */
    public void calculateCapacityStatus() {
        if (maxCapacity == null || maxCapacity == 0) {
            this.capacityStatus = 0;
            this.capacityStatusText = "已满";
            this.remainingCapacityPercent = 0;
            return;
        }

        double ratio = (double) remainingCapacity / maxCapacity;
        this.remainingCapacityPercent = (int) Math.round(ratio * 100);

        if (ratio < 0.1) {
            this.capacityStatus = 0;
            this.capacityStatusText = "已满";
        } else if (ratio < 0.3) {
            this.capacityStatus = 1;
            this.capacityStatusText = "紧张";
        } else {
            this.capacityStatus = 2;
            this.capacityStatusText = "充足";
        }
    }

    /**
     * 生成地图导航链接
     */
    public void generateMapUrls(Double userLat, Double userLon) {
        if (latitude != null && longitude != null) {
            // 高德地图导航链接（分享链接，无需API Key）
            String encodedName = name != null ? name : "救助站";
            try {
                encodedName = java.net.URLEncoder.encode(encodedName, "UTF-8");
            } catch (java.io.UnsupportedEncodingException e) {
                encodedName = "救助站";
            }
            this.amapUrl = String.format(
                "https://uri.amap.com/navigation?to=%f,%f,%s,car&callnative=1",
                longitude, latitude, encodedName
            );

            // 百度地图导航链接（无需API Key的Web版）
            this.baiduUrl = String.format(
                "https://api.map.baidu.com/direction?origin=%f,%f&destination=%f,%f&mode=driving&output=html",
                userLon, userLat, longitude, latitude
            );
        }
    }

    @Override
    public String toString() {
        return "ShelterRecommendationVO{" +
                "shelterId=" + shelterId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", remainingCapacity=" + remainingCapacity +
                ", maxCapacity=" + maxCapacity +
                ", distance=" + distance +
                ", formattedDistance='" + formattedDistance + '\'' +
                ", capacityStatusText='" + capacityStatusText + '\'' +
                '}';
    }
}