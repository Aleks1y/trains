package com.example.trains.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TrainDeparture {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Train train;
    @OneToMany(mappedBy = "trainDeparture")
    List<Waiting> waitingList;
    @ManyToMany(mappedBy = "trainDepartures")
    private List<Employee> employees;
}
