package com.ryanair.es.interconnecting.flights.application;

import com.ryanair.es.interconnecting.flights.domain.response.Leg;
import com.ryanair.es.interconnecting.flights.domain.response.ResponseInterconnection;
import com.ryanair.es.interconnecting.flights.domain.routes.Route;
import com.ryanair.es.interconnecting.flights.domain.schedules.Schedule;
import com.ryanair.es.interconnecting.flights.infrastructure.RoutesLookupService;
import com.ryanair.es.interconnecting.flights.infrastructure.ScheduleLookupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
    public List<ResponseInterconnection> buildInterconnections(final String departure, final String arrival,
                                                               final String departureDateTime, final String arrivalDateTime) {


        try {
            // Start the clock
            long start = System.currentTimeMillis();

            CompletableFuture<List<Route>> futureRoutes = routesService.findRoute();

/*
            CompletableFuture<Schedule> futureSchedule
                    = scheduleLookupService.findSchedule("DUB", "STN", "2018", "8");
*/
            // end clock
            log.info("Elapsed time: " + (System.currentTimeMillis() - start));

            final List<Route> routes = Collections.unmodifiableList(futureRoutes.get());

            final Schedule schedule = fetchSchedules(departure, arrival, departureDateTime, arrivalDateTime);

            //schedule = futureSchedule.get();

            String str = "str";
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error calling API endpoint " + e);
            return new ArrayList<>();
        }


        // only routes with empty connectingAirport should be used


        return buildResponseInterconnection();
    }

    private Schedule fetchSchedules(final String departure, final String arrival, final String departureDateTime,
                                    final String arrivalDateTime) throws InterruptedException, ExecutionException {

        /*
        CompletableFuture<Schedule> futureSchedule
                    = scheduleLookupService.findSchedule(departure, arrival, departureDateTime, arrivalDateTime);
        */
        CompletableFuture<Schedule> futureSchedule
                = scheduleLookupService.findSchedule("DUB", "STN", "2018", "8");

        return futureSchedule.get();
    }

    private List<ResponseInterconnection> buildResponseInterconnection() {

        ResponseInterconnection interconnection1 = new ResponseInterconnection();
        List<Leg> legs1 = new ArrayList<>();
        Leg leg1 = new Leg();
        leg1.setArrivalAirport("WRO");
        leg1.setArrivalDateTime("2018-03-01T16:40");
        leg1.setDepartureAirport("DUB");
        leg1.setDepartureDateTime("2018-03-01T12:40");
        legs1.add(leg1);

        interconnection1.setLegs(legs1);
        interconnection1.setStops(Integer.valueOf(0));


        ResponseInterconnection interconnection2 = new ResponseInterconnection();
        List<Leg> legs2 = new ArrayList<>();
        Leg leg11 = new Leg();
        leg11.setArrivalAirport("STN");
        leg11.setArrivalDateTime("2018-03-01T07:35");
        leg11.setDepartureAirport("DUB");
        leg11.setDepartureDateTime("2018-03-01T06:25");
        legs2.add(leg1);

        Leg leg12 = new Leg();
        leg12.setArrivalAirport("WRO");
        leg12.setArrivalDateTime("2018-03-01T13:20");
        leg12.setDepartureAirport("STN");
        leg12.setDepartureDateTime("2018-03-01T09:50");
        legs2.add(leg12);

        interconnection2.setLegs(legs2);
        interconnection2.setStops(Integer.valueOf(1));

        List<ResponseInterconnection> response = new ArrayList<>();
        response.add(interconnection1);
        response.add(interconnection2);
        return response;
    }
}
