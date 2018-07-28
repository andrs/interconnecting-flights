package com.ryanair.es.interconnecting.flights.application;

import com.ryanair.es.interconnecting.flights.domain.routes.Route;
import com.ryanair.es.interconnecting.flights.infrastructure.RoutesLookupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class InterconnectionServiceImpl implements InterconnectionService {

    private final RoutesLookupService routesService;

    @Autowired
    public InterconnectionServiceImpl(RoutesLookupService routesService) {
        this.routesService = routesService;
    }

    @Override
    public void build() {


        try {
            // Start the clock
            long start = System.currentTimeMillis();

            // Kick of multiple, asynchronou s lookups
            CompletableFuture<List<Route>> page1 = routesService.findRoute();
            CompletableFuture<List<Route>> page2 = routesService.findRoute();
            CompletableFuture<List<Route>> page3 = routesService.findRoute();

            // Print results, including elapsed time
            log.info("Elapsed time: " + (System.currentTimeMillis() - start));
            log.info("--> " + page1.get());
            log.info("--> " + page2.get());
            log.info("--> " + page3.get());

        } catch (InterruptedException | ExecutionException e) {

        }





        String str = "str";


        str = "str";
    }
}
