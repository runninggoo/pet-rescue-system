package com.pet.rescue.utils;

/**
 * 距离计算工具类
 * 使用Haversine公式计算地球两点间距离
 * 适用于上海地区救助所距离计算
 */
public class DistanceCalculator {

    /**
     * 地球半径（公里）
     */
    private static final double EARTH_RADIUS = 6371.0;

    /**
     * 计算两点间距离（公里）
     *
     * @param lat1 点1纬度
     * @param lon1 点1经度
     * @param lat2 点2纬度
     * @param lon2 点2经度
     * @return 距离（公里）
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // 将角度转换为弧度
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // 计算差值
        double latDiff = lat2Rad - lat1Rad;
        double lonDiff = lon2Rad - lon1Rad;

        // Haversine公式
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    /**
     * 计算两点间距离（米）
     */
    public static double calculateDistanceInMeters(double lat1, double lon1, double lat2, double lon2) {
        return calculateDistance(lat1, lon1, lat2, lon2) * 1000;
    }

    /**
     * 格式化距离显示
     */
    public static String formatDistance(double distanceInKm) {
        if (distanceInKm < 1.0) {
            return String.format("%.0f米", distanceInKm * 1000);
        } else if (distanceInKm < 10.0) {
            return String.format("%.1f公里", distanceInKm);
        } else {
            return String.format("%.0f公里", distanceInKm);
        }
    }

    /**
     * 计算方位角（从点1到点2）
     */
    public static double calculateBearing(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double y = Math.sin(lon2Rad - lon1Rad) * Math.cos(lat2Rad);
        double x = Math.cos(lat1Rad) * Math.sin(lat2Rad)
                - Math.sin(lat1Rad) * Math.cos(lat2Rad) * Math.cos(lon2Rad - lon1Rad);

        double bearing = Math.atan2(y, x);
        bearing = Math.toDegrees(bearing);
        bearing = (bearing + 360) % 360;

        return bearing;
    }

    /**
     * 获取方位描述
     */
    public static String getDirectionDescription(double bearing) {
        if (bearing >= 337.5 || bearing < 22.5) {
            return "正北方向";
        } else if (bearing < 67.5) {
            return "东北方向";
        } else if (bearing < 112.5) {
            return "正东方向";
        } else if (bearing < 157.5) {
            return "东南方向";
        } else if (bearing < 202.5) {
            return "正南方向";
        } else if (bearing < 247.5) {
            return "西南方向";
        } else if (bearing < 292.5) {
            return "正西方向";
        } else {
            return "西北方向";
        }
    }

    /**
     * 计算两点间的直线距离和方位
     */
    public static DistanceResult calculateDistanceAndBearing(double lat1, double lon1, double lat2, double lon2) {
        double distance = calculateDistance(lat1, lon1, lat2, lon2);
        double bearing = calculateBearing(lat1, lon1, lat2, lon2);
        String direction = getDirectionDescription(bearing);
        String formattedDistance = formatDistance(distance);

        return new DistanceResult(distance, bearing, direction, formattedDistance);
    }

    /**
     * 距离计算结果类
     */
    public static class DistanceResult {
        private double distance;      // 距离（公里）
        private double bearing;       // 方位角（度）
        private String direction;     // 方位描述
        private String formattedDistance; // 格式化距离

        public DistanceResult(double distance, double bearing, String direction, String formattedDistance) {
            this.distance = distance;
            this.bearing = bearing;
            this.direction = direction;
            this.formattedDistance = formattedDistance;
        }

        public double getDistance() {
            return distance;
        }

        public double getBearing() {
            return bearing;
        }

        public String getDirection() {
            return direction;
        }

        public String getFormattedDistance() {
            return formattedDistance;
        }

        @Override
        public String toString() {
            return String.format("%s，%s", formattedDistance, direction);
        }
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试：浦东新区到徐汇区的距离
        double lat1 = 31.2397; // 浦东新区
        double lon1 = 121.4998;
        double lat2 = 31.1667; // 徐汇区
        double lon2 = 121.4167;

        DistanceResult result = calculateDistanceAndBearing(lat1, lon1, lat2, lon2);
        System.out.println("距离：" + result.getFormattedDistance());
        System.out.println("方向：" + result.getDirection());
        System.out.println("完整信息：" + result);
    }
}