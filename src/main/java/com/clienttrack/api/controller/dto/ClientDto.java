package com.clienttrack.api.controller.dto;

import com.clienttrack.api.core.Client;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

public record ClientDto (@NonNull UUID id, @NonNull String name, @NonNull String company, @NonNull String email,
                        @NonNull String phone, @NonNull String industry, @NonNull String notes,
                        @NonNull Instant createdAt) {
    public static ClientDto from(Client client) {
        return new ClientDto(
                client.getId(),
                client.getName(),
                client.getCompany(),
                client.getEmail(),
                client.getPhone(),
                client.getIndustry(),
                client.getNotes(),
                client.getCreatedAt()
        );
    }
}
