package net.veramendi.client_microservice.service;

import net.veramendi.client_microservice.domain.Client;
import net.veramendi.client_microservice.exception.ResourceAlreadyExistsException;
import net.veramendi.client_microservice.exception.ResourceNotFoundException;
import net.veramendi.client_microservice.respository.ClientRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createClientSuccessfully() {
        Client newClient = new Client();
        newClient.setClientId("0020010");
        when(clientRepository.findByClientId(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(clientRepository.save(any(Client.class))).thenReturn(newClient);

        Client createdClient = clientService.create(newClient);

        assertNotNull(createdClient);
        assertEquals("0020010", createdClient.getClientId());
    }

    @Test
    void createClientWithExistingClientIdThrowsException() {
        Client newClient = new Client();
        newClient.setClientId("0020010");
        when(clientRepository.findByClientId(anyString())).thenReturn(Optional.of(new Client()));

        assertThrows(ResourceAlreadyExistsException.class, () -> clientService.create(newClient));
    }

    @Test
    void getAllClientsSuccessfully() {
        Page<Client> clients = mock(Page.class);
        when(clientRepository.findAll(any(Pageable.class))).thenReturn(clients);

        Page<Client> result = clientService.getAll(Pageable.unpaged());

        assertEquals(clients, result);
    }

    @Test
    void getClientByIdSuccessfully() {
        Client client = new Client();
        client.setId(1L);
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));

        Client result = clientService.getById(1L);

        assertEquals(client, result);
    }

    @Test
    void getClientByIdNotFoundThrowsException() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.getById(1L));
    }

    @Test
    void getClientByClientIdSuccessfully() {
        Client client = new Client();
        client.setClientId("0020010");
        when(clientRepository.findByClientId(anyString())).thenReturn(Optional.of(client));

        Client result = clientService.getByClientId("0020010");

        assertEquals(client, result);
    }

    @Test
    void getClientByClientIdNotFoundThrowsException() {
        when(clientRepository.findByClientId(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.getByClientId("0020010"));
    }

    @Test
    void updateClientSuccessfully() {
        Client existingClient = new Client();
        existingClient.setId(1L);
        existingClient.setClientId("0020010");
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(existingClient));
        when(clientRepository.findByClientId(anyString())).thenReturn(Optional.empty());
        when(clientRepository.save(any(Client.class))).thenReturn(existingClient);

        Client updatedClient = clientService.update(1L, existingClient);

        assertEquals(existingClient, updatedClient);
    }

    @Test
    void updateClientWithExistingClientIdThrowsException() {
        Client existingClientById = new Client();
        existingClientById.setId(1L);
        existingClientById.setClientId("0020010");
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(existingClientById));

        Client existingClientByClientId = new Client();
        existingClientByClientId.setId(2L);
        existingClientByClientId.setClientId("0020010");
        when(clientRepository.findByClientId(anyString())).thenReturn(Optional.of(existingClientByClientId));

        assertThrows(ResourceAlreadyExistsException.class, () -> clientService.update(1L, existingClientById));
    }

    @Test
    void updateClientStatusSuccessfully() {
        Client existingClient = new Client();
        existingClient.setId(1L);
        existingClient.setIsActive(true);
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenReturn(existingClient);

        Client updatedClient = clientService.updateStatus(1L, existingClient);

        assertEquals(existingClient, updatedClient);
    }

    @Test
    void deleteClientSuccessfully() {
        Client existingClient = new Client();
        existingClient.setId(1L);
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(existingClient));
        doNothing().when(clientRepository).delete(any(Client.class));

        assertDoesNotThrow(() -> clientService.delete(1L));
    }

    @Test
    void deleteClientNotFoundThrowsException() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.delete(1L));
    }
}
