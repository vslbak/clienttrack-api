package com.clienttrack.api.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Proposal {
    private UUID id;
    private String title;
    private String description;
    private ProposalStatus status;
    private Deal deal;
    private List<ProposalPricingItem> pricingItems;
    private Instant createdAt;
    private Instant updatedAt;
}
