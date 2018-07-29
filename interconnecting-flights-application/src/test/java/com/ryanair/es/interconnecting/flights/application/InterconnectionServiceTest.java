package com.ryanair.es.interconnecting.flights.application;

import com.ryanair.es.interconnecting.flights.domain.response.Leg;
import com.ryanair.es.interconnecting.flights.domain.response.Interconnection;
import com.ryanair.es.interconnecting.flights.domain.routes.Route;
import com.ryanair.es.interconnecting.flights.infrastructure.RoutesLookupService;
import com.ryanair.es.interconnecting.flights.infrastructure.ScheduleLookupService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

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



    }

    //@Test
    public void buildInterconnectionsTest() throws InterruptedException {
        String departure = "FRA";
        String arrival = "STN";
        String deartureDateTime = "2018-08-01T07:00";
        String arrivalDateTime = "2019-08-03T21:00";

        when(routesService.findRoute()).thenReturn(buildInterconnecionRoutes());

        List<Interconnection> interconnections = interconnectionService.buildInterconnections(departure, arrival, deartureDateTime, arrivalDateTime);

        Assert.assertTrue(true);
    }

    private CompletableFuture<List<Route>> buildInterconnecionRoutes() {
        CompletableFuture<List<Route>> routes = new CompletableFuture<>();

        // add routes

        return routes;
    }

}
