package com.example.trains.pojo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class StationFilter {
    private String name;
    private Timestamp arrivalTimeMin;
    private Timestamp arrivalTimeMax;
    private Timestamp departureTimeMin;
    private Timestamp departureTimeMax;
}
