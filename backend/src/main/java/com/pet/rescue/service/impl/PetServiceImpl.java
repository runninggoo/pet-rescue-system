package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet.rescue.entity.Pet;
import com.pet.rescue.mapper.PetMapper;
import com.pet.rescue.service.PetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PetServiceImpl extends ServiceImpl<PetMapper, Pet> implements PetService {

    private static final Logger log = LoggerFactory.getLogger(PetServiceImpl.class);

    private final PetMapper petMapper;

    public PetServiceImpl(PetMapper petMapper) {
        this.petMapper = petMapper;
    }

    @Override
    public IPage<Pet> findPetsByCondition(Map<String, Object> params) {
        int page = 1;
        int pageSize = 12;
        if (params != null) {
            if (params.containsKey("page")) {
                Object pageObj = params.get("page");
                if (pageObj != null) {
                    try { page = Integer.parseInt(pageObj.toString()); } catch (Exception ignored) {}
                }
            }
            if (params.containsKey("pageSize")) {
                Object psObj = params.get("pageSize");
                if (psObj != null) {
                    try { pageSize = Integer.parseInt(psObj.toString()); } catch (Exception ignored) {}
                }
            }
            if (pageSize > 50) pageSize = 50;
            if (pageSize < 1) pageSize = 12;
            if (page < 1) page = 1;
        }
        log.info("分页查询: page={}, pageSize={}", page, pageSize);

        // 构建完整查询条件，手动添加逻辑删除条件（deleted = 0）
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Pet> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        queryWrapper.eq(Pet::getDeleted, 0);

        if (params != null) {
            if (params.containsKey("status")) {
                Object status = params.get("status");
                if (status != null && !status.toString().isEmpty()) {
                    queryWrapper.eq(Pet::getStatus, status);
                }
            }
            if (params.containsKey("keyword")) {
                Object keyword = params.get("keyword");
                if (keyword != null && !keyword.toString().trim().isEmpty()) {
                    String kw = keyword.toString().trim();
                    queryWrapper.and(w -> w.like(Pet::getName, kw).or().like(Pet::getBreed, kw));
                }
            }
            if (params.containsKey("breed")) {
                Object breed = params.get("breed");
                if (breed != null && !breed.toString().isEmpty()) {
                    queryWrapper.like(Pet::getBreed, breed);
                }
            }
            if (params.containsKey("categoryType")) {
                Object categoryType = params.get("categoryType");
                if (categoryType != null && !categoryType.toString().isEmpty()) {
                    // 同时兜底 category_type 为 NULL 的记录（数据不完整时的容错处理）
                    queryWrapper.and(w -> w.eq(Pet::getCategoryType, categoryType).or().isNull(Pet::getCategoryType));
                }
            }
            if (params.containsKey("urgencyLevel")) {
                Object urgencyLevel = params.get("urgencyLevel");
                if (urgencyLevel != null && !urgencyLevel.toString().isEmpty()) {
                    queryWrapper.eq(Pet::getUrgencyLevel, urgencyLevel);
                }
            }
            if (params.containsKey("regionCode") && params.get("regionCode") != null
                    && !params.get("regionCode").toString().isEmpty()) {
                queryWrapper.eq(Pet::getInstitutionId, params.get("regionCode"));
            }
            if (params.containsKey("institutionId")) {
                Object institutionId = params.get("institutionId");
                if (institutionId != null && !institutionId.toString().isEmpty()) {
                    queryWrapper.eq(Pet::getInstitutionId, institutionId);
                }
            }
        }

        // 先手动查询符合条件的数据总量
        Long total = baseMapper.selectCount(queryWrapper);
        log.info("手动 COUNT 结果: total={}", total);

        // 构建排序
        queryWrapper.orderByDesc(Pet::getUrgencyLevel)
                   .orderByDesc(Pet::getRescueDate)
                   .orderByDesc(Pet::getId);

        // 手动分页：先查出全部符合条件的数据，再截取当前页
        List<Pet> allPets = baseMapper.selectList(queryWrapper);
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, allPets.size());
        List<Pet> pagedPets = (fromIndex < allPets.size()) ? allPets.subList(fromIndex, toIndex) : List.of();

        // 构建分页结果
        Page<Pet> pageResult = new Page<>(page, pageSize, total, false);
        pageResult.setRecords(pagedPets);
        return pageResult;
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