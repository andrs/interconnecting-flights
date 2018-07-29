package com.ryanair.es.interconnecting.flights.application.date;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class InterconnectionDateTimeValidator {

    public static boolean validateImputDates(final String departureDateTime, final String arrivalDateTime) {
        boolean isValid = false;

        // validate dates
        if (InterconnectionDateTimeFormatter.parseStringDateTime(departureDateTime) == null
                || InterconnectionDateTimeFormatter.parseStringDateTime(arrivalDateTime) == null) {
            log.error("Error: departure date time or arrival date time are wrong!");
            return isValid;
        }

        if (!InterconnectionDateTimeFormatter.isDate1BeforeDate2(departureDateTime, arrivalDateTime) ) {
            log.error("Error, arrival date time must be bigger than departure date time");
            return isValid;
        }

        // check diff dates
        if ( !InterconnectionDateTimeFormatter.parseStringDateTime(arrivalDateTime).minusYears(4).isBefore(LocalDateTime.now()) ) {
            log.error("Error, arrival is too big");
            return isValid;
        }

        return true;
    }
}
