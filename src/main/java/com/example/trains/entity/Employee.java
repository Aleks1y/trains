package com.example.trains.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String FIO;
    @ManyToOne
    private Station station;
    private String place;
    @ManyToMany
    private List<TrainDeparture> trainDepartures;

    public void addTrainDeparture(TrainDeparture trainDeparture) {
        this.trainDepartures.add(trainDeparture);
    }
}
