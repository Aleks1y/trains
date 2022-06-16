package com.example.trains.DTO;

import com.example.trains.entity.Train;
import lombok.Data;

import java.util.List;


@Data
public class TrainResponseDTO {
    private Long number;
    private List<TrainTicketsDTO> trainTickets;
    private String station;
    private Long routeNumber;
    private String category;

    public static TrainResponseDTO fromTrain(Train train){
        TrainResponseDTO response = new TrainResponseDTO();
        response.setNumber(train.getNumber());
        response.setRouteNumber(train.getRouteNumber());
        response.setCategory(train.getCategory());
        response.setStation(train.getStation().getName());
        response.setTrainTickets(train.getTrainTickets().stream().map(TrainTicketsDTO::fromTrainTickets).toList());
        return response;
    }
}

