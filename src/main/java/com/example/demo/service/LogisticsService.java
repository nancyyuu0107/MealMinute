package com.example.demo.service;

import com.example.demo.model.Logistics;

import java.util.List;

public interface LogisticsService {

    List<Logistics> getAllLogistics();

    Logistics getLogisticsById(Integer logisticsId);

    Integer createLogistics(Logistics logistics);

    void updateLogistics(Integer logisticsId, Logistics logistics);

    void deleteLogistics(Integer logisticsId);



}
