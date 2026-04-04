package com.pet.rescue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 等级成长规则实体
 */
@Data
@TableName("level_rule")
public class LevelRule {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer level;

    private String levelName;

    private Integer minPoints;

    private Integer maxPoints;

    private String badgeIcon;

    private String privilegeDesc;

    private String levelColor;

    private Integer sortOrder;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    public String getLevelName() { return levelName; }
    public void setLevelName(String levelName) { this.levelName = levelName; }
    public Integer getMinPoints() { return minPoints; }
    public void setMinPoints(Integer minPoints) { this.minPoints = minPoints; }
    public Integer getMaxPoints() { return maxPoints; }
    public void setMaxPoints(Integer maxPoints) { this.maxPoints = maxPoints; }
    public String getBadgeIcon() { return badgeIcon; }
    public void setBadgeIcon(String badgeIcon) { this.badgeIcon = badgeIcon; }
    public String getPrivilegeDesc() { return privilegeDesc; }
    public void setPrivilegeDesc(String privilegeDesc) { this.privilegeDesc = privilegeDesc; }
    public String getLevelColor() { return levelColor; }
    public void setLevelColor(String levelColor) { this.levelColor = levelColor; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
