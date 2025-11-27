package com.clienttrack.api.controller.dto;

import com.clienttrack.api.core.DealStage;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record DealData(
        @NonNull UUID clientId,
        @NonNull String title,
        @NonNull DealStage stage,
        @NonNull BigDecimal value,
        @NonNull Integer probability,
        LocalDate expectedCloseDate
) {
}
