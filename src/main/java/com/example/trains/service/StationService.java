package com.example.trains.service;

import com.example.trains.entity.Station;
import com.example.trains.exception.StationNotFoundException;
import com.example.trains.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationService {
    @Autowired
    StationRepository stationRepository;

    public Station findByName(String name) throws StationNotFoundException {
        Station station = stationRepository.findByName(name);
        if (station == null){
            throw new StationNotFoundException("Station not found: " + name);
        }
        return station;
    }
}
