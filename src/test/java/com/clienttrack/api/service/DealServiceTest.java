package com.clienttrack.api.service;

import com.clienttrack.api.controller.dto.DealData;
import com.clienttrack.api.controller.dto.DealPatchData;
import com.clienttrack.api.core.Deal;
import com.clienttrack.api.core.DealStage;
import com.clienttrack.api.repository.ClientRepository;
import com.clienttrack.api.repository.DealRepository;
import com.clienttrack.api.repository.entity.ClientEntity;
import com.clienttrack.api.repository.entity.DealEntity;
import com.clienttrack.api.repository.mapper.DealEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class DealServiceTest {

    private DealRepository dealRepository;
    private ClientRepository clientRepository;
    private DealEntityMapper mapper;
    private DealService service;

    @BeforeEach
    void setup() {
        dealRepository = mock(DealRepository.class);
        clientRepository = mock(ClientRepository.class);
        mapper = mock(DealEntityMapper.class);
        service = new DealService(dealRepository, clientRepository, mapper);
    }

    @Test
    void getAll_shouldReturnMappedDeals() {
        DealEntity e = new DealEntity();
        Deal core = new Deal(UUID.randomUUID(), null, "T", DealStage.LEAD, new BigDecimal(100), 50, null, Instant.now(), Instant.now());

        when(dealRepository.findAll()).thenReturn(List.of(e));
        when(mapper.toCore(e)).thenReturn(core);

        List<Deal> result = service.getAll();

        assertThat(result).containsExactly(core);
    }

    @Test
    void getById_shouldReturnMappedDeal() {
        UUID id = UUID.randomUUID();
        DealEntity e = new DealEntity();
        Deal core = new Deal(UUID.randomUUID(), null, "T", DealStage.LEAD, new BigDecimal(100), 50, null, Instant.now(), Instant.now());

        when(dealRepository.findById(id)).thenReturn(Optional.of(e));
        when(mapper.toCore(e)).thenReturn(core);

        Deal result = service.getById(id);

        assertThat(result).isEqualTo(core);
    }

    @Test
    void create_shouldSaveAndReturnMappedDeal() {
        UUID clientId = UUID.randomUUID();
        ClientEntity client = new ClientEntity();
        DealEntity saved = new DealEntity();
        Deal core = new Deal(UUID.randomUUID(), null, "T", DealStage.LEAD, new BigDecimal(100), 50, null, Instant.now(), Instant.now());

        DealData data = new DealData(clientId, "T", DealStage.LEAD, new BigDecimal(100), 50, null);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(dealRepository.save(any())).thenReturn(saved);
        when(mapper.toCore(saved)).thenReturn(core);

        Deal result = service.create(data);

        // Verify repo call with correct constructed entity
        ArgumentCaptor<DealEntity> captor = ArgumentCaptor.forClass(DealEntity.class);
        verify(dealRepository).save(captor.capture());

        DealEntity passed = captor.getValue();
        assertThat(passed.getTitle()).isEqualTo("T");
        assertThat(passed.getClient()).isEqualTo(client);

        assertThat(result).isEqualTo(core);
    }

    @Test
    void update_shouldPatchAndSave() {
        UUID id = UUID.randomUUID();
        DealEntity existing = new DealEntity();
        DealPatchData patch = new DealPatchData(DealStage.PROPOSAL, null, null, null);
        DealEntity saved = new DealEntity();
        Deal core = new Deal(id, null, "updated", DealStage.LEAD, new BigDecimal(200), 60, null, Instant.now(), Instant.now());

        when(dealRepository.findById(id)).thenReturn(Optional.of(existing));
        when(dealRepository.save(existing)).thenReturn(saved);
        when(mapper.toCore(saved)).thenReturn(core);

        Deal result = service.update(id, patch);

        verify(mapper).patchEntity(patch, existing);
        verify(dealRepository).save(existing);

        assertThat(result).isEqualTo(core);
    }

    @Test
    void delete_shouldRemoveEntity() {
        UUID id = UUID.randomUUID();
        DealEntity existing = new DealEntity();

        when(dealRepository.findById(id)).thenReturn(Optional.of(existing));

        service.delete(id);

        verify(dealRepository).delete(existing);
    }
}
