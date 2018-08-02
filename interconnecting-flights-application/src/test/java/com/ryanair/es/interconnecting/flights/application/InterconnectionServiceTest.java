package com.ryanair.es.interconnecting.flights.application;

import com.ryanair.es.interconnecting.flights.domain.response.Interconnection;
import com.ryanair.es.interconnecting.flights.domain.response.Leg;
import com.ryanair.es.interconnecting.flights.domain.routes.Route;
import com.ryanair.es.interconnecting.flights.domain.schedules.Day;
import com.ryanair.es.interconnecting.flights.domain.schedules.Flight;
import com.ryanair.es.interconnecting.flights.domain.schedules.Schedule;
import com.ryanair.es.interconnecting.flights.infrastructure.RoutesLookupService;
import com.ryanair.es.interconnecting.flights.infrastructure.ScheduleLookupService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@Slf4j
public class InterconnectionServiceTest {

    public static final String DEPARTURE_AIRPORT = "FRA";
    public static final String ARRIVAL_AIRPORT = "STN";
    public static final String YEAR = "2018";
    public static final String MONTH = "8";
    public static final String AIRPORT_AUX = "ACH";

    @InjectMocks
    private InterconnectionServiceImpl interconnectionService;

    @Mock
    private RoutesLookupService routesService;

    @Mock
    private ScheduleLookupService scheduleLookupService;

    @Before
    public void init()  throws InterruptedException  {
        MockitoAnnotations.initMocks(this);


    }

    @Test
    public void buildInterconnectionsWithDepartureDateTimeErrorTest() throws InterruptedException {
        String deartureDateTime = "2018-08-01T07:00";
        String arrivalDateTime = "2019-08-0321:00";

        List<Interconnection> interconnections = interconnectionService.buildInterconnections(DEPARTURE_AIRPORT, ARRIVAL_AIRPORT, deartureDateTime, arrivalDateTime);

        Assert.assertNotNull(interconnections);
        Assert.assertEquals(0, interconnections.size());
    }

    @Test
    public void buildInterconnectionsWithArrivalDateTimeErrorTest() throws InterruptedException {
        String deartureDateTime = "2020-08-01T00:00";
        String arrivalDateTime = "202008-25T23:59";


        List<Interconnection> interconnections = interconnectionService.buildInterconnections(DEPARTURE_AIRPORT, ARRIVAL_AIRPORT, deartureDateTime, arrivalDateTime);

        Assert.assertNotNull(interconnections);
        Assert.assertEquals(0, interconnections.size());
    }

    @Test
    public void buildInterconnectionsWithArrivalDateTimeEarlyThanDepartureDateTimeTest() throws InterruptedException {
        String deartureDateTime = "2018-09-01T00:00";
        String arrivalDateTime = "2018-08-25T23:59";
        when(routesService.findRoute()).thenReturn(buildInterconnecionRoutesWithConnectionError());

        List<Interconnection> interconnections = interconnectionService.buildInterconnections(DEPARTURE_AIRPORT, ARRIVAL_AIRPORT, deartureDateTime, arrivalDateTime);

        Assert.assertNotNull(interconnections);
        Assert.assertEquals(0, interconnections.size());
    }

    @Test
    public void buildInterconnectionsWithNonStopSuccessTest() throws InterruptedException {
        String deartureDateTime = "2018-08-01T00:00";
        String arrivalDateTime = "2018-08-25T23:59";

        when(routesService.findRoute()).thenReturn(buildInterconnecionRoutes());

        when(scheduleLookupService.findSchedule(DEPARTURE_AIRPORT, ARRIVAL_AIRPORT, YEAR, MONTH)).thenReturn(buildInterconnecionSchedule());

        List<Interconnection> interconnections = interconnectionService.buildInterconnections(DEPARTURE_AIRPORT, ARRIVAL_AIRPORT, deartureDateTime, arrivalDateTime);

        Assert.assertNotNull(interconnections);
        Assert.assertEquals(1, interconnections.size());
    }

