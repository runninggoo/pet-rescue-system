package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet.rescue.entity.PetCategory;
import com.pet.rescue.entity.Pet;
import com.pet.rescue.mapper.PetCategoryMapper;
import com.pet.rescue.mapper.PetMapper;
import com.pet.rescue.service.PetCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetCategoryServiceImpl extends ServiceImpl<PetCategoryMapper, PetCategory> implements PetCategoryService {

    @Autowired
    private PetMapper petMapper;

    /**
     * 获取一级分类列表。
     *
     * 策略：无论 type 是否传入，始终先查出所有根分类，再统一填充 children。
     * 子品种的 children 通过 parent_id 查询，不依赖子品种的冗余 type 字段，
     * 避免 type 数据错误导致品种错挂到其他大类下。
     */
    @Override
    public List<PetCategory> getRootCategories(String type) {
        LambdaQueryWrapper<PetCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNull(PetCategory::getParentId)
                .eq(PetCategory::getIsActive, 1)
                .orderByAsc(PetCategory::getSort);
        if (type != null && !type.isEmpty()) {
            wrapper.eq(PetCategory::getType, type);
        }
        List<PetCategory> roots = baseMapper.selectList(wrapper);

        // 一次性查出所有子品种（parent_id IS NOT NULL），按 parentId 分组
        LambdaQueryWrapper<PetCategory> childWrapper = new LambdaQueryWrapper<>();
        childWrapper.isNotNull(PetCategory::getParentId)
                .eq(PetCategory::getIsActive, 1)
                .orderByAsc(PetCategory::getSort);
        List<PetCategory> allChildren = baseMapper.selectList(childWrapper);

        // 按 parentId 分组
        java.util.Map<Long, List<PetCategory>> childMap = new java.util.HashMap<>();
        for (PetCategory child : allChildren) {
            childMap.computeIfAbsent(child.getParentId(), k -> new ArrayList<>()).add(child);
        }

        // 挂载到各根分类下
        for (PetCategory root : roots) {
            root.setChildren(childMap.getOrDefault(root.getId(), new ArrayList<>()));
        }

        // 如果传了 type，只返回一个根（就是过滤后的那一个），结构与原来一致
        return roots;
    }

    /**
     * 获取指定父分类的子分类列表。
     */
    @Override
    public List<PetCategory> getChildren(Long parentId) {
        LambdaQueryWrapper<PetCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetCategory::getParentId, parentId)
                .eq(PetCategory::getIsActive, 1)
                .orderByAsc(PetCategory::getSort);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean addCategory(PetCategory category) {
        if (category.getIsActive() == null) category.setIsActive(1);
        if (category.getSort() == null) category.setSort(0);
        return baseMapper.insert(category) > 0;
    }

    @Override
    public boolean updateCategory(PetCategory category) {
        return baseMapper.updateById(category) > 0;
    }

    @Override
    @Transactional
    public boolean deleteCategory(Long id) {
        // 检查是否有子分类
        LambdaQueryWrapper<PetCategory> childWrapper = new LambdaQueryWrapper<>();
        childWrapper.eq(PetCategory::getParentId, id);
        long childCount = baseMapper.selectCount(childWrapper);
        if (childCount > 0) {
            throw new RuntimeException("该分类下存在子分类，无法删除");
        }
        // 检查是否有宠物关联（如果有categoryId字段的话）
        return baseMapper.deleteById(id) > 0;
    }
}
