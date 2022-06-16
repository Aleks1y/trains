package com.example.trains.service;

import com.example.trains.entity.Employee;
import com.example.trains.entity.Station;
import com.example.trains.entity.Timetable;
import com.example.trains.entity.TrainDeparture;
import com.example.trains.exception.ElementNotFoundException;
import com.example.trains.exception.StationNotFoundException;
import com.example.trains.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    TrainDepartureService trainDepartureService;

    @Autowired
    TimetableService timetableService;

    @Autowired
    StationService stationService;

    public Employee findById(Long id) throws ElementNotFoundException {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null){
            throw new ElementNotFoundException("Employee not found: " + id);
        }
        return employee;
    }

    public void delete(long id) throws ElementNotFoundException {
        Employee employee = this.findById(id);
        employeeRepository.delete(employee);
    }

    public void create(String fio, String stationName, String place) throws ElementNotFoundException, StationNotFoundException {
        Employee employee = new Employee();
        employee.setFIO(fio);
        Station station = stationService.findByName(stationName);
        employee.setStation(station);
        employee.setPlace(place);
        employeeRepository.save(employee);
    }

    public Page<Employee> getAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public void update(long employeeId, String newFio, String newStationName, String newPlace) throws ElementNotFoundException, StationNotFoundException {
        Employee employee = this.findById(employeeId);
        employee.setFIO(newFio);
        Station station = stationService.findByName(newStationName);
        employee.setStation(station);
        employee.setPlace(newPlace);
        employeeRepository.save(employee);
    }
    public void addTrainDeparture(long employeeId, long newTrainDepartureId) throws Exception {
        Employee employee = this.findById(employeeId);
        TrainDeparture newTrainDeparture = trainDepartureService.findById(newTrainDepartureId);
        List<Timetable> timetables = timetableService.findByTrainDeparture(newTrainDeparture);
        Timestamp dtStart = timetables.get(0).getDt2();
        Timestamp dtEnd = timetables.get(timetables.size() - 1).getDt1();
        for (TrainDeparture trainDeparture: employee.getTrainDepartures()){
            List<Timetable> timetables1 = timetableService.findByTrainDeparture(trainDeparture);
            Timestamp dtStart1 = timetables1.get(0).getDt2();
            Timestamp dtEnd1 = timetables1.get(timetables1.size() - 1).getDt1();
            if (!(dtEnd.before(dtStart1) || dtStart.after(dtEnd1))){
                throw new Exception("Работник занят в это время на другой поездке");
            }
        }
        if (employee.getTrainDepartures().contains(newTrainDeparture)){
            return;
        }
        employee.addTrainDeparture(newTrainDeparture);
        employeeRepository.save(employee);
    }

    public void removeTrainDeparture(long employeeId, long trainDepartureId) throws ElementNotFoundException {
        Employee employee = this.findById(employeeId);
        TrainDeparture trainDeparture = trainDepartureService.findById(trainDepartureId);
        if (!employee.getTrainDepartures().contains(trainDeparture)){
            return;
        }
        employee.getTrainDepartures().remove(trainDeparture);
        employeeRepository.save(employee);
    }
}
