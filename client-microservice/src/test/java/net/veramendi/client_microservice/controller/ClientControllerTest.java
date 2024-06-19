package net.veramendi.client_microservice.controller;

import net.veramendi.client_microservice.controller.dto.ClientResponse;
import net.veramendi.client_microservice.controller.dto.CreateClientRequest;
import net.veramendi.client_microservice.controller.dto.PaginationDto;
import net.veramendi.client_microservice.domain.Client;
import net.veramendi.client_microservice.exception.ResourceAlreadyExistsException;
import net.veramendi.client_microservice.exception.ResourceNotFoundException;
import net.veramendi.client_microservice.service.ClientService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @Mock
    private ModelMapper modelMapper;

    @Captor
    private ArgumentCaptor<Client> clientArgumentCaptor;

    @InjectMocks
    private ClientController clientController;

    private CreateClientRequest createClientRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createClientReturnsCreatedResponse() {
        CreateClientRequest request = new CreateClientRequest();
        Client client = new Client();
        when(modelMapper.map(any(CreateClientRequest.class), any())).thenReturn(client);
        when(clientService.create(any(Client.class))).thenReturn(client);
        when(modelMapper.map(any(Client.class), any())).thenReturn(new ClientResponse());

        ResponseEntity<ClientResponse> response = clientController.create(request);

        assertEquals(201, response.getStatusCode().value());
    }

    @Test
    void createClientReturnsResourceAlreadyExistsException() {
        CreateClientRequest request = new CreateClientRequest();
        Client client = new Client();
        when(modelMapper.map(any(CreateClientRequest.class), any())).thenReturn(client);
        when(clientService.create(any(Client.class))).thenThrow(new ResourceAlreadyExistsException("Resource already exists"));

        assertThrows(ResourceAlreadyExistsException.class, () -> clientController.create(request));
    }

    @Test
    void getAllByKeywordReturnsCorrectResponse() {
        Page<Client> clients = mock(Page.class);
        when(clientService.getAll(any(Pageable.class))).thenReturn(clients);
        when(modelMapper.map(any(), any())).thenReturn(new ArrayList<>());

        ResponseEntity<PaginationDto> response = clientController.getAllByKeyword(0, 10, Collections.singletonList("id"), Sort.Direction.ASC, "");

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void getAllByKeywordReturnsCorrectResponseWithDifferentParameters() {
        Page<Client> clients = mock(Page.class);
        when(clientService.getAll(any(Pageable.class))).thenReturn(clients);
        when(modelMapper.map(any(), any())).thenReturn(new ArrayList<>());

        ResponseEntity<PaginationDto> response = clientController.getAllByKeyword(1, 5, Collections.singletonList("name"), Sort.Direction.DESC, "test");

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void getByIdReturnsCorrectResponse() {
        Client client = new Client();
        when(clientService.getById(anyLong())).thenReturn(client);
        when(modelMapper.map(any(Client.class), any())).thenReturn(new ClientResponse());

        ResponseEntity<ClientResponse> response = clientController.getById(1L);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void getByIdThrowsResourceNotFoundException() {
        when(clientService.getById(anyLong())).thenThrow(new ResourceNotFoundException("Resource not found"));

        assertThrows(ResourceNotFoundException.class, () -> clientController.getById(1L));
    }
}
