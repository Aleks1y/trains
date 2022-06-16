package com.example.trains.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@Table(name = "passenger", uniqueConstraints = {@UniqueConstraint(columnNames = {"passport_number", "passport_series"})})
public class Passenger {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "passport_series")
    private Integer passportSeries;
    @Column(name = "passport_number")
    private Integer passportNumber;
    private Date birthday;
    private String FIO;
}
