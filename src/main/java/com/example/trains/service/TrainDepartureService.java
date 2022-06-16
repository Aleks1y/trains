package com.example.trains.service;

import com.example.trains.entity.TrainDeparture;
import com.example.trains.exception.ElementNotFoundException;
import com.example.trains.repository.TrainDepartureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainDepartureService {

    @Autowired
    TrainDepartureRepository trainDepartureRepository;

    public void save(TrainDeparture trainDeparture){
        trainDepartureRepository.save(trainDeparture);
    }

    public TrainDeparture findById(Long id) throws ElementNotFoundException {
        TrainDeparture trainDeparture = trainDepartureRepository.findById(id).orElse(null);
        if (trainDeparture == null){
            throw new ElementNotFoundException("Train departure not found");
        }
        return trainDeparture;
    }
}
