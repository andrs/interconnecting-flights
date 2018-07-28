package com.ryanair.es.interconnecting.flights.application;

import com.ryanair.es.interconnecting.flights.application.date.InterconnectionDateTimeFormatter;
import com.ryanair.es.interconnecting.flights.domain.response.Leg;
import com.ryanair.es.interconnecting.flights.domain.response.ResponseInterconnection;
import com.ryanair.es.interconnecting.flights.domain.routes.Route;
import com.ryanair.es.interconnecting.flights.domain.schedules.Schedule;
import com.ryanair.es.interconnecting.flights.infrastructure.RoutesLookupService;
import com.ryanair.es.interconnecting.flights.infrastructure.ScheduleLookupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

        if (!validateImputDates(departureDateTime, arrivalDateTime)) {
            log.error("Ups, maybe dates are wrong.");
            return new ArrayList<>();
        }

        List<ResponseInterconnection> interconnections = new ArrayList<>();

        final List<Route>  emptyConnectingAirporRoutes = fetchRoutesFromApi();
        for (Route route : emptyConnectingAirporRoutes) {
            String from = route.getAirportFrom();
            String to = route.getAirportTo();

            if (StringUtils.isEmpty(from) || !from.equalsIgnoreCase(departure) || StringUtils.isEmpty(to)
                                || !to.equalsIgnoreCase(arrival)) {
                continue;
            }

            LocalDateTime departureDate = InterconnectionDateTimeFormatter.parseStringDateTime(departureDateTime);
            LocalDateTime arrivalDate = InterconnectionDateTimeFormatter.parseStringDateTime(arrivalDateTime);


            ResponseInterconnection interconnection = new ResponseInterconnection();
            while (departureDate.isBefore(arrivalDate)) {
                // it's a ordinal 0 - 11
                int month = departureDate.getMonth().ordinal() + 1;
                int year = departureDate.getYear();

                final Schedule schedule = fetchSchedules(from, to, String.valueOf(year), String.valueOf(month));
                if (schedule != null) {
                    schedule.getDays().forEach(d -> {
                        d.getFlights().forEach(f -> {
                            List<Leg> legs = new ArrayList<>();
                            Leg leg = new Leg();
                            leg.setArrivalAirport(to);
                            leg.setArrivalDateTime(buildLocalDateTime(year, schedule.getMonth(), d.getDay(), f.getArrivalTime()).toString());

                            leg.setDepartureAirport(from);
                            leg.setDepartureDateTime(buildLocalDateTime(year, schedule.getMonth(), d.getDay(), f.getDepartureTime()).toString());

                            legs.add(leg);

                            interconnection.setStops(Integer.valueOf(0));
                            interconnection.setLegs(legs);

                            interconnections.add(interconnection);
                        });
                    });
                }

                // increase month
                departureDate = departureDate.plusMonths(1);
            }

        }

        return interconnections;
    }

    private Schedule fetchSchedules(final String departure, final String arrival, final String year, final String month)  {

        try {
            CompletableFuture<Schedule> futureSchedule = scheduleLookupService.findSchedule(departure, arrival, year, month);

            return futureSchedule.get();

        } catch (InterruptedException | ExecutionException e) {
            log.error("Error calling API endpoint schedule " + e);
            return null;
        }
    }

    private List<Route> fetchRoutesFromApi() {
        final List<Route> routes;

        try {
            CompletableFuture<List<Route>> futureRoutes = routesService.findRoute();

            routes = Collections.unmodifiableList(futureRoutes.get());

        } catch (InterruptedException | ExecutionException e) {
            log.error("Error calling API endpoint route " + e);
            return new ArrayList<>();
        }

        // only routes with empty connectingAirport should be used
        return Collections.unmodifiableList(filterRoutes(routes, isConnectingAirportEmpty()));
    }

    private static Predicate<Route> isConnectingAirportEmpty() {
        return p -> StringUtils.isEmpty(p.getConnectingAirport());
    }

    private static List<Route> filterRoutes(List<Route> routes, Predicate<Route> predicate) {
        return routes.stream().filter( predicate ).collect(Collectors.<Route>toList());
    }

    private LocalDateTime buildLocalDateTime(int year, int month, int day, String hour) {
        if (hour.length() != 5) {
            return null;
        }

        String[] parts = hour.split(":");
        LocalDateTime date = LocalDateTime.of(year, month, day, Integer.valueOf(parts[0]), Integer.valueOf(parts[1]));
        return InterconnectionDateTimeFormatter.formatLocalDateTime(date);
    }

    private boolean validateImputDates(final String departureDateTime, final String arrivalDateTime) {
        boolean isValid = false;

        // validate dates
        if (InterconnectionDateTimeFormatter.parseStringDateTime(departureDateTime) == null
                || InterconnectionDateTimeFormatter.parseStringDateTime(arrivalDateTime) == null) {
            log.error("Error: departure date time or arrival date time are wrong!");
            return isValid;
        }

        if (!InterconnectionDateTimeFormatter.isDate1BeforeDate2(departureDateTime, arrivalDateTime) ) {
            log.error("Error, arrival date time must be bigger than departure date time");
            return isValid;
        }

        // check diff dates
        if ( !InterconnectionDateTimeFormatter.parseStringDateTime(arrivalDateTime).minusYears(4).isBefore(LocalDateTime.now()) ) {
            log.error("Error, arrival is too big");
            return isValid;
        }

        return true;
    }

}
