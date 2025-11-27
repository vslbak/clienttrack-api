package com.clienttrack.api.controller;

import com.clienttrack.api.controller.dto.ProposalData;
import com.clienttrack.api.controller.dto.ProposalDto;
import com.clienttrack.api.core.ProposalStatus;
import com.clienttrack.api.service.ProposalService;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/proposals")
@RequiredArgsConstructor
public class ProposalsController {

    private final ProposalService proposalService;

    @GetMapping
    public List<ProposalDto> getAllProposals() {
        return proposalService.getAll()
                .stream()
                .map(ProposalDto::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ProposalDto getProposal(@PathVariable UUID id) {
        return ProposalDto.from(proposalService.getById(id));
    }

    @PostMapping
    public ProposalDto createProposal(@RequestBody ProposalData req) {
        return ProposalDto.from(proposalService.create(req));
    }

    @PostMapping("/{id}/status")
    public void updateProposalStatus(@PathVariable UUID id, @RequestBody ProposalStatus proposalStatus) {
        proposalService.updateStatus(id, proposalStatus);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable UUID id) {
        byte[] pdf = proposalService.generatePdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=proposal-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}
