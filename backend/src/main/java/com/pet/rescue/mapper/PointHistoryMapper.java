package com.pet.rescue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pet.rescue.entity.PointHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 积分变动记录Mapper
 */
@Mapper
public interface PointHistoryMapper extends BaseMapper<PointHistory> {

    /**
     * 查询用户积分历史（分页）
     */
    @Select("SELECT * FROM point_history WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    java.util.List<PointHistory> selectByUserIdPage(@Param("userId") Long userId,
                                                     @Param("limit") int limit,
                                                     @Param("offset") int offset);

    /**
     * 查询用户积分历史总数
     */
    @Select("SELECT COUNT(*) FROM point_history WHERE user_id = #{userId}")
    long countByUserId(@Param("userId") Long userId);
}
