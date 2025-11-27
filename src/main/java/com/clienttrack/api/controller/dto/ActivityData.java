package com.clienttrack.api.controller.dto;

import com.clienttrack.api.core.ActivityType;
import com.clienttrack.api.core.DealStage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ActivityData(
        @JsonProperty UUID dealId,
        @NonNull String title,
        @NonNull ActivityType type,
        @NonNull String description) {}
