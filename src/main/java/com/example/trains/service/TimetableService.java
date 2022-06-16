package com.example.trains.service;

import com.example.trains.DTO.FilteredTimetableResponseDTO;
import com.example.trains.entity.*;
import com.example.trains.exception.ElementNotFoundException;
import com.example.trains.exception.StationFilersAreEmptyException;
import com.example.trains.exception.StationNotFoundException;
import com.example.trains.pojo.StationFilter;
import com.example.trains.repository.TimetableRepository;
import org.hibernate.type.LocalDateTimeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class TimetableService {

    @Autowired
    TimetableRepository timetableRepository;

    @Autowired
    StationService stationService;

    @Autowired
    TrainDepartureService trainDepartureService;

    @Autowired
    RouteService routeService;

    public Page<Timetable> getAll(Pageable pageable) {
        return timetableRepository.findAll(pageable);
    }

    public Page<Timetable> findByStation(Station station, Pageable pageable) {
        return timetableRepository.findByStationAndDt2GreaterThan(station, Timestamp.valueOf(LocalDateTime.now()), pageable);
    }

    public List<Timetable> findByTrainDeparture(TrainDeparture trainDeparture) throws ElementNotFoundException {
        List<Timetable> timetables = timetableRepository.findByTrainDepartureOrderByDt1(trainDeparture);
        if (timetables == null || timetables.isEmpty()){
            throw new ElementNotFoundException("Не найдено расписание с данным отправлением");
        }
        return timetables;
    }

    public void save(Timetable timetable){
        timetableRepository.save(timetable);
    }

    public Timetable findById(Long id) throws ElementNotFoundException {
        Timetable timetable = timetableRepository.findById(id).orElse(null);
        if (timetable == null){
            throw new ElementNotFoundException("Timetable not found");
        }
        return timetable;
    }

    public Page<FilteredTimetableResponseDTO> get(List<StationFilter> stationFilters, Long maxWaitingSum, List<Long> trainNumbers, Pageable pageable) throws StationNotFoundException, StationFilersAreEmptyException {
        if (stationFilters == null || stationFilters.isEmpty()) {
            throw new StationFilersAreEmptyException("station filers are empty");
        }
        List<Station> stations = new LinkedList<>();
        List<TrainDeparture> trainDepartures = new LinkedList<>();
        for (int i = 0; i < stationFilters.size(); i++) {
            StationFilter curStationFilter = stationFilters.get(i);
            Station curStation = stationService.findByName(curStationFilter.getName());
            stations.add(curStation);
            if (curStationFilter.getDepartureTimeMin() == null && curStationFilter.getDepartureTimeMax() == null && curStationFilter.getArrivalTimeMin() == null && curStationFilter.getArrivalTimeMax() == null){
                curStationFilter.setDepartureTimeMin(Timestamp.valueOf(LocalDateTime.now()));
            }
            List<Timetable> curTimetables = timetableRepository.findByStationAndDt1BetweenAndDt2BetweenOrderByDt2(curStation
                    , curStationFilter.getArrivalTimeMin(), curStationFilter.getArrivalTimeMax()
                    , curStationFilter.getDepartureTimeMin(), curStationFilter.getDepartureTimeMax());
            List<TrainDeparture> curTrainDepartures = curTimetables.stream().map(Timetable::getTrainDeparture).toList();
            if (i == 0) {
                trainDepartures.addAll(curTrainDepartures);
            }
            trainDepartures.retainAll(curTrainDepartures);
        }
        for (TrainDeparture trainDeparture: trainDepartures){
            if (trainDeparture.getTrain() == null || !trainNumbers.isEmpty() && !trainNumbers.contains(trainDeparture.getTrain().getNumber())){
                trainDepartures.remove(trainDeparture);
                continue;
            }
            if (maxWaitingSum != null){
                long waitingSum = 0L;
                for (Waiting waiting: trainDeparture.getWaitingList()){
                    waitingSum += (waiting.getNewDate().getTime() - waiting.getOldDate().getTime()) / 1000;
                }
                if (waitingSum > maxWaitingSum){
                    trainDepartures.remove(trainDeparture);
                }
            }
        }
        List<FilteredTimetableResponseDTO> timetablesList = new LinkedList<>();
        trainDepartures.forEach(trainDeparture -> {
            List<Timetable> timetables = timetableRepository.findByStationInAndTrainDepartureOrderByDt2(stations, trainDeparture);
            int stationsCount = routeService.calcStationCountBetween(trainDeparture.getTrain().getRouteNumber(),
                    timetables.get(0).getStation(),
                    timetables.get(timetables.size() - 1).getStation());
            timetablesList.add(new FilteredTimetableResponseDTO(timetables, stationsCount));
        });
        if (pageable.getSort().toString().startsWith("travelTime")){
            timetablesList.sort(Comparator.comparing(FilteredTimetableResponseDTO::getTravelTime));
        } else if (pageable.getSort().toString().startsWith("stationsCount")){
            timetablesList.sort(Comparator.comparing(FilteredTimetableResponseDTO::getStationsCount));
        }
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), timetablesList.size());
        return new PageImpl<>(timetablesList.subList(start, end), pageable, timetablesList.size());
    }

    public List<Timetable> generateTimetablesByTrainDeparture(TrainDeparture previousTrainDeparture, Train curTrain) throws ElementNotFoundException {
        List<Timetable> previousTimetables = this.findByTrainDeparture(previousTrainDeparture);
        TrainDeparture curTrainDeparture = new TrainDeparture();
        curTrainDeparture.setTrain(curTrain);
        trainDepartureService.save(curTrainDeparture);
        int dayOffset = previousTimetables.get(0).getDt1().toLocalDateTime().toLocalDate().lengthOfMonth();
        List<Timetable> newTimetables = new LinkedList<>();
        previousTimetables.forEach(previousTimetable -> {
            Timetable curTimetable = new Timetable();
            if (previousTimetables.indexOf(previousTimetable) != previousTimetables.size() - 1){
                System.out.println(curTrain.getTrainTickets().size());
                curTimetable.setTickets(curTrain.getQuantity());
            }
            curTimetable.setStation(previousTimetable.getStation());
            curTimetable.setTrainDeparture(curTrainDeparture);
            LocalDateTime previousDt1 = previousTimetable.getDt1().toLocalDateTime();
            LocalDateTime previousDt2 = previousTimetable.getDt2().toLocalDateTime();
            curTimetable.setDt1(Timestamp.valueOf(previousDt1.plusDays(dayOffset)));
            curTimetable.setDt2(Timestamp.valueOf(previousDt2.plusDays(dayOffset)));
            newTimetables.add(curTimetable);
            this.save(curTimetable);
        });
        newTimetables.get(newTimetables.size() - 1).setTickets(null);
        return newTimetables;
    }

    public Timetable findByStationAndTrainDeparture(Station station, TrainDeparture trainDeparture){
        return timetableRepository.findByStationAndTrainDeparture(station, trainDeparture);
    }

    public void decreaseTickets(Timetable timetable, String ticketType) throws Exception {
        if (timetable.getTickets() == 0){
            throw new Exception("Все билеты проданы");
        }
        timetable.setTickets(timetable.getTickets() - 1);
        timetableRepository.save(timetable);
    }
}
