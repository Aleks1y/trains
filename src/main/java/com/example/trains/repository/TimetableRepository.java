package com.example.trains.repository;

import com.example.trains.entity.Station;
import com.example.trains.entity.Timetable;
import com.example.trains.entity.TrainDeparture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    @Query("select t from Timetable t where t.station = :station and (cast(:minDt2 as timestamp) is null or t.dt2 >= :minDt2) order by dt2")
    Page<Timetable> findByStationAndDt2GreaterThan(Station station, @Param("minDt2") Timestamp minDt2, Pageable pageable);

    Page<Timetable> findAll(Pageable pageable);

    @Query("select t from Timetable t where t.station = :station and (cast(:minDt1 as timestamp) is null or t.dt1 >= :minDt1) " +
            "and (cast(:maxDt1 as timestamp) is null or t.dt1 <= :maxDt1) and (cast(:minDt2 as timestamp) is null or t.dt2 >= :minDt2)" +
            "and (cast(:maxDt2 as timestamp) is null or t.dt2 <= :maxDt2) order by dt2")
    List<Timetable> findByStationAndDt1BetweenAndDt2BetweenOrderByDt2(@Param("station") Station station, @Param("minDt1") Timestamp minDt1
            , @Param("maxDt1") Timestamp maxDt1, @Param("minDt2") Timestamp minDt2, @Param("maxDt2") Timestamp maxDt2);

    Timetable findByStationAndTrainDeparture(Station station, TrainDeparture trainDeparture);

    List<Timetable> findByStationInAndTrainDepartureOrderByDt2(List<Station> stations, TrainDeparture trainDeparture);

    List<Timetable> findByTrainDepartureOrderByDt1(TrainDeparture trainDeparture);

}