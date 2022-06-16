package com.example.trains.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long number;

    @ManyToOne
    private Passenger passenger;

    @ManyToOne
    private Timetable start;
    @ManyToOne
    private Timetable end;

    private String type;
}
