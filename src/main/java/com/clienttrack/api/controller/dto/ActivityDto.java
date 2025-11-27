package com.clienttrack.api.controller.dto;

import com.clienttrack.api.core.ActivityType;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

public record ActivityDto(
    @NonNull UUID id,
    @NonNull ActivityType type,
    @NonNull String title,
    @NonNull String description,
    boolean completed,
    @NonNull DealDto deal,
    @NonNull Instant createdAt,
    @NonNull Instant updatedAt) {
    public static ActivityDto from(com.clienttrack.api.core.Activity activity) {
        return new ActivityDto(
            activity.getId(),
            activity.getType(),
            activity.getTitle(),
            activity.getDescription(),
            activity.isCompleted(),
            DealDto.from(activity.getDeal()),
            activity.getCreatedAt(),
            activity.getUpdatedAt()
        );
    }
}
