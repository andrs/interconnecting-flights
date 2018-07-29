package com.ryanair.es.interconnecting.flights.infrastructure;

import com.ryanair.es.interconnecting.flights.domain.schedules.Schedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ScheduleLookupService {
    private static String END_POINT_SCHEDULE = "https://api.ryanair.com/timetable/3/schedules/%s/%s/years/%s/months/%s";

    private final RestTemplate restTemplate1;


    public ScheduleLookupService(final RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate1 = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<Schedule> findSchedule(final String departure, final String arrival, final String year,
                                                          final String month) throws InterruptedException {
        String url = String.format(END_POINT_SCHEDULE , departure, arrival, year, month);

        log.info("Looking up schedule ... " + url);

        ResponseEntity<Schedule> rateResponse = restTemplate1.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<Schedule>() { });

        Schedule results = rateResponse.getBody();

        return CompletableFuture.completedFuture(results);
    }
}
