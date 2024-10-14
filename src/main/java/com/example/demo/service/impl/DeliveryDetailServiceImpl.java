package com.example.demo.service.impl;

import com.example.demo.dao.DeliveryDetailDao;
import com.example.demo.model.DeliveryDetail;
import com.example.demo.service.DeliveryDetailService;
import com.example.demo.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryDetailServiceImpl implements DeliveryDetailService {

    @Autowired
    private DeliveryDetailDao deliveryDetailDao;

    private OrdersService ordersService;


    @Override
    public List<DeliveryDetail> getAllDeliveryDetails() {
        return deliveryDetailDao.getAllDeliveryDetails();
    }

    @Override
    public DeliveryDetail getDeliveryDetailById(Integer deliveryId) {
        return deliveryDetailDao.getDeliveryDetailById(deliveryId);
    }

    @Override
    public Integer createDeliveryDetail(DeliveryDetail deliveryDetail) {
        return deliveryDetailDao.createDeliveryDetail(deliveryDetail);
    }

    @Override
    public void updateDeliveryDetail(Integer deliveryId, DeliveryDetail deliveryDetail) {
        deliveryDetailDao.updateDeliveryDetail(deliveryId, deliveryDetail);
    }

    @Override
    public void deleteDeliveryDetail(Integer deliveryId) {
        deliveryDetailDao.deleteDeliveryDetail(deliveryId);
    }

    @Override
    public DeliveryDetail getDeliveryDetailByOrderId(Integer orderId) {
        return deliveryDetailDao.getDeliveryDetailByOrderId(orderId);
    }
}