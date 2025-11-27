package com.clienttrack.api.service;

import com.clienttrack.api.controller.dto.DealData;
import com.clienttrack.api.controller.dto.DealPatchData;
import com.clienttrack.api.core.Deal;
import com.clienttrack.api.repository.ClientRepository;
import com.clienttrack.api.repository.DealRepository;
import com.clienttrack.api.repository.entity.ClientEntity;
import com.clienttrack.api.repository.entity.DealEntity;
import com.clienttrack.api.repository.mapper.DealEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealRepository;
    private final ClientRepository clientRepository;
    private final DealEntityMapper dealEntityMapper;

    public List<Deal> getAll() {
        return dealRepository.findAll()
                .stream()
                .map(dealEntityMapper::toCore)
                .toList();
    }

    public Deal getById(UUID id) {
        return dealRepository.findById(id)
                .map(dealEntityMapper::toCore)
                .orElseThrow(() -> new RuntimeException("Deal with id %s not found".formatted(id)));
    }

    @Transactional
    public Deal create(DealData payloadRequest) {
        ClientEntity clientEntity = clientRepository.findById(payloadRequest.clientId())
                .orElseThrow(() -> new RuntimeException("Client with id %s not found".formatted(payloadRequest.clientId())));

        DealEntity dealEntity = new DealEntity(clientEntity, payloadRequest.title(), payloadRequest.stage(), payloadRequest.value(), payloadRequest.probability(), null);
        DealEntity savedEntity = dealRepository.save(dealEntity);
        return dealEntityMapper.toCore(savedEntity);
    }

    @Transactional
    public Deal update(UUID id, DealPatchData payloadRequest) {
        DealEntity dealEntity = dealRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Deal with id %s not found".formatted(id)));
        dealEntityMapper.patchEntity(payloadRequest, dealEntity);
        DealEntity savedEntity = dealRepository.save(dealEntity);
        return dealEntityMapper.toCore(savedEntity);
    }

    @Transactional
    public void delete(UUID id) {
        DealEntity entity = dealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deal with id %s not found".formatted(id)));
        dealRepository.delete(entity);
    }
}
