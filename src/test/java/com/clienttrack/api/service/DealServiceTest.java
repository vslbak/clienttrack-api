package com.clienttrack.api.service;

import com.clienttrack.api.controller.dto.DealData;
import com.clienttrack.api.core.Deal;
import com.clienttrack.api.repository.DealRepository;
import com.clienttrack.api.repository.entity.DealEntity;
import com.clienttrack.api.repository.mapper.DealEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DealServiceTest {

    private DealRepository dealRepository;
    private DealEntityMapper dealEntityMapper;
    private DealService dealService;

    @BeforeEach
    void setUp() {
        dealRepository = mock(DealRepository.class);
        dealEntityMapper = mock(DealEntityMapper.class);
        dealService = new DealService(dealRepository, dealEntityMapper);
    }

    @Test
    void getAll_returnsMappedDeals() {
        DealEntity entity = new DealEntity();
        Deal coreDeal = mock(Deal.class);

        when(dealRepository.findAll()).thenReturn(List.of(entity));
        when(dealEntityMapper.toCore(entity)).thenReturn(coreDeal);

        List<Deal> result = dealService.getAll();

        assertEquals(1, result.size());
        assertEquals(coreDeal, result.getFirst());
    }

    @Test
    void getById_returnsMappedDeal() {
        UUID id = UUID.randomUUID();
        DealEntity entity = new DealEntity();
        Deal coreDeal = mock(Deal.class);

        when(dealRepository.findById(id)).thenReturn(Optional.of(entity));
        when(dealEntityMapper.toCore(entity)).thenReturn(coreDeal);

        Deal result = dealService.getById(id);

        assertEquals(coreDeal, result);
    }

    @Test
    void getById_throwsWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(dealRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> dealService.getById(id));

        assertTrue(ex.getMessage().contains("Deal with id"));
    }

    @Test
    void create_mapsUpdatesAndSaves() {
        DealData payload = mock(DealData.class);

        DealEntity savedEntity = new DealEntity();
        Deal mappedCore = mock(Deal.class);

        when(dealRepository.save(any())).thenReturn(savedEntity);
        when(dealEntityMapper.toCore(savedEntity)).thenReturn(mappedCore);

        Deal result = dealService.create(payload);

        // verify mapstruct updateEntity was called
        verify(dealEntityMapper).updateEntity(eq(payload), any(DealEntity.class));

        // verify savedEntity was mapped back to core
        assertEquals(mappedCore, result);
    }

    @Test
    void update_mapsUpdatesAndSaves() {
        UUID id = UUID.randomUUID();
        DealData payload = mock(DealData.class);

        DealEntity existing = new DealEntity();
        DealEntity saved = new DealEntity();
        Deal mappedCore = mock(Deal.class);

        when(dealRepository.findById(id)).thenReturn(Optional.of(existing));
        when(dealRepository.save(existing)).thenReturn(saved);
        when(dealEntityMapper.toCore(saved)).thenReturn(mappedCore);

        Deal result = dealService.update(id, payload);

        verify(dealEntityMapper).updateEntity(payload, existing);
        verify(dealRepository).save(existing);
        assertEquals(mappedCore, result);
    }

    @Test
    void update_throwsWhenNotFound() {
        UUID id = UUID.randomUUID();
        DealData payload = mock(DealData.class);

        when(dealRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> dealService.update(id, payload));

        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void delete_deletesWhenFound() {
        UUID id = UUID.randomUUID();
        DealEntity existing = new DealEntity();

        when(dealRepository.findById(id)).thenReturn(Optional.of(existing));

        dealService.delete(id);

        verify(dealRepository).delete(existing);
    }

    @Test
    void delete_throwsWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(dealRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> dealService.delete(id));

        assertTrue(ex.getMessage().contains("not found"));
    }
}
