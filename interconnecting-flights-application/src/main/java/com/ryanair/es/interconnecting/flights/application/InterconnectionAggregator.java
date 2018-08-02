package com.ryanair.es.interconnecting.flights.application;

import com.ryanair.es.interconnecting.flights.domain.response.Interconnection;
import com.ryanair.es.interconnecting.flights.domain.response.Leg;

import java.util.ArrayList;
import java.util.List;

public class InterconnectionAggregator extends AbstractInterconnectionAggregator {
    private static final int ONE_STOP = 1;

    @Override
    public List<Interconnection> aggregateOneStop(final List<Interconnection> interconnectionFligtsDeparture,
                                                  final List<Interconnection> interconnectionFligtsArrival) {

        final List<Interconnection> interconnectionFlights = new ArrayList<>();

        // match departure interconnectionFligtsDeparture collection - arrival interconnectionFligtsArrival collection
        for (Interconnection interconnection : interconnectionFligtsDeparture ) {
            interconnection.getLegs().forEach(leg -> {
                List <Leg> legsFromArrival = matchInterconnectionDepartureFromArrival(interconnectionFligtsArrival,
                        leg.getArrivalAirport(), leg.getArrivalDateTime());

                if (!legsFromArrival.isEmpty()) {
                    for (Leg l : legsFromArrival) {
                        Leg newLeg = new Leg();
                        List<Leg> legs = new ArrayList<>();

                        newLeg.setArrivalAirport(leg.getArrivalAirport());
                        newLeg.setDepartureDateTime(leg.getDepartureDateTime());
                        newLeg.setDepartureAirport(leg.getDepartureAirport());
                        newLeg.setArrivalDateTime(leg.getArrivalDateTime());
                        legs.add(newLeg);

                        newLeg = new Leg();
                        newLeg.setArrivalAirport(l.getArrivalAirport());
                        newLeg.setDepartureDateTime(l.getDepartureDateTime());
                        newLeg.setDepartureAirport(l.getDepartureAirport());
                        newLeg.setArrivalDateTime(l.getArrivalDateTime());
                        legs.add(newLeg);

                        interconnectionFlights.add(new Interconnection(Integer.valueOf(ONE_STOP), legs));
                    }
                }
            });
        }

        return interconnectionFlights;
    }

}
