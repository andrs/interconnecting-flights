package com.ryanair.es.interconnecting.flights.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class ResponseInterconnection {

    private Integer stops;

    private List<Leg> legs;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
