package com.ryanair.es.interconnecting.flights.infrastructure;

import com.ryanair.es.interconnecting.flights.domain.routes.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class RoutesLookupService {
    private static String ENDPOINT_ROUTES = "https://api.ryanair.com/core/3/routes";
    private final RestTemplate restTemplate;


    public RoutesLookupService(final RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<List<Route>> findRoute() throws InterruptedException {
        log.info("Looking up routes ");
        ResponseEntity<List<Route>> rateResponse = restTemplate.exchange(ENDPOINT_ROUTES, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Route>>() { });
        List<Route> results = rateResponse.getBody();
        return CompletableFuture.completedFuture(results);
    }
}
