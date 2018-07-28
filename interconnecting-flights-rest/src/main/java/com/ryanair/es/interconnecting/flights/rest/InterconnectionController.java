package com.ryanair.es.interconnecting.flights.rest;

import com.ryanair.es.interconnecting.flights.application.InterconnectionService;
import com.ryanair.es.interconnecting.flights.domain.GenericEntity;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/")
@Api(value = "InterconnectionController")
public class InterconnectionController {

    private InterconnectionService interconnectionService;

    public InterconnectionController(final InterconnectionService interconnectionService) {
        this.interconnectionService = interconnectionService;
    }

    @GetMapping("/test")
    public List<String> findAll() {
        String str = "str";
        interconnectionService.build();
        return Arrays.asList("test");
    }
}
