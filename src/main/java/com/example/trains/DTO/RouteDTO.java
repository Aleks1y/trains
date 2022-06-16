package com.example.trains.DTO;

import com.example.trains.entity.Route;
import com.example.trains.entity.Train;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class RouteDTO {
    @Data
    @AllArgsConstructor
    private static class StationOrder{
        private int order;
        private String stationName;
    }
    private Long routeNumber;
    private List<StationOrder> stationOrders = new LinkedList<>();
    private List<Long> trains = new LinkedList<>();

    public static RouteDTO fromEntityList(List<Route> routeList, List<Train> trainList){
        RouteDTO routeDTO = new RouteDTO();
        routeDTO.setRouteNumber(routeList.get(0).getId().getRouteNumber());
        routeList.forEach(routeDTO::addStationOrder);
        if (trainList != null && !trainList.isEmpty()){
            trainList.forEach(routeDTO::addTrain);
        }
        return routeDTO;
    }

    private void addStationOrder(Route route){
        this.stationOrders.add(new StationOrder(route.get_order(), route.getId().getStation().getName()));
    }

    private void addTrain(Train train){
        this.trains.add(train.getNumber());
    }
}
