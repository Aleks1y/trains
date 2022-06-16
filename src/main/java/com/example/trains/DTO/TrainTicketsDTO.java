package com.example.trains.DTO;

import com.example.trains.entity.TrainTickets;
import lombok.Data;

@Data
public class TrainTicketsDTO {
    private String type;
    private Integer count;

    public static TrainTicketsDTO fromTrainTickets(TrainTickets trainTickets){
        TrainTicketsDTO trainTicketsDTO = new TrainTicketsDTO();
        trainTicketsDTO.setCount(trainTickets.getCount());
        trainTicketsDTO.setType(trainTickets.getType());
        return trainTicketsDTO;
    }
}
