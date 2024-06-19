package net.veramendi.client_microservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.veramendi.client_microservice.domain.Client;
import net.veramendi.client_microservice.exception.ResourceAlreadyExistsException;
import net.veramendi.client_microservice.exception.ResourceNotFoundException;
import net.veramendi.client_microservice.respository.ClientRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final PasswordEncoder passwordEncoder;

    public Client create(Client newClient) {
        Optional<Client> client = this.clientRepository.findByClientId(newClient.getClientId());
        if (client.isPresent()) {
            String message = "Client with clientId: %s already exists".formatted(newClient.getClientId());

            log.error(message);
            throw new ResourceAlreadyExistsException(message);
        }

        newClient.setIsActive(true);
        newClient.setPassword(this.passwordEncoder.encode(newClient.getPassword()));

        Client clientCreated = this.clientRepository.save(newClient);
        log.info("Client created: {}", clientCreated);

        return clientCreated;
    }

    public Page<Client> getAll(Pageable pageable) {
        return this.clientRepository.findAll(pageable);
    }

    public Client getById(Long id) {
        Optional<Client> client = this.clientRepository.findById(id);
        if (client.isEmpty()) {
            String message = "Client with id: %s not found".formatted(id);

            log.error(message);
            throw new ResourceNotFoundException(message);
        }

        return client.get();
    }

    public Client getByClientId(String clientId) {
        Optional<Client> client = this.clientRepository.findByClientId(clientId);
        if (client.isEmpty()) {
            String message = "Client with clientId: %s not found".formatted(clientId);

            log.error(message);
            throw new ResourceNotFoundException(message);
        }

        return client.get();
    }

    public Client update(Long id, Client clientModified) {
        Optional<Client> clientOptionalByClientId = this.clientRepository.findByClientId(clientModified.getClientId());
        if (clientOptionalByClientId.isPresent() && !clientOptionalByClientId.get().getId().equals(id)) {
            String message = "Client with clientId: %s already exists".formatted(clientModified.getClientId());

            log.error(message);
            throw new ResourceAlreadyExistsException(message);
        }

        Client clientToUpdate = this.getById(id);
        clientToUpdate.setFirstName(clientModified.getFirstName());
        clientToUpdate.setLastName(clientModified.getLastName());
        clientToUpdate.setGender(clientModified.getGender());
        clientToUpdate.setAge(clientModified.getAge());
        clientToUpdate.setIdNumber(clientModified.getIdNumber());
        clientToUpdate.setAddress(clientModified.getAddress());
        clientToUpdate.setPhoneNumber(clientModified.getPhoneNumber());
        clientToUpdate.setClientId(clientModified.getClientId());

        Client clientUpdated = this.clientRepository.save(clientToUpdate);
        log.info("Client updated: {}", clientUpdated);

        return clientUpdated;
    }

    public Client updateStatus(Long id, Client clientModified) {
        Client clientToUpdate = this.getById(id);
        clientToUpdate.setIsActive(clientModified.getIsActive());

        Client clientUpdated = this.clientRepository.save(clientToUpdate);
        log.info("Client updated isActive value: {}", clientUpdated.getIsActive());

        return clientUpdated;
    }

    public void delete(Long id) {
        Client clientToDelete = this.getById(id);
        this.clientRepository.delete(clientToDelete);

        log.info("Client with id: {} deleted", id);
    }
}
