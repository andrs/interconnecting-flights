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
http://localhost:8080/flights/interconnections?departure=DUB&arrival=MAD&departureDateTime=2018-08-20T23:00&arrivalDateTime=2018-08-21T23:00

[
    {
        "stops": 0,
        "legs": [
            {
                "departureAirport": "DUB",
                "arrivalAirport": "MAD",
                "departureDateTime": "2018-08-21T06:25",
                "arrivalDateTime": "2018-08-21T10:00"
            }
        ]
    },
    {
        "stops": 0,
        "legs": [
            {
                "departureAirport": "DUB",
                "arrivalAirport": "MAD",
                "departureDateTime": "2018-08-21T16:25",
                "arrivalDateTime": "2018-08-21T20:05"
            }
        ]
    },
    {
        "stops": 1,
        "legs": [
            {
                "departureAirport": "DUB",
                "arrivalAirport": "ACE",
                "departureDateTime": "2018-08-21T06:15",
                "arrivalDateTime": "2018-08-21T10:30"
            },
            {
                "departureAirport": "ACE",
                "arrivalAirport": "MAD",
                "departureDateTime": "2018-08-21T17:30",
                "arrivalDateTime": "2018-08-21T21:05"
            }
        ]
    },
    {
        "stops": 1,
        "legs": [
            {
                "departureAirport": "DUB",
                "arrivalAirport": "ACE",
                "departureDateTime": "2018-08-21T07:10",
                "arrivalDateTime": "2018-08-21T11:20"
            },
            {
                "departureAirport": "ACE",
                "arrivalAirport": "MAD",
                "departureDateTime": "2018-08-21T17:30",
                "arrivalDateTime": "2018-08-21T21:05"
            }
        ]
    },
    {
        "stops": 1,
        "legs": [
            {
                "departureAirport": "DUB",
                "arrivalAirport": "BVA",
                "departureDateTime": "2018-08-21T06:40",
                "arrivalDateTime": "2018-08-21T09:15"
            },
            {
                "departureAirport": "BVA",
                "arrivalAirport": "MAD",
                "departureDateTime": "2018-08-21T13:45",
                "arrivalDateTime": "2018-08-21T15:55"
            }
        ]
    },
    {
        "stops": 1,
        "legs": [
            {
                "departureAirport": "DUB",
                "arrivalAirport": "CIA",
                "departureDateTime": "2018-08-21T06:50",
                "arrivalDateTime": "2018-08-21T10:55"
            },
            {
                "departureAirport": "CIA",
                "arrivalAirport": "MAD",
                "departureDateTime": "2018-08-21T17:25",
                "arrivalDateTime": "2018-08-21T20:00"
            }
        ]
    },
    {
        "stops": 1,
        "legs": [
            {
                "departureAirport": "DUB",
                "arrivalAirport": "CIA",
                "departureDateTime": "2018-08-21T06:50",
                "arrivalDateTime": "2018-08-21T10:55"
            },
            {
                "departureAirport": "CIA",
                "arrivalAirport": "MAD",
                "departureDateTime": "2018-08-21T17:25",
                "arrivalDateTime": "2018-08-21T20:00"
            }
        ]
    },
    {
        "stops": 1,
        "legs": [
            {
                "departureAirport": "DUB",
                "arrivalAirport": "BRU",
                "departureDateTime": "2018-08-21T06:30",
                "arrivalDateTime": "2018-08-21T09:20"
            },
            {
                "departureAirport": "BRU",
                "arrivalAirport": "MAD",
                "departureDateTime": "2018-08-21T20:30",
                "arrivalDateTime": "2018-08-21T23:00"
            }
        ]
    },
    {
        "stops": 1,
        "legs": [
            {
                "departureAirport": "DUB",
                "arrivalAirport": "STN",
                "departureDateTime": "2018-08-21T06:25",
                "arrivalDateTime": "2018-08-21T07:50"
            },
            {
                "departureAirport": "STN",
                "arrivalAirport": "MAD",
                "departureDateTime": "2018-08-21T15:30",
                "arrivalDateTime": "2018-08-21T19:05"
            }
        ]
    },
    {
        "stops": 1,
        "legs": [
            {
                "departureAirport": "DUB",
                "arrivalAirport": "STN",
                "departureDateTime": "2018-08-21T08:20",
                "arrivalDateTime": "2018-08-21T09:40"
            },
            {
                "departureAirport": "STN",
                "arrivalAirport": "MAD",
                "departureDateTime": "2018-08-21T15:30",
                "arrivalDateTime": "2018-08-21T19:05"
            }
        ]
    }
]