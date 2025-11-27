package com.clienttrack.api.service;

import com.clienttrack.api.controller.dto.ActivityData;
import com.clienttrack.api.core.Activity;
import com.clienttrack.api.core.ActivityType;
import com.clienttrack.api.repository.ActivityRepository;
import com.clienttrack.api.repository.DealRepository;
import com.clienttrack.api.repository.entity.ActivityEntity;
import com.clienttrack.api.repository.entity.DealEntity;
import com.clienttrack.api.repository.mapper.ActivityEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ActivityServiceTest {

    private ActivityRepository activityRepository;
    private DealRepository dealRepository;
    private ActivityEntityMapper mapper;

    private ActivityService service;

    @BeforeEach
    void setup() {
        activityRepository = mock(ActivityRepository.class);
        dealRepository = mock(DealRepository.class);
        mapper = mock(ActivityEntityMapper.class);

        service = new ActivityService(activityRepository, dealRepository, mapper);
    }

    @Test
    void getAll_shouldReturnMappedActivities() {
        ActivityEntity e = new ActivityEntity();
        Activity core = new Activity(UUID.randomUUID(), ActivityType.NOTE, "note", "description", false, null, Instant.now(), Instant.now());

        when(activityRepository.findAll()).thenReturn(List.of(e));
        when(mapper.toCore(e)).thenReturn(core);

        List<Activity> result = service.getAll();

        assertThat(result).containsExactly(core);
    }

    @Test
    void create_shouldSaveMappedActivity() {
        UUID dealId = UUID.randomUUID();
        DealEntity deal = new DealEntity();
        ActivityEntity savedEntity = new ActivityEntity();
        Activity core = new Activity(UUID.randomUUID(), ActivityType.NOTE, "note", "description", false, null, Instant.now(), Instant.now());

        ActivityData data = new ActivityData(dealId, "call", ActivityType.NOTE, "Test note");

        when(dealRepository.findById(dealId)).thenReturn(Optional.of(deal));
        when(activityRepository.save(any())).thenReturn(savedEntity);
        when(mapper.toCore(savedEntity)).thenReturn(core);

        Activity result = service.create(data);

        ArgumentCaptor<ActivityEntity> captor = ArgumentCaptor.forClass(ActivityEntity.class);
        verify(activityRepository).save(captor.capture());

        ActivityEntity passed = captor.getValue();

        // mapper.updateEntity(data, passed) must have been called
        verify(mapper).updateEntity(data, passed);
        assertThat(passed.getDeal()).isEqualTo(deal);

        assertThat(result).isEqualTo(core);
    }

    @Test
    void setComplete_shouldMarkCompletedAndSave() {
        UUID id = UUID.randomUUID();
        ActivityEntity existing = new ActivityEntity();
        existing.setCompleted(false);

        when(activityRepository.findById(id)).thenReturn(Optional.of(existing));
        when(activityRepository.save(existing)).thenReturn(existing);

        service.setComplete(id);

        assertThat(existing.isCompleted()).isTrue();
        verify(activityRepository).save(existing);
    }
}
