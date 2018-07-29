package com.ryanair.es.interconnecting.flights.application;

import com.ryanair.es.interconnecting.flights.domain.response.Interconnection;
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

import static org.mockito.Mockito.when;

@Slf4j
public class InterconnectionServiceTest {

    public static final String DEPARTURE_AIRPORT = "FRA";
    public static final String ARRIVAL_AIRPORT = "STN";
    public static final String YEAR = "2018";
    public static final String MONTH = "8";

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
    public void buildInterconnectionsWithStopErrorTest() throws InterruptedException {
        String arrival = "STN";
        String deartureDateTime = "2018-08-01T07:00";
        String arrivalDateTime = "2019-08-03T21:00";

        when(routesService.findRoute()).thenReturn(buildInterconnecionRoutesWithConnectionError());

        List<Interconnection> interconnections = interconnectionService.buildInterconnections(DEPARTURE_AIRPORT, arrival, deartureDateTime, arrivalDateTime);

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

    //@Test
    public void buildInterconnectionsWithNonStopErrorTest() throws InterruptedException {


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

        Route route = new Route();
        route.setAirportFrom("FRA");
        route.setAirportTo("STN");
        route.setConnectingAirport(null);
        route.setGroup("CITY");
        route.setNewRoute(Boolean.FALSE);
        route.setOperator("RYANAIR");
        route.setSeasonalRoute(Boolean.FALSE);
        rs.add(route);

        routes.complete(rs);
        return routes;
    }

    private CompletableFuture<Schedule> buildInterconnecionSchedule() {
        CompletableFuture<Schedule> schedule = new CompletableFuture<>();

        Day day = new Day();
        day.setDay(Integer.valueOf(15));
        Flight flight = new Flight();
        flight.setArrivalTime("10:50");
        flight.setDepartureTime("06:50");
        flight.setNumber("123");

        day.setFlights(Arrays.asList(flight));

        List<Day> days = new ArrayList<>();
        days.add(day);

        Schedule s = new Schedule();
        s.setMonth(8);
        s.setDays(days);

        schedule.complete(s);
        return schedule;
    }

}
