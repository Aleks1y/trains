package com.example.trains.DTO;

import lombok.Data;

import java.sql.Date;

@Data
public class RegistrationRequestDTO {
    private String email;
    private String password;
    private String passwordConfirm;
    private String fio;
    private Integer passportSeries;
    private Integer passportNumber;
    private Date birthday;
}
