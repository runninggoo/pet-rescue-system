package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet.rescue.entity.Pet;
import com.pet.rescue.entity.PetFavorite;
import com.pet.rescue.mapper.PetFavoriteMapper;
import com.pet.rescue.mapper.PetMapper;
import com.pet.rescue.service.PetFavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PetFavoriteServiceImpl extends ServiceImpl<PetFavoriteMapper, PetFavorite>
        implements PetFavoriteService {

    private final PetFavoriteMapper petFavoriteMapper;
    private final PetMapper petMapper;

    public PetFavoriteServiceImpl(PetFavoriteMapper petFavoriteMapper, PetMapper petMapper) {
        this.petFavoriteMapper = petFavoriteMapper;
        this.petMapper = petMapper;
    }

    @Override
    public boolean addFavorite(Long userId, Long petId) {
        // 检查宠物是否存在
        Pet pet = petMapper.selectById(petId);
        if (pet == null) {
            throw new RuntimeException("宠物不存在");
        }
        // 使用 INSERT ... ON DUPLICATE KEY UPDATE，数据库层自动处理重复键冲突
        int rows = petFavoriteMapper.insertOrReactivate(userId, petId);
        return rows > 0;
    }

    @Override
    public boolean removeFavorite(Long userId, Long petId) {
        // 用 MP 的 deleteById 做逻辑删除（配合 @TableLogic）
        LambdaQueryWrapper<PetFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetFavorite::getUserId, userId)
               .eq(PetFavorite::getPetId, petId);
        PetFavorite favorite = petFavoriteMapper.selectOne(wrapper);
        if (favorite == null) {
            return false;
        }
        return petFavoriteMapper.deleteById(favorite.getId()) > 0;
    }

    @Override
    public boolean isFavorited(Long userId, Long petId) {
        LambdaQueryWrapper<PetFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetFavorite::getUserId, userId)
               .eq(PetFavorite::getPetId, petId);
        return petFavoriteMapper.selectCount(wrapper) > 0;
    }

    @Override
    public Page<PetFavorite> getUserFavorites(Long userId, int page, int pageSize) {
        // 查询用户的收藏记录
        LambdaQueryWrapper<PetFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetFavorite::getUserId, userId)
               .eq(PetFavorite::getDeleted, 0)
               .orderByDesc(PetFavorite::getCreatedAt);
        Page<PetFavorite> pageResult = petFavoriteMapper.selectPage(new Page<>(page, pageSize), wrapper);

        // 填充宠物详情
        if (pageResult.getRecords() != null && !pageResult.getRecords().isEmpty()) {
            enrichWithPetDetails(pageResult.getRecords());
        }
        return pageResult;
    }

    @Override
    public List<Long> getUserFavoritePetIds(Long userId) {
        LambdaQueryWrapper<PetFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetFavorite::getUserId, userId)
               .eq(PetFavorite::getDeleted, 0)
               .select(PetFavorite::getPetId);
        List<PetFavorite> favorites = petFavoriteMapper.selectList(wrapper);
        if (favorites == null) {
            return Collections.emptyList();
        }
        return favorites.stream()
                .map(PetFavorite::getPetId)
                .collect(Collectors.toList());
    }

    /**
     * 填充宠物详情信息
     */
    private void enrichWithPetDetails(List<PetFavorite> favorites) {
        // 收集所有宠物ID
        List<Long> petIds = favorites.stream()
                .map(PetFavorite::getPetId)
                .distinct()
                .collect(Collectors.toList());

        if (petIds.isEmpty()) {
            return;
        }

        // 批量查询宠物信息
        LambdaQueryWrapper<Pet> petWrapper = new LambdaQueryWrapper<>();
        petWrapper.in(Pet::getId, petIds);
        List<Pet> pets = petMapper.selectList(petWrapper);
        Map<Long, Pet> petMap = pets.stream()
                .collect(Collectors.toMap(Pet::getId, p -> p));

        // 填充到收藏记录
        for (PetFavorite favorite : favorites) {
            Pet pet = petMap.get(favorite.getPetId());
            if (pet != null) {
                favorite.setPetName(pet.getName());
                favorite.setPetBreed(pet.getBreed());
                favorite.setPetImageUrl(pet.getImageUrl());
                favorite.setPetStatus(pet.getStatus());
            }
        }
    }
}
