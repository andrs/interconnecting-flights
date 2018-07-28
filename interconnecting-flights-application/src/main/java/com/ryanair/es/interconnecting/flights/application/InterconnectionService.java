package com.ryanair.es.interconnecting.flights.application;

public interface InterconnectionService {

    void build(String departure, String arrival, String departureDateTime, String arrivalDateTime);

}
