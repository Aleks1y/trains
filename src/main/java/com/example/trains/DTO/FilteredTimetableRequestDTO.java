package com.example.trains.DTO;

import com.example.trains.pojo.StationFilter;
import lombok.Data;

import java.util.List;

@Data
public class FilteredTimetableRequestDTO {
    private List<StationFilter> stationFilters;
    private List<Long> trainNumbers;
    private Long maxWaitingSum;
}
