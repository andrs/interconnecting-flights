# interconnecting-flights

The "Spring MVC based RESTful API" application
==============================

to compile use: mvn clean install

deploy a war file in tomcat, **or test** with mvn spring-boot:run inside boot module.


**tested on:** java version "1.8.0_181", and apache-tomcat-8.0.53.

-- interfaz web: http://localhost:8080/flights/

-- examples
curl -X GET http://localhost:8080/flights/v1/test
