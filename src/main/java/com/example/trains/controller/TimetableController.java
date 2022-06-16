package com.example.trains.controller;

import com.example.trains.DTO.FilteredTimetableResponseDTO;
import com.example.trains.DTO.GenerateRideRequestDTO;
import com.example.trains.DTO.FilteredTimetableRequestDTO;
import com.example.trains.DTO.TimetableResponseDTO;
import com.example.trains.entity.Station;
import com.example.trains.entity.Timetable;
import com.example.trains.entity.Train;
import com.example.trains.entity.TrainDeparture;
import com.example.trains.exception.ElementNotFoundException;
import com.example.trains.exception.StationFilersAreEmptyException;
import com.example.trains.exception.StationNotFoundException;
import com.example.trains.service.StationService;
import com.example.trains.service.TimetableService;
import com.example.trains.service.TrainDepartureService;
import com.example.trains.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timetable")
@CrossOrigin( origins = "*", maxAge = 3500)
public class TimetableController {
    @Autowired
    StationService stationService;

    @Autowired
    TimetableService timetableService;

    @Autowired
    TrainDepartureService trainDepartureService;

    @Autowired
    TrainService trainService;

    @GetMapping("/all")
    public ResponseEntity<?> getTimetable(@PageableDefault(sort = {"trainDeparture", "dt2"}, direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.ok(timetableService.getAll(pageable).map(TimetableResponseDTO::fromEntity));
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getStationTimetable(@PathVariable String name,
                                                 @PageableDefault(sort = {"dt2"}, direction = Sort.Direction.DESC) Pageable pageable){
        try {
            Station station = stationService.findByName(name);
            Page<Timetable> timetablesList = timetableService.findByStation(station, pageable);
            return ResponseEntity.ok(timetablesList.map(TimetableResponseDTO::fromEntity));
        } catch (StationNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/departure/{number}")
    public ResponseEntity<?> getStationTimetable(@PathVariable long number,
                                                 @PageableDefault(sort = {"dt2"}, direction = Sort.Direction.DESC) Pageable pageable){
        try {
            TrainDeparture trainDeparture = trainDepartureService.findById(number);
            List<Timetable> timetablesList = timetableService.findByTrainDeparture(trainDeparture);
            return ResponseEntity.ok(timetablesList.stream().map(TimetableResponseDTO::fromEntity));
        } catch (ElementNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getFilteredTimetable(@RequestBody FilteredTimetableRequestDTO request,
                                                  @PageableDefault(sort = {"number"}, direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            Page<FilteredTimetableResponseDTO> timetablesPages = timetableService.get(request.getStationFilters(), request.getMaxWaitingSum(), request.getTrainNumbers(), pageable);
            return ResponseEntity.ok(timetablesPages);
        } catch (StationNotFoundException | StationFilersAreEmptyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateRide(@RequestBody GenerateRideRequestDTO request){
        try {
            if (request.getTrainNumber() == null){
                throw new IllegalArgumentException("Train number is null");
            }
            TrainDeparture trainDeparture;
            if (request.getTrainDepartureId() != null) {
                trainDeparture = trainDepartureService.findById(request.getTrainDepartureId());
            }
            else if (request.getTimetableId() != null){
                trainDeparture = timetableService.findById(request.getTimetableId()).getTrainDeparture();
            }
            else {
                throw new IllegalArgumentException("Train departure id and timetable id are null");
            }
            Train train = trainService.findByNumber(request.getTrainNumber());
            List<Timetable> timetablesList = timetableService.generateTimetablesByTrainDeparture(trainDeparture, train);
            return ResponseEntity.ok(timetablesList.stream().map(TimetableResponseDTO::fromEntity).toList());
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
