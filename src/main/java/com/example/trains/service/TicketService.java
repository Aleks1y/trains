package com.example.trains.service;

import com.example.trains.entity.*;
import com.example.trains.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    TimetableService timetableService;

    @Autowired
    RouteService routeService;

    @Autowired
    TicketRepository ticketRepository;

    public Ticket buyTicket(Timetable timetableStart, Timetable timetableEnd, Passenger passenger, String type) throws Exception {
        if (!timetableStart.getTrainDeparture().getId().equals(timetableEnd.getTrainDeparture().getId())){
            throw new Exception("Расписания ссылаются на разные маршруты");
        }
        Ticket ticket = new Ticket();
        ticket.setPassenger(passenger);
        ticket.setStart(timetableStart);
        ticket.setEnd(timetableEnd);
        ticket.setType(type);
        List<Route> routeList = routeService.findBetweenStations(timetableStart.getStation(), timetableEnd.getStation(), timetableStart.getTrainDeparture().getTrain().getRouteNumber());
        if (routeList == null || routeList.isEmpty()){
            throw new Exception("Введены некорректные станции");
        }
        for (Route route: routeList){
            Timetable timetable = timetableService.findByStationAndTrainDeparture(route.getId().getStation(), timetableStart.getTrainDeparture());
            timetableService.decreaseTickets(timetable, type);
        }
        ticketRepository.save(ticket);
        return ticket;
    }
}
