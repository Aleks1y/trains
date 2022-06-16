package com.example.trains.DTO;

import com.example.trains.entity.Timetable;
import lombok.Data;

import java.time.Duration;
import java.util.List;

@Data
public class FilteredTimetableResponseDTO {
    private List<TimetableResponseDTO> timetableResponseDTOList;
    private String travelTime;
    private int stationsCount;

    public FilteredTimetableResponseDTO(List<Timetable> timetableList, int stationsCount){
        this.timetableResponseDTOList = timetableList.stream().map(TimetableResponseDTO::fromEntity).toList();
        long travelTimeSeconds = Duration.between(timetableList.get(0).getDt2().toLocalDateTime(),
                timetableList.get(timetableList.size()-1).getDt1().toLocalDateTime()).getSeconds();

        this.travelTime = String.format("%dд:%02dч:%02dмин", travelTimeSeconds / (24 * 60 * 60),
                (travelTimeSeconds % (24 * 60 * 60)) / 3600,
                (travelTimeSeconds % (60 * 60)) / 60);

        this.stationsCount = stationsCount;
    }
}
