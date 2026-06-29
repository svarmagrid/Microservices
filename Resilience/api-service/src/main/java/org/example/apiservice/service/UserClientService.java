package org.example.apiservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class UserClientService {

    private final RestClient restClient;

    public UserClientService(RestClient restClient) {
        this.restClient = restClient;
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackUsers")
    @Retry(name = "userService")
    public List<String> getUsers() {

        return restClient.get()
                .uri("/users")
                .retrieve()
                .body(new ParameterizedTypeReference<List<String>>() {});
    }

    public List<String> fallbackUsers(Throwable ex) {

        return List.of(
                "Fallback User 1",
                "Fallback User 2"
        );

    }

}
