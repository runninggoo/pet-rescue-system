package com.pet.rescue.dto;

/**
 * 救助站推荐请求DTO
 * 用于接收用户位置信息和推荐筛选条件
 */
public class ShelterRecommendationRequest {
    
    /**
     * 用户纬度
     */
    private Double latitude;
    
    /**
     * 用户经度
     */
    private Double longitude;
    
    /**
     * 最大搜索距离（公里），默认10公里
     */
    private Double maxDistance = 10.0;
    
    /**
     * 是否只返回有剩余容量的救助站，默认true
     */
    private Boolean needCapacity = true;
    
    /**
     * 区域筛选（可选），如"浦东新区"
     */
    private String district;
    
    /**
     * 返回结果数量限制，默认10个
     */
    private Integer limit = 10;
    
    public ShelterRecommendationRequest() {
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
    
    public Double getMaxDistance() {
        return maxDistance;
    }
    
    public void setMaxDistance(Double maxDistance) {
        this.maxDistance = maxDistance;
    }
    
    public Boolean getNeedCapacity() {
        return needCapacity;
    }
    
    public void setNeedCapacity(Boolean needCapacity) {
        this.needCapacity = needCapacity;
    }
    
    public String getDistrict() {
        return district;
    }
    
    public void setDistrict(String district) {
        this.district = district;
    }
    
    public Integer getLimit() {
        return limit;
    }
    
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    
    /**
     * 验证请求参数是否有效
     */
    public boolean isValid() {
        return latitude != null && longitude != null
                && latitude >= -90 && latitude <= 90
                && longitude >= -180 && longitude <= 180;
    }
    
    @Override
    public String toString() {
        return "ShelterRecommendationRequest{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", maxDistance=" + maxDistance +
                ", needCapacity=" + needCapacity +
                ", district='" + district + '\'' +
                ", limit=" + limit +
                '}';
    }
}