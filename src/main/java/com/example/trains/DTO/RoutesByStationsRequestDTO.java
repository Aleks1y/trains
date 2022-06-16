package com.example.trains.DTO;

import lombok.Data;

import java.util.List;

@Data
public class RoutesByStationsRequestDTO {
    private List<String> stations;
}
