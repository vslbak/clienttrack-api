package com.clienttrack.api.controller.dto;

import com.clienttrack.api.core.Deal;
import com.clienttrack.api.core.DealStage;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record DealDto(
        @NonNull UUID id,
        @NonNull ClientDto client,
        @NonNull String title,
        @NonNull DealStage stage,
        @NonNull BigDecimal value,
        Integer probability,
        LocalDate expectedCloseDate,
        @NonNull Instant createdAt,
        @NonNull Instant updatedAt
) {

    public static DealDto from(Deal deal) {
        return new DealDto(
                deal.getId(),
                ClientDto.from(deal.getClient()),
                deal.getTitle(),
                deal.getStage(),
                deal.getValue(),
                deal.getProbability(),
                deal.getExpectedCloseDate(),
                deal.getCreatedAt(),
                deal.getUpdatedAt()
        );
    }
}
