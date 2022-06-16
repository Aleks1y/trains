package com.example.trains.controller;


import com.example.trains.entity.Passenger;
import com.example.trains.entity.RegisteredUser;
import com.example.trains.entity.Role;
import com.example.trains.DTO.RegistrationRequestDTO;
import com.example.trains.repository.PassengerRepository;
import com.example.trains.repository.RegisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@CrossOrigin( origins = "*", maxAge = 3500)
@RestController
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    RegisteredUserRepository registeredUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PassengerRepository passengerRepository;

    @PostMapping()
    public ResponseEntity<?> register(@RequestBody RegistrationRequestDTO request){
        if  (request.getBirthday() == null || request.getEmail() == null || request.getFio() == null || request.getPassword() == null || request.getPasswordConfirm() == null || request.getPassportNumber() == null || request.getPassportSeries() == null){
            return ResponseEntity.badRequest().body("Не все поля заполнены");
        }
        if (request.getFio().split(" ").length != 3){
            return ResponseEntity.badRequest().body("ФИО: 3 слова должны быть разделены пробелом");
        }
        if (registeredUserRepository.findByMail(request.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Пользователь с такой почтой уже существует");
        }
        if(!request.getPassword().equals(request.getPasswordConfirm())) {
            return ResponseEntity.badRequest().body("Пароли не совпадают!");
        }
        Passenger passenger = passengerRepository.findByPassportSeriesAndPassportNumber(request.getPassportSeries(), request.getPassportNumber());
        if (passenger == null){
            passenger = new Passenger();
            passenger.setBirthday(request.getBirthday());
            passenger.setFIO(request.getFio());
            passenger.setPassportSeries(request.getPassportSeries());
            passenger.setPassportNumber(request.getPassportNumber());
            passengerRepository.save(passenger);
        }else if (!passenger.getBirthday().equals(request.getBirthday()) || !passenger.getFIO().equals(request.getFio())){
            return ResponseEntity.badRequest().body("Пользователь с таким паспортом и другими данными уже существует");
        }
        RegisteredUser user = new RegisteredUser();
        user.setMail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPassenger(passenger);
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        registeredUserRepository.save(user);
        return ResponseEntity.ok("Пользователь зарегистрирован!");
    }
}

