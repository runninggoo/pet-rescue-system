package com.pet.rescue.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pet.rescue.entity.PetFavorite;
import com.pet.rescue.security.UserContext;
import com.pet.rescue.service.PetFavoriteService;
import com.pet.rescue.vo.ResponseResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favorite")
public class PetFavoriteController {

    private final PetFavoriteService petFavoriteService;

    public PetFavoriteController(PetFavoriteService petFavoriteService) {
        this.petFavoriteService = petFavoriteService;
    }

    /**
     * 添加收藏
     */
    @PostMapping("/add")
    public ResponseResult addFavorite(@RequestParam Long petId) {
        try {
            Long userId = UserContext.getCurrentUserId();
            boolean success = petFavoriteService.addFavorite(userId, petId);
            if (success) {
                return ResponseResult.ok("收藏成功");
            } else {
                return ResponseResult.error("已收藏过该宠物");
            }
        } catch (RuntimeException e) {
            return ResponseResult.error(e.getMessage());
        } catch (Exception e) {
            return ResponseResult.error("收藏失败：" + e.getMessage());
        }
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/remove")
    public ResponseResult removeFavorite(@RequestParam Long petId) {
        try {
            Long userId = UserContext.getCurrentUserId();
            boolean success = petFavoriteService.removeFavorite(userId, petId);
            if (success) {
                return ResponseResult.ok("取消收藏成功");
            } else {
                return ResponseResult.error("未收藏该宠物");
            }
        } catch (Exception e) {
            return ResponseResult.error("取消收藏失败：" + e.getMessage());
        }
    }

    /**
     * 检查是否已收藏
     */
    @GetMapping("/check")
    public ResponseResult checkFavorite(@RequestParam Long petId) {
        try {
            Long userId = UserContext.getCurrentUserId();
            boolean favorited = petFavoriteService.isFavorited(userId, petId);
            return ResponseResult.ok().data("favorited", favorited);
        } catch (Exception e) {
            return ResponseResult.error("检查收藏状态失败：" + e.getMessage());
        }
    }

    /**
     * 获取我的收藏列表（分页）
     */
    @GetMapping("/list")
    public ResponseResult listMyFavorites(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Long userId = UserContext.getCurrentUserId();
            Page<PetFavorite> pageResult = petFavoriteService.getUserFavorites(userId, page, pageSize);
            Map<String, Object> data = new HashMap<>();
            data.put("favorites", pageResult.getRecords());
            data.put("total", pageResult.getTotal());
            data.put("pages", pageResult.getPages());
            data.put("current", pageResult.getCurrent());
            data.put("size", pageResult.getSize());
            return ResponseResult.ok().data(data);
        } catch (Exception e) {
            return ResponseResult.error("获取收藏列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取我的收藏的所有宠物ID（用于前端宠物列表页标记收藏状态）
     */
    @GetMapping("/pet-ids")
    public ResponseResult getMyFavoritePetIds() {
        try {
            Long userId = UserContext.getCurrentUserId();
            List<Long> petIds = petFavoriteService.getUserFavoritePetIds(userId);
            return ResponseResult.ok().data("petIds", petIds);
        } catch (Exception e) {
            return ResponseResult.error("获取收藏宠物ID失败：" + e.getMessage());
        }
    }
}
