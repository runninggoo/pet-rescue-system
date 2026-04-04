package com.pet.rescue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pet.rescue.entity.PetCategory;

import java.util.List;

public interface PetCategoryService extends IService<PetCategory> {

    /** 获取一级分类列表（按type过滤） */
    List<PetCategory> getRootCategories(String type);

    /** 获取子分类列表 */
    List<PetCategory> getChildren(Long parentId);

    /** 添加分类 */
    boolean addCategory(PetCategory category);

    /** 更新分类 */
    boolean updateCategory(PetCategory category);

    /** 删除分类（需无子分类且无关联宠物） */
    boolean deleteCategory(Long id);
}
