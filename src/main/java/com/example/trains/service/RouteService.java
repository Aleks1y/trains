package com.example.trains.service;

import com.example.trains.DTO.RouteDTO;
import com.example.trains.entity.Route;
import com.example.trains.entity.RouteId;
import com.example.trains.entity.Station;
import com.example.trains.exception.ElementNotFoundException;
import com.example.trains.exception.StationNotFoundException;
import com.example.trains.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RouteService {

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    TrainService trainService;

    @Autowired
    StationService stationService;

    public Page<RouteDTO> getAll(Pageable pageable){
        List<Long> routeNumbers = routeRepository.findRoutesNumbers();
        return this.routeNumbersToRouteDtoPages(routeNumbers, pageable);
    }

    public List<Route> findBetweenStations(Station st1, Station st2, long routeNumber) throws ElementNotFoundException {
        Route route1 = routeRepository.findById(new RouteId(routeNumber, st1)).orElse(null);
        Route route2 = routeRepository.findById(new RouteId(routeNumber, st2)).orElse(null);
        if (route1 == null || route2 == null){
            throw new ElementNotFoundException("Станции в маршруте не найдены");
        }
        return routeRepository.findById_RouteNumberAnd_orderBetween(routeNumber, route1.get_order(), route2.get_order());
    }

    public Page<RouteDTO> getByStations(List<String> stations, Pageable pageable) throws StationNotFoundException {
        List<Long> routeNumbers = routeRepository.findRoutesNumbers();
        for (String st: stations){
            Station station = stationService.findByName(st);
            routeNumbers.retainAll(routeRepository.findRoutesNumbersByStation(station));
        }
        return this.routeNumbersToRouteDtoPages(routeNumbers, pageable);
    }

    public void create(Long number, String stationName, int order) throws Exception {
        Station station = stationService.findByName(stationName);
        RouteId routeId = new RouteId(number, station);
        Route route = routeRepository.findById(routeId).orElse(null);
        if (route != null){
            throw new Exception("Эта станция в этом маршруте уже есть");
        }
        route = new Route();
        route.setId(routeId);
        route.set_order(order);
        routeRepository.save(route);
    }

    public void delete(Long number, String stationName) throws Exception {
        Station station = stationService.findByName(stationName);
        RouteId routeId = new RouteId(number, station);
        Route route = routeRepository.findById(routeId).orElse(null);
        if (route == null){
            throw new ElementNotFoundException("Такой станции в этом маршруте нет");
        }
        routeRepository.delete(route);
    }

    public void update(Long number, String stationName, int order) throws Exception {
        Station station = stationService.findByName(stationName);
        RouteId routeId = new RouteId(number, station);
        Route route = routeRepository.findById(routeId).orElse(null);
        if (route == null){
            throw new ElementNotFoundException("Такой станции в этом маршруте нет");
        }
        route.set_order(order);
        routeRepository.save(route);
    }

    public int calcStationCountBetween(Long routeNumber, Station st1, Station st2) {
        Route route1 = routeRepository.findById(new RouteId(routeNumber, st1)).orElse(null);
        Route route2 = routeRepository.findById(new RouteId(routeNumber, st2)).orElse(null);
        return Math.abs(route1.get_order() - route2.get_order());
    }

    private Page<RouteDTO> routeNumbersToRouteDtoPages(List<Long> routeNumbers, Pageable pageable){
        List<RouteDTO> routeDTOList = new LinkedList<>();
        if (routeNumbers != null && !routeNumbers.isEmpty()){
            for (Long routeNumber: routeNumbers){
                List<Route> routeList = routeRepository.findById_RouteNumber(routeNumber);
                routeDTOList.add(RouteDTO.fromEntityList(routeList, trainService.findByRoute(routeNumber)));
            }
        }
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), routeDTOList.size());
        return new PageImpl<>(routeDTOList.subList(start, end), pageable, routeDTOList.size());
    }
}
