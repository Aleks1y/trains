package com.example.trains.DTO;

import lombok.Data;

import java.sql.Date;

@Data
public class BuyTicketDTO {
    private long timetableStartId;
    private long timetableEndId;
    private String type;
    private String fio;
    private Integer passportSeries;
    private Integer passportNumber;
    private Date birthday;
}
