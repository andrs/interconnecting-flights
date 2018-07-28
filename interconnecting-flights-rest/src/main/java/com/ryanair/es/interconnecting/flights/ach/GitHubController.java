package com.ryanair.es.interconnecting.flights.ach;

import com.ryanair.es.interconnecting.flights.domain.routes.Route;
import com.ryanair.es.interconnecting.flights.domain.schedules.Schedule;
import com.ryanair.es.interconnecting.flights.infrastructure.RoutesLookupService;
import com.ryanair.es.interconnecting.flights.infrastructure.ScheduleLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class GitHubController {

    private final GitHubLookupService lookupService;

    private final RoutesLookupService routesService;

    private final ScheduleLookupService scheduleLookupService;

    @Autowired
    public GitHubController(GitHubLookupService lookupService, RoutesLookupService service, ScheduleLookupService s) {
        this.lookupService = lookupService;
        this.routesService = service;
        this.scheduleLookupService = s;
    }

    @RequestMapping("/user/{name}")
    public CompletableFuture<TimedResponse<User>> findUser(@PathVariable(value = "name") String name) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        ServerResponse response = new ServerResponse();
        CompletableFuture<TimedResponse<User>> future = lookupService.findUser(name)
                .thenApply(user -> {
                    response.setData(user);
                    response.setTimeMs(System.currentTimeMillis() - start);
                    response.setCompletingThread(Thread.currentThread().getName());
                    return response;
                });
        return future;
    }


    @RequestMapping("/routes")
    public CompletableFuture<TimedResponse<List<Route>>> findUser1() throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        RouteResponse response = new RouteResponse();
        CompletableFuture<TimedResponse<List<Route>>> future = routesService.findRoute()
                .thenApply(user -> {
                    response.setData(user);
                    response.setTimeMs(System.currentTimeMillis() - start);
                    response.setCompletingThread(Thread.currentThread().getName());
                    return response;
                });
        return future;
    }

    // https://api.ryanair.com/timetable/3/schedules/DUB/STN/years/2018/months/8

    @RequestMapping("/schedule")
    public CompletableFuture<TimedResponse<Schedule>> findUser2() throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        ScheduleResponse response = new ScheduleResponse();
        CompletableFuture<TimedResponse<Schedule>> future = scheduleLookupService.findSchedule("DUB", "STN", "2018", "8")
                .thenApply(user -> {
                    response.setData(user);
                    response.setTimeMs(System.currentTimeMillis() - start);
                    response.setCompletingThread(Thread.currentThread().getName());
                    return response;
                });
        return future;
    }
}
