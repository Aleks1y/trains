package com.example.trains.repository;

import com.example.trains.entity.Train;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainRepository extends JpaRepository<Train, Long> {
    Page<Train> findAll(Pageable pageable);
    List<Train> findByRouteNumber(Long routeNumber);
}
