package com.example.trains.repository;

import com.example.trains.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Passenger findByPassportSeriesAndPassportNumber(int passportSeries, int passportNumber);
}
