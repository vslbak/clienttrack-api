package com.clienttrack.api.controller.dto;

import com.clienttrack.api.core.DealStage;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record DealPatchData(DealStage stage, BigDecimal value, Integer probability, LocalDate expectedCloseDate) {
}
