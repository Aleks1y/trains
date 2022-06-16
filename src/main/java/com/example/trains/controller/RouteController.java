package com.example.trains.controller;

import com.example.trains.DTO.RouteRequestDTO;
import com.example.trains.DTO.RoutesByStationsRequestDTO;
import com.example.trains.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/route")
@CrossOrigin( origins = "*", maxAge = 3500)
public class RouteController {

    @Autowired
    RouteService routeService;

    @GetMapping("/all")
    public ResponseEntity<?> getRoutes(@PageableDefault Pageable pageable){
        return ResponseEntity.ok(routeService.getAll(pageable));
    }

    @GetMapping
    public ResponseEntity<?> getRoutesByStations(@RequestBody RoutesByStationsRequestDTO request, @PageableDefault Pageable pageable){
        try {
            return ResponseEntity.ok(routeService.getByStations(request.getStations(), pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createRoute(@RequestBody RouteRequestDTO request){
        try {
            routeService.create(request.getRouteNumber(), request.getStationName(), request.getOrder());
            return ResponseEntity.ok("Новая запись добавлена");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateRoute(@RequestBody RouteRequestDTO request){
        try {
            routeService.update(request.getRouteNumber(), request.getStationName(), request.getOrder());
            return ResponseEntity.ok("Запись изменена");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRoute(@RequestBody RouteRequestDTO request){
        try {
            routeService.delete(request.getRouteNumber(), request.getStationName());
            return ResponseEntity.ok("Запись удалена");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
