package com.ryanair.es.interconnecting.flights.application;

import com.ryanair.es.interconnecting.flights.domain.routes.Route;
import com.ryanair.es.interconnecting.flights.domain.schedules.Schedule;
import com.ryanair.es.interconnecting.flights.infrastructure.RoutesLookupService;
import com.ryanair.es.interconnecting.flights.infrastructure.ScheduleLookupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class InterconnectionServiceImpl implements InterconnectionService {

    private final RoutesLookupService routesService;

    private final ScheduleLookupService scheduleLookupService;

    @Autowired
    public InterconnectionServiceImpl(final RoutesLookupService routesService,
                                      final ScheduleLookupService scheduleLookupService) {
        this.routesService = routesService;
        this.scheduleLookupService = scheduleLookupService;
    }

    @Override
    public void build(String departure, String arrival, String departureDateTime, String arrivalDateTime) {


        try {
            // Start the clock
            long start = System.currentTimeMillis();

            CompletableFuture<List<Route>> futureRoutes = routesService.findRoute();


            CompletableFuture<Schedule> futureSchedule
                    = scheduleLookupService.findSchedule("DUB", "STN", "2018", "8");

            // end clock
            log.info("Elapsed time: " + (System.currentTimeMillis() - start));

            final List<Route> routes = Collections.unmodifiableList(futureRoutes.get());

            Schedule schedule = fetchSchedules(departure, arrival, departureDateTime, arrivalDateTime);

            schedule = futureSchedule.get();

            String str = "str";
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private Schedule fetchSchedules(final String departure, final String arrival, final String departureDateTime,
                                    final String arrivalDateTime) throws InterruptedException, ExecutionException {

        CompletableFuture<Schedule> futureSchedule
                    = scheduleLookupService.findSchedule(departure, arrival, departureDateTime, arrivalDateTime);

        return futureSchedule.get();
    }
}
