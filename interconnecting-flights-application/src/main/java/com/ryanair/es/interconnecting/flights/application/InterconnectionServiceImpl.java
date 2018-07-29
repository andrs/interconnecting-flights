package com.ryanair.es.interconnecting.flights.application;

import com.ryanair.es.interconnecting.flights.application.date.InterconnectionDateTimeFormatter;
import com.ryanair.es.interconnecting.flights.application.date.InterconnectionDateTimeValidator;
import com.ryanair.es.interconnecting.flights.domain.response.Leg;
import com.ryanair.es.interconnecting.flights.domain.response.Interconnection;
import com.ryanair.es.interconnecting.flights.domain.routes.Route;
import com.ryanair.es.interconnecting.flights.domain.schedules.Day;
import com.ryanair.es.interconnecting.flights.domain.schedules.Flight;
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
    public List<Interconnection> buildInterconnections(final String departure, final String arrival,
                                                       final String departureDateTime, final String arrivalDateTime) {

        if (!InterconnectionDateTimeValidator.validateImputDates(departureDateTime, arrivalDateTime)) {
            log.error("Ups, maybe dates are wrong.");
            return new ArrayList<>();
        }

        final List<Interconnection> interconnectionFlights = new ArrayList<>();
        final List<Route>  emptyConnectingAirporRoutes = fetchRoutesFromApi();

        // find 0 stop
        buildInterconnectionsFlightsDirect(interconnectionFlights, emptyConnectingAirporRoutes, departure, arrival,
                departureDateTime, arrivalDateTime);

        // find 1 stop
        final List<Interconnection> interconnectionFligtsDeparture = new ArrayList<>();
        final List<Interconnection> interconnectionFligtsArrival = new ArrayList<>();

        buildInterconnectionsFlightsWithOrigin(interconnectionFligtsDeparture, emptyConnectingAirporRoutes, departure,
                arrival, departureDateTime, arrivalDateTime);
        buildInterconnectionsFlightsWithDestin(interconnectionFligtsArrival, emptyConnectingAirporRoutes, departure, arrival,
                departureDateTime, arrivalDateTime);


       for (Interconnection interconnection : interconnectionFligtsDeparture ) {
           interconnection.getLegs().forEach(leg -> {
               String arrivaAirport = leg.getArrivalAirport();

               List <Leg> legs1 = findInterconnectionArrival(interconnectionFligtsArrival, arrivaAirport, leg.getArrivalDateTime());
               log.info(legs1.toString());

               if (!legs1.isEmpty()) {
                   for (Leg l : legs1) {
                       Interconnection interconnectionOneStop = new Interconnection();
                       List<Leg> legs = new ArrayList<>();
                       legs.add(leg);
                       legs.add(l);

                       interconnection.setStops(Integer.valueOf(1));
                       interconnection.setLegs(legs);
                       interconnectionFlights.add(interconnection);
                   }
               }
           });
       }

       return interconnectionFlights;
    }

    private List<Leg> findInterconnectionArrival(final List<Interconnection> interconnectionsArrival,
                                                 final String arrival, final String arrivalDateTime) {

        LocalDateTime arriveDate = InterconnectionDateTimeFormatter.parseStringDateTime(arrivalDateTime);
        if (arriveDate == null ){
            return null;
        }

        // For interconnected flights the difference between the arrival and the next departure should be 2h or greater
        arriveDate = arriveDate.plusHours(2);

        List<Leg> legs = new ArrayList<>();
        for (Interconnection interconnection : interconnectionsArrival ) {
            for (Leg leg : interconnection.getLegs() ) {
                LocalDateTime departureDate = InterconnectionDateTimeFormatter.parseStringDateTime(leg.getDepartureDateTime());

                if (departureDate != null && arrival.equalsIgnoreCase(leg.getDepartureAirport())
                        && departureDate.isAfter(arriveDate)) {
                    legs.add(leg);
                }
            }
        }

        return legs;
    }

    private void buildInterconnectionsFlightsWithDestin(final List<Interconnection> interconnections,
                                                        final List<Route> routes,
                                                        final String departure, final String arrival,
                                                        final String departureDateTime, final String arrivalDateTime) {
        for (Route route : routes) {
            String to = route.getAirportTo();

            if (StringUtils.isEmpty(to) || !to.equalsIgnoreCase(arrival)) {
                continue;
            }

            buildInterconnectionsFlights(interconnections, route, departureDateTime, arrivalDateTime);
        }
    }


    private void buildInterconnectionsFlightsWithOrigin(final List<Interconnection> interconnections,
                                                        final List<Route> routes,
                                                        final String departure, final String arrival,
                                                        final String departureDateTime, final String arrivalDateTime) {
        for (Route route : routes) {
            String from = route.getAirportFrom();

            if (StringUtils.isEmpty(from) || !from.equalsIgnoreCase(departure)) {
                continue;
            }

            buildInterconnectionsFlights(interconnections, route, departureDateTime, arrivalDateTime);
        }
    }


    private void buildInterconnectionsFlightsDirect(final List<Interconnection> interconnections,
                                                    final List<Route> routes,
                                                    final String departure, final String arrival,
                                                    final String departureDateTime, final String arrivalDateTime) {
        for (Route route : routes) {
            String from = route.getAirportFrom();
            String to = route.getAirportTo();

            if (StringUtils.isEmpty(from) || !from.equalsIgnoreCase(departure) || StringUtils.isEmpty(to)
                    || !to.equalsIgnoreCase(arrival)) {
                continue;
            }

            buildInterconnectionsFlights(interconnections, route, departureDateTime, arrivalDateTime);
        }
    }

    private void buildInterconnectionsFlights(final List<Interconnection> interconnections,
                                              final Route route,
                                              final String departureDateTime, final String arrivalDateTime) {

        LocalDateTime departureDate = InterconnectionDateTimeFormatter.parseStringDateTime(departureDateTime);
        LocalDateTime arrivalDate = InterconnectionDateTimeFormatter.parseStringDateTime(arrivalDateTime);

        while (departureDate.isBefore(arrivalDate)) {
            findInterconnectionsScheduleFlights(interconnections, route.getAirportFrom(), route.getAirportTo(),
                    departureDate,  arrivalDateTime, departureDateTime);

            // increase month to fetch next
            departureDate = departureDate.plusMonths(1);
        }
    }

    private void findInterconnectionsScheduleFlights(final List<Interconnection> interconnections,
                                                     final String from, String to,
                                                     final LocalDateTime departureDateTimeFlight,
                                                     final String arrivalDateTime,
                                                     final String departureDateTime) {
        // it's a ordinal 0 - 11
        int month = departureDateTimeFlight.getMonth().ordinal() + 1;
        int year = departureDateTimeFlight.getYear();

        final Schedule scheduleTimeLine = fetchSchedules(from, to, String.valueOf(year), String.valueOf(month));
        if (scheduleTimeLine != null) {
            for (Day day : scheduleTimeLine.getDays()) {

                for (Flight flight : day.getFlights()) {
                    // check imput arrival date time
                    if (flight.getArrivalTime().length() != 5) {
                        continue;
                    }

                    if (flight.getDepartureTime().length() != 5) {
                        continue;
                    }

                    String[] partsDepartureTime = flight.getDepartureTime().split(":");
                    String[] partsArrivalTime = flight.getArrivalTime().split(":");

                    LocalDateTime flightDepartureDateTime = LocalDateTime.of(year, month, day.getDay(),
                            Integer.valueOf(partsDepartureTime[0]).intValue(), Integer.valueOf(partsDepartureTime[1]).intValue());
                    if (flightDepartureDateTime.isBefore(InterconnectionDateTimeFormatter.parseStringDateTime(departureDateTime))) {
                        break;
                    }

                    LocalDateTime flightArivalDateTime = LocalDateTime.of(year, month, day.getDay(),
                            Integer.valueOf(partsArrivalTime[0]).intValue(), Integer.valueOf(partsArrivalTime[1]).intValue());
                    if (flightArivalDateTime.isAfter(InterconnectionDateTimeFormatter.parseStringDateTime(arrivalDateTime))) {
                        break;
                    }

                    Interconnection interconnection = new Interconnection();
                    List<Leg> legs = new ArrayList<>();
                    Leg leg = new Leg();
                    leg.setArrivalAirport(to);
                    leg.setArrivalDateTime(buildLocalDateTime(year, scheduleTimeLine.getMonth(), day.getDay(),
                            flight.getArrivalTime()).toString());

                    leg.setDepartureAirport(from);
                    leg.setDepartureDateTime(buildLocalDateTime(year, scheduleTimeLine.getMonth(), day.getDay(),
                            flight.getDepartureTime()).toString());

                    legs.add(leg);

                    interconnection.setStops(Integer.valueOf(0));
                    interconnection.setLegs(legs);

                    interconnections.add(interconnection);
                }
            }
        }
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

}
