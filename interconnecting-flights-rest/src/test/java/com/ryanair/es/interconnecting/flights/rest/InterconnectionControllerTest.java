package com.ryanair.es.interconnecting.flights.rest;

import com.ryanair.es.interconnecting.flights.application.InterconnectionService;
import com.ryanair.es.interconnecting.flights.domain.response.Interconnection;
import com.ryanair.es.interconnecting.flights.domain.response.Leg;
import io.swagger.annotations.ApiParam;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class InterconnectionControllerTest {
        private static final String ENDPOINT__INTERCONNECTIONS_CONTROLLER = "/interconnections";

        private MockMvc mockRestController;

        @InjectMocks
        private InterconnectionController interconnectionController;

        @Mock
        private InterconnectionService interconnectionService;


        @Before
        public void init() {
            MockitoAnnotations.initMocks(this);

            when(interconnectionService.buildInterconnections(Mockito.anyString(), Mockito.anyString(),
                    Mockito.anyString(), Mockito.anyString() )).thenReturn(buildResponseInterconnection());

            this.mockRestController = standaloneSetup( interconnectionController ).setMessageConverters().build();
        }

        @Test
        public void handleInterconnectionsTest() throws Exception {

            MvcResult mvcResult = mockRestController.perform(get(ENDPOINT__INTERCONNECTIONS_CONTROLLER)
                    .headers(headers())
                    .param("departure", "custom")
                    .param("arrival", "custom")
                    .param("departureDateTime", "custom")
                    .param("arrivalDateTime", "custom")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful()).andReturn();

            assertNotNull(mvcResult.getResponse().getContentAsString());

        }

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        return headers;
    }

    private List<Interconnection> buildResponseInterconnection() {
        Interconnection interconnection1 = new Interconnection();
        List<Leg> legs1 = new ArrayList<>();
        legs1.add(buildLeg("DUB", "WRO", "2018-03-01T12:40", "2018-03-01T16:40"));

        interconnection1.setLegs(legs1);
        interconnection1.setStops(Integer.valueOf(0));

        Interconnection interconnection2 = new Interconnection();
        List<Leg> legs2 = new ArrayList<>();
        legs2.add(buildLeg("DUB", "STN", "2018-03-01T06:25", "2018-03-01T07:35"));
        legs2.add(buildLeg("DUB", "WRO", "2018-03-01T09:50", "2018-03-01T13:20"));

        interconnection2.setLegs(legs2);
        interconnection2.setStops(Integer.valueOf(1));

        List<Interconnection> response = new ArrayList<>();
        response.add(interconnection1);
        response.add(interconnection2);
        return response;
    }

    private Leg buildLeg(String departure, String arrival, String departureDateTime, String arrivalDateTime) {
        Leg leg = new Leg();
        leg.setArrivalAirport(arrival);
        leg.setArrivalDateTime(arrivalDateTime);
        leg.setDepartureAirport(departure);
        leg.setDepartureDateTime(departureDateTime);
        return leg;
    }
}
