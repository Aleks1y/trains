package com.example.trains.service;

import com.example.trains.entity.Passenger;
import com.example.trains.exception.ElementNotFoundException;
import com.example.trains.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class PassangerService {
    @Autowired
    PassengerRepository passengerRepository;

    public Passenger findByPassportSeriesAndPassportNumber(int passportSeries, int passportNumber){
        Passenger passenger = passengerRepository.findByPassportSeriesAndPassportNumber(passportSeries, passportNumber);
        return passenger;
    }

    public Passenger create(String fio, int passportSeries, int passportNumber, Date birthday) throws Exception {
        Passenger passenger = this.findByPassportSeriesAndPassportNumber(passportSeries, passportNumber);
        if (passenger != null){
            throw new Exception("Пользователь уже существует");
        }
        passenger = new Passenger();
        passenger.setBirthday(birthday);
        passenger.setFIO(fio);
        passenger.setPassportSeries(passportSeries);
        passenger.setPassportNumber(passportNumber);
        passengerRepository.save(passenger);
        return passenger;
    }
}
