package com.example.demo.dao;

import com.example.demo.model.Logistics;

import java.util.List;

public interface LogisticsDao {

    List<Logistics> getAllLogistics();

    Logistics getLogisticsById(Integer logisticsId);

    Integer createLogistics(Logistics logistics);

    void updateLogistics(Integer logisticsId, Logistics logistics);

    void deleteLogistics(Integer logisticsId);  // 返回影響的行數
}
