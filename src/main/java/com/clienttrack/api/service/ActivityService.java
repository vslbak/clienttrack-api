package com.clienttrack.api.service;

import com.clienttrack.api.controller.dto.ActivityData;
import com.clienttrack.api.core.Activity;
import com.clienttrack.api.repository.ActivityRepository;
import com.clienttrack.api.repository.ClientRepository;
import com.clienttrack.api.repository.DealRepository;
import com.clienttrack.api.repository.entity.ActivityEntity;
import com.clienttrack.api.repository.mapper.ActivityEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final DealRepository dealRepository;
    private final ActivityEntityMapper activityEntityMapper;

    public List<Activity> getAll() {
        return activityRepository.findAll()
                .stream()
                .map(activityEntityMapper::toCore)
                .toList();
    }

    public Activity create(ActivityData activityData) {
        var dealEntity = dealRepository.findById(activityData.dealId())
                .orElseThrow(() -> new RuntimeException("Deal with id %s not found".formatted(activityData.dealId())));

        var activityEntity = new ActivityEntity();
        activityEntityMapper.updateEntity(activityData, activityEntity);
        activityEntity.setDeal(dealEntity);
        var savedEntity = activityRepository.save(activityEntity);
        return activityEntityMapper.toCore(savedEntity);
    }

    public void setComplete(UUID id) {
        var activityEntity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity with id %s not found".formatted(id)));
        activityEntity.setCompleted(true);
        activityRepository.save(activityEntity);
    }
}
