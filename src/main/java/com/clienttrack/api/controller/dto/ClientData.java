package com.clienttrack.api.controller.dto;

import lombok.NonNull;

public record ClientData(@NonNull String name, @NonNull String company, @NonNull String email,
                         @NonNull String phone, @NonNull String industry, @NonNull String notes) {
}
