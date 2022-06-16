package com.example.trains.DTO;

import lombok.Data;

@Data
public class GenerateRideRequestDTO {
    private Long timetableId;
    private Long trainDepartureId;
    private Long trainNumber;
}
