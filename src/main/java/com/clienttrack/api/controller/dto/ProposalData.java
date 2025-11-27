package com.clienttrack.api.controller.dto;

import com.clienttrack.api.core.ProposalPricingItem;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

public record ProposalData(
        @NonNull String title,
        @NonNull String description,
        @NonNull UUID dealId,
        @NonNull List<ProposalPricingItem> pricingItems
) {
}
