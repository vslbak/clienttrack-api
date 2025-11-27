package com.clienttrack.api.service;

import com.clienttrack.api.controller.dto.ProposalData;
import com.clienttrack.api.core.Proposal;
import com.clienttrack.api.core.ProposalStatus;
import com.clienttrack.api.repository.DealRepository;
import com.clienttrack.api.repository.ProposalRepository;
import com.clienttrack.api.repository.entity.DealEntity;
import com.clienttrack.api.repository.entity.ProposalEntity;
import com.clienttrack.api.repository.mapper.ProposalEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProposalServiceTest {

    private DealRepository dealRepository;
    private ProposalRepository proposalRepository;
    private ProposalEntityMapper mapper;
    private ProposalPdfService pdfService;

    private ProposalService service;

    @BeforeEach
    void setup() {
        dealRepository = mock(DealRepository.class);
        proposalRepository = mock(ProposalRepository.class);
        mapper = mock(ProposalEntityMapper.class);
        pdfService = mock(ProposalPdfService.class);

        service = new ProposalService(
                dealRepository,
                proposalRepository,
                mapper,
                pdfService
        );
    }

    @Test
    void getAll_shouldReturnMappedProposals() {
        ProposalEntity e = new ProposalEntity();
        Proposal core = new Proposal(UUID.randomUUID(), "title", "description", ProposalStatus.PREPARED, null, List.of(), Instant.now(), Instant.now());

        when(proposalRepository.findAll()).thenReturn(List.of(e));
        when(mapper.toCore(e)).thenReturn(core);

        List<Proposal> result = service.getAll();

        assertThat(result).containsExactly(core);
    }

    @Test
    void getById_shouldReturnMappedProposal() {
        UUID id = UUID.randomUUID();
        ProposalEntity e = new ProposalEntity();
        Proposal core = new Proposal(UUID.randomUUID(), "title", "description", ProposalStatus.PREPARED, null, List.of(), Instant.now(), Instant.now());

        when(proposalRepository.findById(id)).thenReturn(Optional.of(e));
        when(mapper.toCore(e)).thenReturn(core);

        Proposal result = service.getById(id);

        assertThat(result).isEqualTo(core);
    }

    @Test
    void create_shouldSaveAndReturnMappedProposal() {
        UUID dealId = UUID.randomUUID();
        DealEntity deal = new DealEntity();
        ProposalEntity saved = new ProposalEntity();
        Proposal core = new Proposal(UUID.randomUUID(), "title", "description", ProposalStatus.PREPARED, null, List.of(), Instant.now(), Instant.now());

        ProposalData data = new ProposalData("Title", "Description", dealId, List.of());

        when(dealRepository.findById(dealId)).thenReturn(Optional.of(deal));
        when(proposalRepository.save(any())).thenReturn(saved);
        when(mapper.toCore(saved)).thenReturn(core);

        Proposal result = service.create(data);

        ArgumentCaptor<ProposalEntity> captor = ArgumentCaptor.forClass(ProposalEntity.class);
        verify(proposalRepository).save(captor.capture());

        ProposalEntity passed = captor.getValue();

        // ensure mapper updated entity
        verify(mapper).updateEntity(data, passed);
        assertThat(passed.getDeal()).isEqualTo(deal);
        assertThat(passed.getStatus()).isEqualTo(ProposalStatus.PREPARED);

        assertThat(result).isEqualTo(core);
    }

    @Test
    void updateStatus_shouldModifyAndSave() {
        UUID id = UUID.randomUUID();
        ProposalEntity existing = new ProposalEntity();

        when(proposalRepository.findById(id)).thenReturn(Optional.of(existing));

        service.updateStatus(id, ProposalStatus.SENT);

        assertThat(existing.getStatus()).isEqualTo(ProposalStatus.SENT);
        verify(proposalRepository).save(existing);
    }

    @Test
    void generatePdf_shouldLoadProposalAndDelegateToPdfService() {
        UUID id = UUID.randomUUID();
        ProposalEntity e = new ProposalEntity();
        Proposal core = new Proposal(UUID.randomUUID(), "title", "description", ProposalStatus.PREPARED, null, List.of(), Instant.now(), Instant.now());
        byte[] pdfBytes = new byte[]{1, 2, 3};

        when(proposalRepository.findById(id)).thenReturn(Optional.of(e));
        when(mapper.toCore(e)).thenReturn(core);
        when(pdfService.build(core)).thenReturn(pdfBytes);

        byte[] result = service.generatePdf(id);

        assertThat(result).isEqualTo(pdfBytes);
        verify(pdfService).build(core);
    }
}
