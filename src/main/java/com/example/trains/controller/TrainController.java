package com.example.trains.controller;

import com.example.trains.DTO.TrainRequestDTO;
import com.example.trains.DTO.TrainResponseDTO;
import com.example.trains.exception.ElementNotFoundException;
import com.example.trains.exception.StationNotFoundException;
import com.example.trains.exception.TrainAlreadyExistException;
import com.example.trains.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/train")
@CrossOrigin( origins = "*", maxAge = 3500)
public class TrainController {

    @Autowired
    TrainService trainService;

    @GetMapping("/all")
    public ResponseEntity<?> getTrains(@PageableDefault(sort = {"number"}, direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(trainService.readAllTrains(pageable).map(TrainResponseDTO::fromTrain));
    }

    @GetMapping("/{number}")
    public ResponseEntity<?> getTrainByNumber(@PathVariable Long number){
        try {
            return ResponseEntity.ok(TrainResponseDTO.fromTrain(trainService.findByNumber(number)));
        } catch (ElementNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateTrain(@RequestBody TrainRequestDTO request){
        try {
            trainService.updateTrain(request.getNumber(),
                    request.getTrainTickets(),
                    request.getStation(),
                    request.getRouteNumber(),
                    request.getCategory());
            return ResponseEntity.ok("Train updated");
        } catch (StationNotFoundException | ElementNotFoundException | TrainAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createTrain(@RequestBody TrainRequestDTO request){
        try {
            trainService.createTrain(request.getNumber(),
                    request.getTrainTickets(),
                    request.getStation(),
                    request.getRouteNumber(),
                    request.getCategory());
            return ResponseEntity.ok("Train created");
        } catch (StationNotFoundException | ElementNotFoundException | TrainAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<?> deleteTrain(@PathVariable Long number){
        try {
            trainService.deleteTrain(number);
            return ResponseEntity.ok("Train deleted");
        } catch (ElementNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
