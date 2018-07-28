package com.ryanair.es.interconnecting.flights.rest;

import com.ryanair.es.interconnecting.flights.application.InterconnectionService;
import com.ryanair.es.interconnecting.flights.domain.GenericEntity;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/interconnections")
@Api(value = "InterconnectionController")
public class InterconnectionController {
    private static final int HTTP_GET_OK = 200;
    private static final int HTTP_NO_CONTENT = 204;
    private static final int HTTP_NOT_FOUND = 404;

    private InterconnectionService interconnectionService;

    public InterconnectionController(final InterconnectionService interconnectionService) {
        this.interconnectionService = interconnectionService;
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "http://<HOST>/<CONTEXT>/interconnections?departure={departure}&arrival={arrival}&departureDateTime={departureDateTime}&arrivalDateTime={arrivalDateTime}",
            notes = "erves information about possible direct and interconnected flights")
    @ApiResponses({
            @ApiResponse(code = HTTP_GET_OK, message = "Successfully"),
            @ApiResponse(code = HTTP_NO_CONTENT, message = "No Content"),
            @ApiResponse(code = HTTP_NOT_FOUND, message = "ERROR, NOT FOUND") })
    @RequestMapping
    public ResponseEntity  handleInterconnections (@ApiParam(value = "departure airport IATA code") @RequestParam String departure,
                                               @ApiParam(value = "an arrival airport IATA code") @RequestParam String arrival,
                                               @ApiParam(value = "departure datetime in the departure airport") @RequestParam String departureDateTime,
                                               @ApiParam(value = "an arrival datetime in the arrival airport") @RequestParam String arrivalDateTime) {
        Assert.notNull(departure, "departure airport IATA  is mandatory");
        Assert.notNull(arrival, "an arrival airport IATA code is mandatory");
        Assert.notNull(departureDateTime, "departure datetime in the departure airport is mandatory");
        Assert.notNull(arrivalDateTime, "an arrival datetime in the arrival airport is mandatory");

        interconnectionService.build(departure, arrival, departureDateTime, arrivalDateTime);

        return new ResponseEntity ("asda", HttpStatus.OK);

    }
}
