package com.pet.rescue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.pet.rescue.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据ID列表批量查询用户（原生SQL，绕过MP拦截链）
     */
    @InterceptorIgnore(tenantLine = "true")
    @Select("<script>SELECT * FROM user WHERE id IN <foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach></script>")
    List<User> selectByIds(@Param("ids") List<Long> ids);
}