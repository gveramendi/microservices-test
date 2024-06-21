package net.veramendi.account_microservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.veramendi.account_microservice.client.CustomerClient;
import net.veramendi.account_microservice.client.dto.ClientResponse;
import net.veramendi.account_microservice.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class CustomerService {

    private final CustomerClient customerClient;

    public ClientResponse checkClient(String clientId) {
        // TODO: Instead try catch implement a circuit breaker pattern
        try {
            ClientResponse clientResponse = this.customerClient.findByClientId(clientId);
            if (clientResponse == null || !clientResponse.getIsActive()) {
                String message = "Client with clientId: %s does not exist or is not active.".formatted(clientId);

                log.error(message);
                throw new ResourceNotFoundException(message);
            }

            return clientResponse;
        } catch (Exception e) {
            String message = "Client with clientId: %s does not exist or is not active.".formatted(clientId);

            log.error(message);
            throw new ResourceNotFoundException(message);
        }
    }
}
