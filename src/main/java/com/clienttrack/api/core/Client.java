package com.clienttrack.api.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Client {
    private UUID id;
    private String name;
    private String company;
    private String industry;
    private String email;
    private String phone;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;
}
