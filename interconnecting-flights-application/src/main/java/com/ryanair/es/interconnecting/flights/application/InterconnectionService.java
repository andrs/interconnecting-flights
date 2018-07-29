package com.ryanair.es.interconnecting.flights.application;

import com.ryanair.es.interconnecting.flights.domain.response.Interconnection;

import java.util.List;

public interface InterconnectionService {

    List<Interconnection> buildInterconnections(String departure, String arrival,
                                                String departureDateTime, String arrivalDateTime);

}
