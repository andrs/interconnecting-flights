package com.ryanair.es.interconnecting.flights.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@EnableSwagger2
@SpringBootApplication
@ComponentScan("com.ryanair.es.interconnecting.flights")
public class SpringBootTomcatApplication extends SpringBootServletInitializer {
}
