package com.clienttrack.api.controller;

import com.clienttrack.api.controller.dto.*;
import com.clienttrack.api.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deals")
@RequiredArgsConstructor
public class DealsController {

    private final DealService dealService;

    @GetMapping
    public List<DealDto> getAllDeals() {
        return dealService.getAll()
                .stream()
                .map(DealDto::from)
                .toList();
    }

    @GetMapping("/{id}")
    public DealDto getDealById(@PathVariable UUID id) {
        return DealDto.from(dealService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DealDto createDeal(@RequestBody DealData req) {
        return DealDto.from(dealService.create(req));
    }

    @PatchMapping("/{id}")
    public DealDto updateDeal(@PathVariable UUID id, @RequestBody DealPatchData req) {
        return DealDto.from(dealService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeal(@PathVariable UUID id) {
        dealService.delete(id);
    }
}
