package com.example.trains.repository;

import com.example.trains.entity.TrainDeparture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainDepartureRepository extends JpaRepository<TrainDeparture, Long> {
}
