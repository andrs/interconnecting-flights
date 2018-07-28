package com.ryanair.es.interconnecting.flights.application;

import com.ryanair.es.interconnecting.flights.application.date.InterconnectionDateTimeFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

public class InterconnectionDateTimeFormatterTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void parseStringDateTimeSuccessTest() {
        String arrivalDateTime = "2018-03-01T07:35";

        Assert.assertNotNull(InterconnectionDateTimeFormatter.parseStringDateTime(arrivalDateTime));
    }

    @Test
    public void parseStringDateTimeErrorTest() {
        String arrivalDateTime = "aa2018-03-01T07:aa";

        Assert.assertNull(InterconnectionDateTimeFormatter.parseStringDateTime(arrivalDateTime));
    }

    @Test
    public void isDate1BeforeDate2SuccessTest() {
        String departureDateTime = "2018-03-01T12:40";
        String arrivalDateTime = "2018-03-01T17:35";


        Assert.assertTrue(InterconnectionDateTimeFormatter.isDate1BeforeDate2(departureDateTime, arrivalDateTime));
    }

    @Test
    public void isDate1BeforeDateErrorEqualsTest() {
        String departureDateTime = "2018-03-01T07:35";
        String arrivalDateTime = "2018-03-01T07:35";

        Assert.assertFalse(InterconnectionDateTimeFormatter.isDate1BeforeDate2(departureDateTime, arrivalDateTime));
    }

    @Test
    public void isDate1BeforeDateError1Test() {
        String departureDateTime = "2018-03-01T07:35";
        String arrivalDateTime = "2018-03-01T05:35";

        Assert.assertFalse(InterconnectionDateTimeFormatter.isDate1BeforeDate2(departureDateTime, arrivalDateTime));
    }

    @Test
    public void isDate2GreaterThanTwoHoursSuccessTest() {
        String departureDateTime = "2018-03-01T10:35";
        String arrivalDateTime = "2018-03-01T17:35";

        Assert.assertTrue(InterconnectionDateTimeFormatter.isDate2GreaterThanTwoHours(departureDateTime, arrivalDateTime));
    }

    @Test
    public void isDate2GreaterThanTwoHoursEqualsTest() {
        String departureDateTime = "2018-03-01T10:00";
        String arrivalDateTime = "2018-03-01T12:00";

        Assert.assertFalse(InterconnectionDateTimeFormatter.isDate2GreaterThanTwoHours(departureDateTime, arrivalDateTime));
    }

    public void isDate2GreaterThanTwoHoursErrorTest() {
        String departureDateTime = "2018-03-01T10:00";
        String arrivalDateTime = "2018-03-01T11:59";

        Assert.assertFalse(InterconnectionDateTimeFormatter.isDate2GreaterThanTwoHours(departureDateTime, arrivalDateTime));
    }

}
