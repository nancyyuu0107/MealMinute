package com.example.demo.controller;

import com.example.demo.model.Logistics;
import com.example.demo.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class LogisticsController {

    @Autowired
    private LogisticsService logisticsService;


    @GetMapping("/logistics")
    public ResponseEntity<List<Logistics>> getAllLogistics() {
        List<Logistics> logisticsList = logisticsService.getAllLogistics();
        return ResponseEntity.ok(logisticsList);
    }

    @GetMapping("/logistics/{logisticsId}")
    public ResponseEntity<Logistics> getLogistics(@PathVariable Integer logisticsId) {
        Logistics logistics = logisticsService.getLogisticsById(logisticsId);
        if (logistics != null) {
            return ResponseEntity.ok(logistics);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found when logistics not found
        }
    }

    @PostMapping("/logistics")
    public ResponseEntity<Logistics> createLogistics(@RequestBody Logistics logistics) {
        logisticsService.createLogistics(logistics);
        return ResponseEntity.status(HttpStatus.CREATED).body(logistics);
    }

    @PutMapping("/logistics/{logisticsId}")
    public ResponseEntity<Logistics> updateLogistics(@PathVariable Integer logisticsId, @RequestBody Logistics logistics) {
        Logistics existingLogistics = logisticsService.getLogisticsById(logisticsId);
        if (existingLogistics != null) {
            // 設置正確的 ID
            logistics.setLogisticsID(logisticsId);

            logisticsService.updateLogistics(logisticsId, logistics);
            return ResponseEntity.ok(logistics);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found when logistics not found
        }
    }


    @DeleteMapping("/logistics/{logisticsId}")
    public ResponseEntity<Void> deleteLogistics(@PathVariable Integer logisticsId) {
        logisticsService.deleteLogistics(logisticsId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content when successfully deleted
    }
}
