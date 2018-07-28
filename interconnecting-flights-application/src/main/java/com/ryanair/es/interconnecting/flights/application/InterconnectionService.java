package com.ryanair.es.interconnecting.flights.application;

import com.ryanair.es.interconnecting.flights.domain.response.ResponseInterconnection;

import java.util.List;

public interface InterconnectionService {

    List<ResponseInterconnection> buildInterconnections(String departure, String arrival,
                                                        String departureDateTime, String arrivalDateTime);

}
