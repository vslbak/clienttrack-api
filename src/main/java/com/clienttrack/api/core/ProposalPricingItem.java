package com.clienttrack.api.core;

import lombok.NonNull;

import java.math.BigDecimal;

public record ProposalPricingItem (
        @NonNull String description,
        @NonNull Integer quantity,
        @NonNull BigDecimal price
) {}
