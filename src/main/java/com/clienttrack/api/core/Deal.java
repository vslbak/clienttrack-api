package com.clienttrack.api.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Deal {
    private UUID id;
    private Client client;
    private String title;
    private DealStage stage;
    private BigDecimal value;
    private Integer probability;
    private LocalDate expectedCloseDate;
    private Instant createdAt;
    private Instant updatedAt;
}
