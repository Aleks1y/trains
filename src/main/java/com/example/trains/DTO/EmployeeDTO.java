package com.example.trains.DTO;

import com.example.trains.entity.Employee;
import com.example.trains.entity.TrainDeparture;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;


@Data
public class EmployeeDTO {
    private Long id;
    private String FIO;
    private String station;
    private String place;
    private List<Long> trainDepartures = new LinkedList<>();

    public static EmployeeDTO fromEmployee(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setFIO(employee.getFIO());
        employeeDTO.setPlace(employee.getPlace());
        employeeDTO.setStation(employee.getStation().getName());
        employee.getTrainDepartures().forEach(employeeDTO::addTrainDeparture);
        return employeeDTO;
    }

    public void addTrainDeparture(TrainDeparture trainDeparture){
        this.trainDepartures.add(trainDeparture.getId());
    }
}
