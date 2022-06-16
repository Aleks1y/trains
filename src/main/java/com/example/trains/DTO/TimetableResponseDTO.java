package com.example.trains.DTO;

import com.example.trains.entity.Timetable;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimetableResponseDTO {
    private long id;
    private String station;
    private Long trainDepartureNumber;
    private Integer tickets;
    private LocalDateTime dt1;
    private LocalDateTime dt2;

    public static TimetableResponseDTO fromEntity(Timetable timetable){
        TimetableResponseDTO timetableResponseDTO = new TimetableResponseDTO();
        timetableResponseDTO.setId(timetable.getId());
        timetableResponseDTO.setStation(timetable.getStation().getName());
        timetableResponseDTO.setTickets(timetable.getTickets());
        timetableResponseDTO.setTrainDepartureNumber(timetable.getTrainDeparture().getId());
        timetableResponseDTO.setDt1(timetable.getDt1().toLocalDateTime());
        timetableResponseDTO.setDt2(timetable.getDt2().toLocalDateTime());
        return timetableResponseDTO;
    }
}
