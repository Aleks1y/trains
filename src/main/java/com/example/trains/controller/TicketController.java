package com.example.trains.controller;

import com.example.trains.DTO.BuyTicketDTO;
import com.example.trains.DTO.TickeDTO;
import com.example.trains.entity.*;
import com.example.trains.service.PassangerService;
import com.example.trains.service.RegisteredUserService;
import com.example.trains.service.TicketService;
import com.example.trains.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/ticket")
@CrossOrigin( origins = "*", maxAge = 3500)
public class TicketController {

    @Autowired
    RegisteredUserService registeredUserService;

    @Autowired
    TicketService ticketService;

    @Autowired
    TimetableService timetableService;

    @Autowired
    PassangerService passangerService;

    @PostMapping("/buy")
    public ResponseEntity<?> buyTicket(@RequestBody BuyTicketDTO request, Principal principal){
        try {
            Timetable timetableStart = timetableService.findById(request.getTimetableStartId());
            Timetable timetableEnd = timetableService.findById(request.getTimetableEndId());
            System.out.println(request);

            Passenger passenger;
            if (principal != null && registeredUserService.findByMail(principal.getName()).getPassenger() != null){
                passenger = registeredUserService.findByMail(principal.getName()).getPassenger();
            }
            else {
                passenger = passangerService.findByPassportSeriesAndPassportNumber(request.getPassportSeries(), request.getPassportNumber());
                if (passenger != null &&( !passenger.getBirthday().toString().equals(request.getBirthday().toString()) || !passenger.getFIO().equals(request.getFio()))) {
                    System.out.println(passenger.getBirthday().compareTo(request.getBirthday()));
                    System.out.println(request.getBirthday().compareTo(passenger.getBirthday()));
                    System.out.println(request.getBirthday());
                    System.out.println(passenger.getBirthday());
                    System.out.println(!passenger.getFIO().equals(request.getFio()));
                    return ResponseEntity.badRequest().body("Пользователь с таким паспортом и другими данными уже существует");
                }
                if (passenger == null){
                    passenger = passangerService.create(request.getFio(), request.getPassportSeries(), request.getPassportNumber(), request.getBirthday());
                }
            }
            Ticket ticket = ticketService.buyTicket(timetableStart, timetableEnd, passenger, request.getType());
            return ResponseEntity.ok(new TickeDTO(ticket.getNumber()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
