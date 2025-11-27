package com.clienttrack.api.repository;

import com.clienttrack.api.repository.entity.DealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DealRepository extends JpaRepository<DealEntity, UUID> {
}
