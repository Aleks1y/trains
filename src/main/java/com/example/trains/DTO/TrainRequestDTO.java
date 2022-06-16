package com.example.trains.DTO;


import lombok.Data;

import java.util.List;

@Data
public class TrainRequestDTO {
    private Long number;
    private List<TrainTicketsDTO> trainTickets;
    private String station;
    private Long routeNumber;
    private String category;
}
