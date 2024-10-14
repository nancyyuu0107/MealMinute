package com.example.demo.service.impl;

import com.example.demo.dao.LogisticsDao;
import com.example.demo.model.Logistics;
import com.example.demo.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogisticsServiceImpl implements LogisticsService {

    @Autowired
    private LogisticsDao logisticsDao;

    @Override
    public List<Logistics> getAllLogistics() {
        return logisticsDao.getAllLogistics();
    }

    @Override
    public Logistics getLogisticsById(Integer logisticsId) {
        return logisticsDao.getLogisticsById(logisticsId);
    }

    @Override
    public Integer createLogistics(Logistics logistics) {
         return logisticsDao.createLogistics(logistics);
    }

    @Override
    public void updateLogistics(Integer logisticsId, Logistics logistics) {
        logisticsDao.updateLogistics(logisticsId, logistics);
    }

    @Override
    public void deleteLogistics(Integer logisticsId) {

        logisticsDao.deleteLogistics(logisticsId);

    }
}
