package com.example.trains.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "train_tickets",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"type", "train_number"})})
public class TrainTickets  {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String type;
    private Integer count;
    @ManyToOne
    @JoinColumn(name = "train_number")
    private Train train;
}
