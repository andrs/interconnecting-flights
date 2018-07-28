package com.ryanair.es.interconnecting.flights.application;

import com.ryanair.es.interconnecting.flights.application.date.InterconnectionDateTimeFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

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
        String arrivalDateTime = "2018-03-01T07:35";
        String departureDateTime = "2018-03-01T12:40";

        Assert.assertTrue(InterconnectionDateTimeFormatter.isDate1BeforeDate2(arrivalDateTime, departureDateTime));
    }

    @Test
    public void isDate1BeforeDateErrorTest() {
        String arrivalDateTime = "2018-03-01T07:35";
        String departureDateTime = "2018-03-01T07:35";

        Assert.assertFalse(InterconnectionDateTimeFormatter.isDate1BeforeDate2(arrivalDateTime, departureDateTime));
    }

    @Test
    public void isDate1BeforeDateError1Test() {
        String arrivalDateTime = "2018-03-01T07:35";
        String departureDateTime = "2018-03-01T05:35";

        Assert.assertFalse(InterconnectionDateTimeFormatter.isDate1BeforeDate2(arrivalDateTime, departureDateTime));
    }
}
