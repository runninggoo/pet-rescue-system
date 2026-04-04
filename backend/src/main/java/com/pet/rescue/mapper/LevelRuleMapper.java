package com.pet.rescue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pet.rescue.entity.LevelRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 等级规则Mapper
 */
@Mapper
public interface LevelRuleMapper extends BaseMapper<LevelRule> {

    /**
     * 获取所有等级规则（按等级排序）
     */
    @Select("SELECT * FROM level_rule ORDER BY level ASC")
    java.util.List<LevelRule> selectAllOrderByLevel();
}
