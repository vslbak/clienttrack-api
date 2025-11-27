package com.clienttrack.api.repository.entity;

import com.clienttrack.api.core.DealStage;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "deals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DealEntity {

    public DealEntity(ClientEntity client, String title, DealStage stage, BigDecimal value, Integer probability, LocalDate expectedCloseDate) {
        this.client = client;
        this.title = title;
        this.stage = stage;
        this.value = value;
        this.probability = probability;
        this.expectedCloseDate = expectedCloseDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DealStage stage;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal value;

    private Integer probability;

    @Column(name = "expected_close_date")
    private LocalDate expectedCloseDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;
}
