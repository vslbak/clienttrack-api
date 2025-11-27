package com.clienttrack.api.controller.dto;

import com.clienttrack.api.core.Proposal;
import com.clienttrack.api.core.ProposalPricingItem;
import com.clienttrack.api.core.ProposalStatus;
import lombok.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ProposalDto (
    @NonNull UUID id,
    @NonNull String title,
    @NonNull String description,
    @NonNull ProposalStatus status,
    @NonNull DealDto deal,
    @NonNull List<ProposalPricingItem> pricingItems,
    @NonNull Instant createdAt,
    @NonNull Instant updatedAt
) {
    public static ProposalDto from(Proposal proposal) {
        return new ProposalDto(
                proposal.getId(),
                proposal.getTitle(),
                proposal.getDescription(),
                proposal.getStatus(),
                DealDto.from(proposal.getDeal()),
                proposal.getPricingItems(),
                proposal.getCreatedAt(),
                proposal.getUpdatedAt()
        );
    }
}
