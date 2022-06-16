package com.example.trains.service;

import com.example.trains.DTO.TrainTicketsDTO;
import com.example.trains.entity.Train;
import com.example.trains.entity.TrainTickets;
import com.example.trains.exception.ElementNotFoundException;
import com.example.trains.exception.StationNotFoundException;
import com.example.trains.exception.TrainAlreadyExistException;
import com.example.trains.repository.TrainRepository;
import com.example.trains.repository.TrainTicketsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class TrainService {

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    TrainTicketsRepository trainTicketsRepository;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    StationService stationService;

    public Page<Train> readAllTrains(Pageable pageable) {
        return trainRepository.findAll(pageable);
    }

    public void createTrain(Long number,
                            List<TrainTicketsDTO> trainTicketsList,
                            String station,
                            Long routeNumber,
                            String category) throws ElementNotFoundException, TrainAlreadyExistException, StationNotFoundException{
        if (number != null && trainRepository.findById(number).orElse(null) != null){
            throw new TrainAlreadyExistException("This number is already taken: " + number);
        }
        Train train = new Train();
        List<TrainTickets> trainTickets = new LinkedList<>();
        if (trainTicketsList != null){
            for (TrainTicketsDTO trainTicketsDTO: trainTicketsList){
                TrainTickets trainTicket = new TrainTickets();
                trainTicket.setTrain(train);
                trainTicket.setType(trainTicketsDTO.getType());
                trainTicket.setCount(trainTicketsDTO.getCount());
                trainTickets.add(trainTicket);
            }
        }
        train.setTrainTickets(trainTickets);
        train.setNumber(number);
        train.setStation(stationService.findByName(station));
        train.setRouteNumber(routeNumber);
        train.setCategory(category);
        trainRepository.save(train);
    }

    public void updateTrain(Long number,
                            List<TrainTicketsDTO> trainTicketsList,
                            String station,
                            Long routeNumber,
                            String category) throws ElementNotFoundException, TrainAlreadyExistException, StationNotFoundException {
        Train train = this.findByNumber(number);
        List<TrainTickets> newTrainTicketsList = new LinkedList<>();
        if (trainTicketsList != null && !trainTicketsList.isEmpty()){
            for (TrainTicketsDTO trainTicketsDTO: trainTicketsList){
                TrainTickets trainTickets = trainTicketsRepository.findByTypeAndTrain(trainTicketsDTO.getType(), train);
                if (trainTickets == null){
                    trainTickets = new TrainTickets();
                    trainTickets.setTrain(train);
                    trainTickets.setType(trainTicketsDTO.getType());
                }
                trainTickets.setCount(trainTicketsDTO.getCount());
                newTrainTicketsList.add(trainTickets);
            }
        }
        train.setTrainTickets(newTrainTicketsList);
        train.setStation(stationService.findByName(station));
        train.setRouteNumber(routeNumber);
        train.setCategory(category);
        trainRepository.save(train);
    }

    public void deleteTrain(Long number) throws ElementNotFoundException {
        Train train = this.findByNumber(number);
        train.getTrainDepartures().forEach((trainDeparture -> trainDeparture.setTrain(null)));
        trainRepository.delete(train);
    }

    public List<Train> findByRoute(Long number) {
        return trainRepository.findByRouteNumber(number);
    }

    public Train findByNumber(Long number) throws ElementNotFoundException {
        Train train = trainRepository.findById(number).orElse(null);
        if (train == null){
            throw new ElementNotFoundException("Train not found");
        }
        return train;
    }
}
