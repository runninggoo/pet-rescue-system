package com.pet.rescue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pet.rescue.entity.Pet;

import java.util.List;
import java.util.Map;

public interface PetService extends IService<Pet> {
    /**
     * 根据条件查询宠物列表
     */
    List<Pet> findPetsByCondition(Map<String, Object> params);

    /**
     * 查询宠物详情
     */
    Pet findPetDetail(Long petId);

    /**
     * 添加宠物（管理员）
     */
    boolean addPet(Pet pet);

    /**
     * 更新宠物信息
     */
    boolean updatePet(Pet pet);

    /**
     * 删除宠物
     */
    boolean deletePet(Long petId);

    /**
     * 更新宠物状态
     */
    boolean updatePetStatus(Long petId, Integer status);
}