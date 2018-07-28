package com.ryanair.es.interconnecting.flights.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class Leg {

    private String departureAirport;

    private String arrivalAirport;

    private String departureDateTime;

    private String arrivalDateTime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
