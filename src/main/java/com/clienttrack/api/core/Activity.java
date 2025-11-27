package com.clienttrack.api.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Activity {
    private UUID id;
    private ActivityType type;
    private String title;
    private String description;
    private boolean completed;
    private Client client;
    private Deal deal;
    private Instant createdAt;
    private Instant updatedAt;
}
