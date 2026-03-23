package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet.rescue.entity.Pet;
import com.pet.rescue.mapper.PetMapper;
import com.pet.rescue.service.PetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PetServiceImpl extends ServiceImpl<PetMapper, Pet> implements PetService {

    private final PetMapper petMapper;

    public PetServiceImpl(PetMapper petMapper) {
        this.petMapper = petMapper;
    }

    @Override
    public List<Pet> findPetsByCondition(Map<String, Object> params) {
        // 使用LambdaQueryWrapper构建动态查询
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Pet> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        if (params != null) {
            if (params.containsKey("status")) {
                queryWrapper = queryWrapper.eq(Pet::getStatus, params.get("status"));
            }
            if (params.containsKey("breed")) {
                queryWrapper = queryWrapper.like(Pet::getBreed, params.get("breed"));
            }
            if (params.containsKey("institutionId")) {
                queryWrapper = queryWrapper.eq(Pet::getInstitutionId, params.get("institutionId"));
            }
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Pet findPetDetail(Long petId) {
        return baseMapper.selectById(petId);
    }

    @Override
    public boolean addPet(Pet pet) {
        return baseMapper.insert(pet) > 0;
    }

    @Override
    public boolean updatePet(Pet pet) {
        return baseMapper.updateById(pet) > 0;
    }

    @Override
    public boolean deletePet(Long petId) {
        return baseMapper.deleteById(petId) > 0;
    }

    @Override
    public boolean updatePetStatus(Long petId, Integer status) {
        Pet pet = new Pet();
        pet.setId(petId);
        pet.setStatus(status);
        return baseMapper.updateById(pet) > 0;
    }
}