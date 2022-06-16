package com.example.trains.repository;

import com.example.trains.entity.Train;
import com.example.trains.entity.TrainTickets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainTicketsRepository extends JpaRepository<TrainTickets, Long> {
    TrainTickets findByTypeAndTrain(String type, Train train);
}
