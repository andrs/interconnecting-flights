package com.ryanair.es.interconnecting.flights.domain.routes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class Route implements Serializable {
    private String airportFrom;

    private String airportTo;

    private String connectingAirport;

    private Boolean newRoute;

    private Boolean seasonalRoute;

    private String operator;

    private String group;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
