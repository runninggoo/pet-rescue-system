package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet.rescue.entity.HealthRecord;
import com.pet.rescue.entity.Pet;
import com.pet.rescue.mapper.HealthRecordMapper;
import com.pet.rescue.mapper.PetMapper;
import com.pet.rescue.service.HealthRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class HealthRecordServiceImpl extends ServiceImpl<HealthRecordMapper, HealthRecord> implements HealthRecordService {

    private final HealthRecordMapper healthRecordMapper;
    private final PetMapper petMapper;

    public HealthRecordServiceImpl(HealthRecordMapper healthRecordMapper, PetMapper petMapper) {
        this.healthRecordMapper = healthRecordMapper;
        this.petMapper = petMapper;
    }

    @Override
    public List<HealthRecord> findByCondition(Map<String, Object> params) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HealthRecord> wrapper =
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();

        if (params != null) {
            if (params.containsKey("petId") && params.get("petId") != null) {
                wrapper.eq(HealthRecord::getPetId, params.get("petId"));
            }
            if (params.containsKey("recordType") && params.get("recordType") != null
                    && !params.get("recordType").toString().isEmpty()) {
                wrapper.eq(HealthRecord::getRecordType, params.get("recordType"));
            }
            if (params.containsKey("keyword") && params.get("keyword") != null) {
                String kw = params.get("keyword").toString().trim();
                if (!kw.isEmpty()) {
                    wrapper.and(w -> w
                        .like(HealthRecord::getTitle, kw)
                        .or()
                        .like(HealthRecord::getContent, kw)
                        .or()
                        .like(HealthRecord::getInstitution, kw)
                    );
                }
            }
            if (params.containsKey("status") && params.get("status") != null) {
                wrapper.eq(HealthRecord::getStatus, params.get("status"));
            }
        }

        wrapper.orderByDesc(HealthRecord::getRecordDate);
        List<HealthRecord> records = baseMapper.selectList(wrapper);

        // 补充宠物名称
        Map<Long, String> petNameMap = new HashMap<>();
        for (HealthRecord r : records) {
            if (r.getPetId() != null) {
                petNameMap.putIfAbsent(r.getPetId(),
                    Optional.ofNullable(petMapper.selectById(r.getPetId()))
                        .map(Pet::getName)
                        .orElse("未知宠物"));
            }
        }
        for (HealthRecord r : records) {
            if (r.getPetId() != null) {
                r.setPetName(petNameMap.get(r.getPetId()));
            }
        }
        return records;
    }

    @Override
    public List<HealthRecord> findByPetId(Long petId) {
        List<HealthRecord> records = baseMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HealthRecord>()
                .eq(HealthRecord::getPetId, petId)
                .orderByDesc(HealthRecord::getRecordDate)
        );
        if (!records.isEmpty()) {
            Pet pet = petMapper.selectById(petId);
            String petName = pet != null ? pet.getName() : "未知宠物";
            for (HealthRecord r : records) {
                r.setPetName(petName);
            }
        }
        return records;
    }

    @Override
    public HealthRecord findDetail(Long recordId) {
        HealthRecord record = baseMapper.selectById(recordId);
        if (record != null && record.getPetId() != null) {
            Pet pet = petMapper.selectById(record.getPetId());
            if (pet != null) {
                record.setPetName(pet.getName());
                record.setPetBreed(pet.getBreed());
            }
        }
        return record;
    }

    @Override
    public boolean addRecord(HealthRecord record) {
        if (record.getStatus() == null) {
            record.setStatus(0);
        }
        return baseMapper.insert(record) > 0;
    }

    @Override
    public boolean updateRecord(HealthRecord record) {
        return baseMapper.updateById(record) > 0;
    }

    @Override
    public boolean deleteRecord(Long recordId) {
        return baseMapper.deleteById(recordId) > 0;
    }

    @Override
    public List<HealthRecord> findUpcomingReminders(int days) {
        java.util.Date today = new java.util.Date();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(today);
        cal.add(java.util.Calendar.DAY_OF_MONTH, days);
        java.util.Date endDate = cal.getTime();

        return baseMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HealthRecord>()
                .isNotNull(HealthRecord::getNextReminderDate)
                .ge(HealthRecord::getNextReminderDate, today)
                .le(HealthRecord::getNextReminderDate, endDate)
                .eq(HealthRecord::getStatus, 0)
                .orderByAsc(HealthRecord::getNextReminderDate)
        );
    }

    @Override
    public Map<String, Object> getStats(Long petId) {
        Map<String, Object> stats = new HashMap<>();

        if (petId != null) {
            long total = baseMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HealthRecord>()
                    .eq(HealthRecord::getPetId, petId)
            );
            long vaccines = baseMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HealthRecord>()
                    .eq(HealthRecord::getPetId, petId)
                    .eq(HealthRecord::getRecordType, "vaccine")
            );
            long checkups = baseMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HealthRecord>()
                    .eq(HealthRecord::getPetId, petId)
                    .eq(HealthRecord::getRecordType, "checkup")
            );
            double totalCost = baseMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HealthRecord>()
                    .eq(HealthRecord::getPetId, petId)
                    .isNotNull(HealthRecord::getCost)
            ).stream()
                .filter(r -> r.getCost() != null)
                .mapToDouble(HealthRecord::getCost)
                .sum();

            stats.put("totalRecords", total);
            stats.put("vaccineCount", vaccines);
            stats.put("checkupCount", checkups);
            stats.put("totalCost", totalCost);
        }

        return stats;
    }
}
