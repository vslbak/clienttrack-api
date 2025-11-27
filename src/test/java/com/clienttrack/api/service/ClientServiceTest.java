package com.clienttrack.api.service;

import com.clienttrack.api.controller.dto.ClientData;
import com.clienttrack.api.repository.ClientRepository;
import com.clienttrack.api.repository.entity.ClientEntity;
import com.clienttrack.api.repository.mapper.ClientEntityMapper;
import com.clienttrack.api.core.Client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    private ClientService clientService;

    @BeforeEach
    void setUp() {
        ClientEntityMapper mapper = Mappers.getMapper(ClientEntityMapper.class);
        clientService = new ClientService(clientRepository, mapper);
    }

    // ---------------------------------------------------------
    // GET ALL
    // ---------------------------------------------------------

    @Test
    void getAll_returnsMappedClients() {
        ClientEntity e1 = new ClientEntity();
        e1.setId(UUID.randomUUID());
        e1.setName("John");

        ClientEntity e2 = new ClientEntity();
        e2.setId(UUID.randomUUID());
        e2.setName("Mary");

        when(clientRepository.findAll()).thenReturn(List.of(e1, e2));

        List<Client> result = clientService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("John");
        assertThat(result.get(1).getName()).isEqualTo("Mary");
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------

    @Test
    void getById_whenFound_returnsMappedClient() {
        UUID id = UUID.randomUUID();
        ClientEntity entity = new ClientEntity();
        entity.setId(id);
        entity.setName("John");

        when(clientRepository.findById(id)).thenReturn(Optional.of(entity));

        Client result = clientService.getById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("John");
    }

    @Test
    void getById_whenNotFound_throwsException() {
        UUID id = UUID.randomUUID();
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.getById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client with id");
    }

    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------

    @Test
    void create_savesEntityAndReturnsMappedClient() {
        var payload = new ClientData(
                "John",
                "ACME",
                "john@example.com",
                "555-1234",
                "Athens",
                "Some notes"
        );

        ArgumentCaptor<ClientEntity> captor = ArgumentCaptor.forClass(ClientEntity.class);

        ClientEntity saved = new ClientEntity();
        saved.setId(UUID.randomUUID());
        saved.setName("John");
        saved.setCompany("ACME");

        when(clientRepository.save(any(ClientEntity.class))).thenReturn(saved);

        Client result = clientService.create(payload);

        verify(clientRepository).save(captor.capture());

        ClientEntity passedEntity = captor.getValue();
        assertThat(passedEntity.getName()).isEqualTo("John");
        assertThat(passedEntity.getCompany()).isEqualTo("ACME");
        assertThat(passedEntity.getCreatedAt()).isNotNull();

        assertThat(result.getName()).isEqualTo("John");
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------

    @Test
    void update_whenFound_updatesAndReturnsMappedClient() {
        UUID id = UUID.randomUUID();

        ClientEntity existing = new ClientEntity();
        existing.setId(id);
        existing.setName("Old Name");

        when(clientRepository.findById(id)).thenReturn(Optional.of(existing));

        var payload = new ClientData(
                "New Name",
                "ACME",
                "new@example.com",
                "555-7777",
                "Athens",
                "Updated notes"
        );

        ClientEntity saved = new ClientEntity();
        saved.setId(id);
        saved.setName("New Name");
        saved.setCompany("ACME");

        when(clientRepository.save(any(ClientEntity.class))).thenReturn(saved);

        Client result = clientService.update(id, payload);

        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getCompany()).isEqualTo("ACME");
    }

    @Test
    void update_whenNotFound_throwsException() {
        UUID id = UUID.randomUUID();
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        var payload = new ClientData(
                "Whatever",
                "ACME",
                "x@example.com",
                "555-0000",
                "Saloniki",
                "Nothing"
        );

        assertThatThrownBy(() -> clientService.update(id, payload))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client with id");
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------

    @Test
    void delete_whenFound_deletesEntity() {
        UUID id = UUID.randomUUID();

        ClientEntity entity = new ClientEntity();
        entity.setId(id);

        when(clientRepository.findById(id)).thenReturn(Optional.of(entity));

        clientService.delete(id);

        verify(clientRepository).delete(entity);
    }

    @Test
    void delete_whenNotFound_throwsException() {
        UUID id = UUID.randomUUID();
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.delete(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Client with id");
    }
}
