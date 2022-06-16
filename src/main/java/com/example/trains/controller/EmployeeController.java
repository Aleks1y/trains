package com.example.trains.controller;

import com.example.trains.DTO.EmployeeAddTrainDepartureDTO;
import com.example.trains.DTO.EmployeeDTO;
import com.example.trains.exception.ElementNotFoundException;
import com.example.trains.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@CrossOrigin( origins = "*", maxAge = 3500)
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(EmployeeDTO.fromEmployee(employeeService.findById(id)));
        } catch (ElementNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getEmployees(@PageableDefault(sort = {"FIO"}) Pageable pageable){
        return ResponseEntity.ok(employeeService.getAll(pageable).map(EmployeeDTO::fromEmployee));
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDTO request){
        try {
            employeeService.create(request.getFIO(), request.getStation(), request.getPlace());
            return ResponseEntity.ok("Добавлено");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateEmployee(@RequestBody EmployeeDTO request){
        try {
            employeeService.update(request.getId(), request.getFIO(), request.getStation(), request.getPlace());
            return ResponseEntity.ok("Добавлено");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/addTrainDeparture")
    public ResponseEntity<?> addTrainDepartureToEmployee(@RequestBody EmployeeAddTrainDepartureDTO request){
        try {
            employeeService.addTrainDeparture(request.getEmployeeId(), request.getTrainDepartureId());
            return ResponseEntity.ok("Добавлено");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteTrainDeparture")
    public ResponseEntity<?> deleteTrainDepartureToEmployee(@RequestBody EmployeeAddTrainDepartureDTO request){
        try {
            employeeService.removeTrainDeparture(request.getEmployeeId(), request.getTrainDepartureId());
            return ResponseEntity.ok("Удалено");
        } catch (ElementNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id){
        try {
            employeeService.delete(id);
            return ResponseEntity.ok("Работник удалён");
        } catch (ElementNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
