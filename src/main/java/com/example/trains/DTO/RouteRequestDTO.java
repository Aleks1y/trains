package com.example.trains.DTO;

import lombok.Data;

@Data
public class RouteRequestDTO {
    Long routeNumber;
    String stationName;
    int order;
}
