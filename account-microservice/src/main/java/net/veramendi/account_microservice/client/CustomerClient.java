package net.veramendi.account_microservice.client;

import net.veramendi.account_microservice.client.dto.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client-microservice")
public interface CustomerClient {

    @GetMapping(
            value = "/api/clients/client-id/{clientId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ClientResponse findByClientId(@PathVariable("clientId") String clientId);
}
