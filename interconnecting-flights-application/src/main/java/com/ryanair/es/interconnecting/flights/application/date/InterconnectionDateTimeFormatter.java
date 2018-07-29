package com.ryanair.es.interconnecting.flights.application.date;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class InterconnectionDateTimeFormatter {

    private static final String FORMATTER_DATE_TIME = "yyyy-MM-dd'T'HH:mm";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER_DATE_TIME);

    public static LocalDateTime parseStringDateTime(final String date) {
        try {
            return LocalDateTime.parse(date, formatter);
        } catch (DateTimeParseException dtpe) {
            log.error("Error parsing dates: " + dtpe);
            return null;
        }
    }

    public static boolean isDate1BeforeDate2(final String date1, final String date2) {
        LocalDateTime dt1 = parseStringDateTime(date1);
        LocalDateTime dt2 = parseStringDateTime(date2);

        return dt1.isBefore(dt2);
    }

    public static boolean isDate2GreaterThanTwoHours(final String date1, final String date2) {
        LocalDateTime dt1 = parseStringDateTime(date1);
        LocalDateTime dt2 = parseStringDateTime(date2);

        dt2 = dt2.minusHours(2);

        return dt1.isBefore(dt2);
    }

    public static LocalDateTime formatLocalDateTime(final LocalDateTime localDateTime) {
        try {
            return LocalDateTime.parse(localDateTime.toString(), formatter);
        } catch (DateTimeParseException dtpe) {
            log.error("Error parsing dates: " + dtpe);
            return null;
        }
    }

}
