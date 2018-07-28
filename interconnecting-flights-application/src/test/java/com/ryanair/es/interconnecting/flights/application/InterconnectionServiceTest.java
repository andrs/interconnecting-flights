package com.ryanair.es.interconnecting.flights.application;

import com.ryanair.es.interconnecting.flights.domain.response.Leg;
import com.ryanair.es.interconnecting.flights.domain.response.ResponseInterconnection;
import com.ryanair.es.interconnecting.flights.domain.routes.Route;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.when;

@Slf4j
public class InterconnectionServiceTest {

    @InjectMocks
    private InterconnectionServiceImpl interconnectionService;

    @Mock
    private RoutesLookupService routesService;

    @Mock
    private ScheduleLookupService scheduleLookupService;

    @Before
    public void init()  throws InterruptedException  {
        MockitoAnnotations.initMocks(this);


        when(routesService.findRoute()).thenReturn(buildInterconnecionRoutes());
    }

    //@Test
    public void buildInterconnectionsTest() {
        String departure = "FRA";
        String arrival = "STN";
        String deartureDateTime = "2018-08-01T07:00";
        String arrivalDateTime = "2019-08-03T21:00";

        List<ResponseInterconnection> interconnections = interconnectionService.buildInterconnections(departure, arrival, deartureDateTime, arrivalDateTime);

        Assert.assertTrue(true);
    }

    private CompletableFuture<List<Route>> buildInterconnecionRoutes() {
        CompletableFuture<List<Route>> routes = new CompletableFuture<>();

        // add routes

        return routes;
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
