package com.clienttrack.api.controller;

import com.clienttrack.api.controller.dto.ClientDto;
import com.clienttrack.api.controller.dto.ClientData;
import com.clienttrack.api.core.Client;
import com.clienttrack.api.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<ClientDto> getAllClients() {
        return clientService.getAll()
                .stream()
                .map(ClientDto::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ClientDto getClient(@PathVariable UUID id) {
        Client client = clientService.getById(id);
        return ClientDto.from(client);
    }

    @PostMapping
    public ClientDto createClient(@RequestBody ClientData payloadRequest) {
        Client created = clientService.create(payloadRequest);
        return ClientDto.from(created);
    }

    @PutMapping("/{id}")
    public ClientDto updateClient(@PathVariable UUID id, @RequestBody ClientData payloadRequest) {
        Client updated = clientService.update(id, payloadRequest);
        return ClientDto.from(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable UUID id) {
        clientService.delete(id);
    }
}
