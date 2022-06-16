package com.example.trains.repository;

import com.example.trains.entity.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
}
