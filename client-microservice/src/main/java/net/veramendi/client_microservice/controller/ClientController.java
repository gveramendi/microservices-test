package net.veramendi.client_microservice.controller;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import net.veramendi.client_microservice.controller.dto.*;
import net.veramendi.client_microservice.domain.Client;
import net.veramendi.client_microservice.service.ClientService;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    private final ModelMapper modelMapper;

    @PostMapping()
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody CreateClientRequest createClientRequest) {
        Client client = this.modelMapper.map(createClientRequest, Client.class);
        ClientResponse clientResponse = this.modelMapper.map(
                this.clientService.create(client), ClientResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(clientResponse);
    }

    @GetMapping
    public ResponseEntity<PaginationDto> getAllByKeyword(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") List<String> sortList,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortOrder,
            @RequestParam(defaultValue = "")  String keyword) {
        Pageable pageable = pageable(page, size, sortList, sortOrder);
        Page<Client> users = this.clientService.getAll(pageable);
        PaginationDto paginationDto = PaginationDto.builder()
                .page(PageDto.builder()
                        .size(users.getSize())
                        .totalElements(users.getTotalElements())
                        .totalPages(users.getTotalPages())
                        .pageNumber(users.getNumber())
                        .orderBy(sortList.getFirst())
                        .orderDir(sortOrder.toString())
                        .keyword(keyword)
                        .build())
                .content(this.modelMapper.map(users.getContent(), new TypeToken<List<ClientResponse>>() {}.getType()))
                .build();

        return ResponseEntity.ok(paginationDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getById(@PathVariable Long id) {
        Client client = this.clientService.getById(id);
        ClientResponse clientResponse = this.modelMapper.map(client, ClientResponse.class);

        return ResponseEntity.ok(clientResponse);
    }

    @GetMapping("/client-id/{clientId}")
    public ResponseEntity<ClientResponse> getById(@PathVariable String clientId) {
        Client client = this.clientService.getByClientId(clientId);
        ClientResponse clientResponse = this.modelMapper.map(client, ClientResponse.class);

        return ResponseEntity.ok(clientResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody UpdateClientRequest updateClientRequest) {
        Client client = this.modelMapper.map(updateClientRequest, Client.class);
        ClientResponse clientResponse = this.modelMapper.map(
                this.clientService.update(id, client), ClientResponse.class);

        return ResponseEntity.ok(clientResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClientResponse> patch(@PathVariable Long id,
                                                @Valid @RequestBody ChangeStatusClientRequest changeStatusClientRequest) {
        Client client = this.modelMapper.map(changeStatusClientRequest, Client.class);
        ClientResponse clientResponse = this.modelMapper.map(
                this.clientService.updateStatus(id, client), ClientResponse.class);

        return ResponseEntity.ok(clientResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.clientService.delete(id);

        return ResponseEntity.noContent().build();
    }

    private Pageable pageable(int page, int size, List<String> sortList, Sort.Direction sortDirection) {
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        for (String sort : sortList) {
            if (sortDirection != null) {
                direction = Sort.Direction.fromString(sortDirection.toString());
            } else {
                direction = Sort.Direction.DESC;
            }
            sorts.add(new Sort.Order(direction, sort));
        }

        return PageRequest.of(page, size, Sort.by(sorts));
    }
}
