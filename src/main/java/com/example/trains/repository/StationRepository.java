package com.example.trains.repository;

import com.example.trains.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {
    Station findByName(String name);
}
