package com.pet.rescue.controller;

import com.pet.rescue.entity.PetCategory;
import com.pet.rescue.service.PetCategoryService;
import com.pet.rescue.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class PetCategoryController {

    private final PetCategoryService petCategoryService;

    public PetCategoryController(PetCategoryService petCategoryService) {
        this.petCategoryService = petCategoryService;
    }

    /**
     * 获取一级分类列表（按type过滤，如CATS、DOGS）
     */
    @GetMapping("/list")
    public ResponseResult list(@RequestParam(required = false) String type) {
        try {
            List<PetCategory> categories = petCategoryService.getRootCategories(type);
            return ResponseResult.ok().data("categories", categories);
        } catch (Exception e) {
            return ResponseResult.error("获取分类失败：" + e.getMessage());
        }
    }

    /**
     * 获取子分类列表
     */
    @GetMapping("/children/{parentId}")
    public ResponseResult children(@PathVariable Long parentId) {
        try {
            List<PetCategory> children = petCategoryService.getChildren(parentId);
            return ResponseResult.ok().data("children", children);
        } catch (Exception e) {
            return ResponseResult.error("获取子分类失败：" + e.getMessage());
        }
    }

    /**
     * 添加分类（仅管理员）
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult add(@RequestBody PetCategory category) {
        try {
            boolean success = petCategoryService.addCategory(category);
            if (success) {
                return ResponseResult.ok("添加分类成功");
            } else {
                return ResponseResult.error("添加分类失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("添加分类失败：" + e.getMessage());
        }
    }

    /**
     * 更新分类（仅管理员）
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult update(@RequestBody PetCategory category) {
        try {
            boolean success = petCategoryService.updateCategory(category);
            if (success) {
                return ResponseResult.ok("更新分类成功");
            } else {
                return ResponseResult.error("更新分类失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新分类失败：" + e.getMessage());
        }
    }

    /**
     * 删除分类（仅管理员）
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult delete(@PathVariable Long id) {
        try {
            boolean success = petCategoryService.deleteCategory(id);
            if (success) {
                return ResponseResult.ok("删除分类成功");
            } else {
                return ResponseResult.error("删除分类失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("删除分类失败：" + e.getMessage());
        }
    }
}
