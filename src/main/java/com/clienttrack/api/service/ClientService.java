package com.clienttrack.api.service;

import com.clienttrack.api.controller.dto.ClientData;
import com.clienttrack.api.core.Client;
import com.clienttrack.api.repository.ClientRepository;
import com.clienttrack.api.repository.entity.ClientEntity;
import com.clienttrack.api.repository.mapper.ClientEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientEntityMapper clientEntityMapper;

    public List<Client> getAll() {
        return clientRepository.findAll()
                .stream()
                .map(clientEntityMapper::toCore)
                .toList();
    }

    public Client getById(UUID id) {
        return clientRepository.findById(id)
                .map(clientEntityMapper::toCore)
                .orElseThrow(() -> new RuntimeException("Client with id %s not found".formatted(id)));
    }

    public Client create(ClientData payloadRequest) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntityMapper.updateEntity(payloadRequest, clientEntity);
        ClientEntity savedEntity = clientRepository.save(clientEntity);
        return clientEntityMapper.toCore(savedEntity);
    }

    public Client update(UUID id, ClientData payloadRequest) {
        ClientEntity clientEntity = clientRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Client with id %s not found".formatted(id)));
        clientEntityMapper.updateEntity(payloadRequest, clientEntity);
        ClientEntity savedEntity = clientRepository.save(clientEntity);
        return clientEntityMapper.toCore(savedEntity);
    }

    public void delete(UUID id) {
        ClientEntity entity = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client with id %s not found".formatted(id)));
        clientRepository.delete(entity);
    }
}
