package com.ryanair.es.interconnecting.flights.application;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

@Slf4j
public class InterconnectionServiceTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        String str = "str";
    }

    @Test
    public void findMealPlansServicesSuccessTest() {

        String arrivalDateTime = "2018-03-01T07:35";

        String strDatewithTime = "2015-08-04T10:11:30";
        String aa = "aa";
        LocalDateTime date = LocalDateTime.parse(aa);


        System.out.println("Date with Time: " + date);

    }

    private LocalDateTime parseStringDateTime(final String date) {
        try {
            return LocalDateTime.parse(date);
        } catch (DateTimeParseException dtpe) {
            log.error("Error parsing dates: " + dtpe);
            return null;
        }
    }

}
