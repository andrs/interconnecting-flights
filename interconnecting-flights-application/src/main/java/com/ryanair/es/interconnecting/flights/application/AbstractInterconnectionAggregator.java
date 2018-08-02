package com.ryanair.es.interconnecting.flights.application;

import com.ryanair.es.interconnecting.flights.application.date.InterconnectionDateTimeFormatter;
import com.ryanair.es.interconnecting.flights.domain.response.Interconnection;
import com.ryanair.es.interconnecting.flights.domain.response.Leg;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

abstract class AbstractInterconnectionAggregator {

    public abstract List<Interconnection> aggregateOneStop(final List<Interconnection> interconnectionFligtsDeparture,
                                                           final List<Interconnection> interconnectionFligtsArrival);


    protected List<Leg> matchArrivalInterconnectionWithNexDeparture(final List<Interconnection> interconnectionsArrival,
                                                                    final String arrival, final String arrivalDateTime) {

        LocalDateTime arriveDate = InterconnectionDateTimeFormatter.parseStringDateTime(arrivalDateTime);
        if (arriveDate == null ){
            return null;
        }

        // For interconnected flights the difference between the arrival and the next departure should be 2h or greater
        arriveDate = arriveDate.plusHours(2);

        List<Leg> legs = new ArrayList<>();
        for (Interconnection interconnection : interconnectionsArrival ) {
            for (Leg leg : interconnection.getLegs() ) {
                LocalDateTime departureDate = InterconnectionDateTimeFormatter.parseStringDateTime(leg.getDepartureDateTime());

                if (departureDate != null && arrival.equalsIgnoreCase(leg.getDepartureAirport())
                        && departureDate.isAfter(arriveDate)) {
                    legs.add(leg);
                }
            }
        }

        return legs;
    }
}
