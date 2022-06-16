package com.example.trains.repository;

import com.example.trains.entity.Route;
import com.example.trains.entity.RouteId;
import com.example.trains.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, RouteId> {
    @Query("select distinct r.id.routeNumber from Route r order by r.id.routeNumber")
    List<Long> findRoutesNumbers();

    @Query("select distinct r.id.routeNumber from Route r where r.id.station = :st order by r.id.routeNumber")
    List<Long> findRoutesNumbersByStation(@Param("st") Station station);

    List<Route> findById_RouteNumber(Long routeNumber);

    @Query("select  r from Route r where r.id.routeNumber = :rn and r._order < :max and r._order >= :min order by r._order")
    List<Route> findById_RouteNumberAnd_orderBetween(@Param("rn")long routeNumber, @Param("min")int minOrder, @Param("max")int maxOrder);
}