    @Test
    public void buildInterconnectionsWithOneStopSuccessTest() throws InterruptedException {
        String deartureDateTime = "2018-08-01T00:00";
        String arrivalDateTime = "2018-08-25T23:59";

        when(routesService.findRoute()).thenReturn(buildInterconnecionOneStopRoutes());

        when(scheduleLookupService.findSchedule(DEPARTURE_AIRPORT, ARRIVAL_AIRPORT, YEAR, MONTH)).thenReturn(buildInterconnecionSchedule());
        when(scheduleLookupService.findSchedule(DEPARTURE_AIRPORT, AIRPORT_AUX, YEAR, MONTH)).thenReturn(buildInterconnecionOneStopSchedule());
        when(scheduleLookupService.findSchedule(AIRPORT_AUX, ARRIVAL_AIRPORT, YEAR, MONTH)).thenReturn(buildInterconnecionOneStopSchedule());

        List<Interconnection> interconnections = interconnectionService.buildInterconnections(DEPARTURE_AIRPORT, ARRIVAL_AIRPORT, deartureDateTime, arrivalDateTime);

        Assert.assertNotNull(interconnections);
        Assert.assertEquals(5, interconnections.size());

        List<Interconnection> interconnectionsOneStop = interconnections.stream().filter( f -> f.getStops().intValue() == 1 ).collect(Collectors.toList());
        Assert.assertEquals(1, interconnections.stream().filter( f -> f.getStops().intValue() == 0 ).collect(Collectors.toList()).size());
        Assert.assertEquals(4, interconnectionsOneStop.size());


        Assert.assertEquals(4, interconnectionsOneStop.stream().filter( f -> {
            Leg leg = f.getLegs().stream().filter(l -> DEPARTURE_AIRPORT.equalsIgnoreCase(l.getDepartureAirport())
                    && AIRPORT_AUX.equalsIgnoreCase(l.getArrivalAirport())).findFirst().orElse(null);
            if ( leg != null) {
                return true;
            }

            return false;
        } ).collect(Collectors.toList()).size());
    }

    private CompletableFuture<List<Route>> buildInterconnecionRoutesWithConnectionError() {
        CompletableFuture<List<Route>> routes = new CompletableFuture<>();

        routes.completeExceptionally(new RuntimeException());
        routes.exceptionally(t -> {
            //t.printStackTrace();
            throw new CompletionException(t);
        });

        return routes;
    }

    private CompletableFuture<List<Route>> buildInterconnecionRoutes() {
        CompletableFuture<List<Route>> routes = new CompletableFuture<>();

        List<Route> rs = new ArrayList<>();
        rs.add(buildRoute(DEPARTURE_AIRPORT , ARRIVAL_AIRPORT ));
        routes.complete(rs);
        return routes;
    }

    private CompletableFuture<Schedule> buildInterconnecionSchedule() {
        CompletableFuture<Schedule> schedule = new CompletableFuture<>();

        Day day = new Day();
        day.setDay(Integer.valueOf(15));
        day.setFlights(Arrays.asList(buildFlight("06:50", "10:50", "123" )));

        List<Day> days = new ArrayList<>();
        days.add(day);

        Schedule s = new Schedule();
        s.setMonth(8);
        s.setDays(days);

        schedule.complete(s);
        return schedule;
    }

    private CompletableFuture<List<Route>> buildInterconnecionOneStopRoutes() {
        CompletableFuture<List<Route>> routes = new CompletableFuture<>();

        List<Route> rs = new ArrayList<>();
        rs.add(buildRoute(DEPARTURE_AIRPORT , ARRIVAL_AIRPORT ));
        rs.add(buildRoute(DEPARTURE_AIRPORT , AIRPORT_AUX ));
        rs.add(buildRoute(AIRPORT_AUX , ARRIVAL_AIRPORT ));

        routes.complete(rs);
        return routes;
    }

    private CompletableFuture<Schedule> buildInterconnecionOneStopSchedule() {
        CompletableFuture<Schedule> schedule = new CompletableFuture<>();

        Day day = new Day();
        day.setDay(Integer.valueOf(16));

        List<Flight> flights = new ArrayList<>();
        flights.add(buildFlight("12:50", "16:50", "1233" ));
        flights.add(buildFlight("20:50", "23:50", "123" ));

        day.setFlights(flights);

        List<Day> days = new ArrayList<>();
        days.add(day);


        day = new Day();
        day.setDay(Integer.valueOf(17));
        flights = new ArrayList<>();
        flights.add(buildFlight("00:50", "04:50", "1233" ));
        flights.add(buildFlight("02:50", "03:50", "123" ));
        day.setFlights(flights);
        days.add(day);

        Schedule s = new Schedule();
        s.setMonth(8);
        s.setDays(days);

        schedule.complete(s);
        return schedule;
    }

    private Route buildRoute(String departure, String arrival) {
        Route route = new Route();
        route.setAirportFrom(departure);
        route.setAirportTo(arrival);
        route.setConnectingAirport(null);
        route.setGroup("CITY");
        route.setNewRoute(Boolean.FALSE);
        route.setOperator("RYANAIR");
        route.setSeasonalRoute(Boolean.FALSE);
        return route;
    }

    private Flight buildFlight(String departureTime, String arrivalTime, String number) {
        Flight flight = new Flight();
        flight.setArrivalTime(arrivalTime);
        flight.setDepartureTime(departureTime);
        flight.setNumber(number);
        return flight;
    }
}
