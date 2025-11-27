package com.clienttrack.api.service;

import com.clienttrack.api.controller.dto.ProposalData;
import com.clienttrack.api.core.Client;
import com.clienttrack.api.core.Proposal;
import com.clienttrack.api.core.ProposalStatus;
import com.clienttrack.api.repository.DealRepository;
import com.clienttrack.api.repository.ProposalRepository;
import com.clienttrack.api.repository.entity.DealEntity;
import com.clienttrack.api.repository.entity.ProposalEntity;
import com.clienttrack.api.repository.mapper.ProposalEntityMapper;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProposalService {

    private final DealRepository dealRepository;
    private final ProposalRepository proposalRepository;
    private final ProposalEntityMapper proposalEntityMapper;
    private final ProposalPdfService proposalPdfService;

    public List<Proposal> getAll() {
        return proposalRepository.findAll()
                .stream()
                .map(proposalEntityMapper::toCore)
                .toList();
    }

    public Proposal getById(UUID id) {
        ProposalEntity proposalEntity = proposalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proposal with id %s not found".formatted(id)));
        return proposalEntityMapper.toCore(proposalEntity);
    }

    @Transactional
    public Proposal create(ProposalData payloadRequest) {
        DealEntity dealEntity = dealRepository.findById(payloadRequest.dealId())
                .orElseThrow(() -> new RuntimeException("Deal with id %s not found".formatted(payloadRequest.dealId())));

        ProposalEntity proposalEntity = new ProposalEntity();
        proposalEntityMapper.updateEntity(payloadRequest, proposalEntity);
        proposalEntity.setDeal(dealEntity);
        proposalEntity.setStatus(ProposalStatus.PREPARED);
        ProposalEntity savedEntity = proposalRepository.save(proposalEntity);
        return proposalEntityMapper.toCore(savedEntity);
    }

    @Transactional
    public void updateStatus(UUID id, ProposalStatus status) {
        ProposalEntity proposalEntity = proposalRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Proposal with id %s not found".formatted(id)));
        proposalEntity.setStatus(status);
        proposalRepository.save(proposalEntity);
    }

    public byte[] generatePdf(UUID id) {
        ProposalEntity proposalEntity = proposalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proposal with id %s not found".formatted(id)));
        Proposal proposal = proposalEntityMapper.toCore(proposalEntity);
        return proposalPdfService.build(proposal);
    }

}
