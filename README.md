# interconnecting-flights

The "Spring MVC based RESTful API" application
==============================

to compile use: mvn clean install

deploy a war file in tomcat, **or test** with mvn spring-boot:run inside boot module.


**tested on:** java version "1.8.0_181", and apache-tomcat-8.0.53.

-- interfaz web: http://localhost:8080/flights/

-- examples REST interface

http://localhost:8080/flights/interconnections?departure=DUB&arrival=MAD&departureDateTime=2018-08-01T07:00&arrivalDateTime=2018-12-03T23:00

http://localhost:8080/flights/interconnections?departure=DUB&arrival=WRO&departureDateTime=2018-08-01T07:00&arrivalDateTime=2018-08-03T21:00

http://localhost:8080/flights/interconnections?departure=FRA&arrival=STN&departureDateTime=2018-08-01T07:00&arrivalDateTime=2019-08-03T21:00

http://localhost:8080/flights/interconnections?departure=MAD&arrival=DUB&departureDateTime=2018-08-01T07:00&arrivalDateTime=2018-12-03T21:00


-- test 
ºhttp://localhost:8080/flights/interconnections?departure=DUB&arrival=MAD&departureDateTime=2018-08-20T23:00&arrivalDateTime=2018-08-21T23:00