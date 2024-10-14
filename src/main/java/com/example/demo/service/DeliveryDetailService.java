package com.example.demo.service;

import com.example.demo.model.DeliveryDetail;

import java.util.List;

public interface DeliveryDetailService {

    List<DeliveryDetail> getAllDeliveryDetails();
    DeliveryDetail getDeliveryDetailById(Integer deliveryId);
    Integer createDeliveryDetail(DeliveryDetail deliveryDetail);
    void updateDeliveryDetail(Integer deliveryId, DeliveryDetail deliveryDetail);
    void deleteDeliveryDetail(Integer deliveryId);

    DeliveryDetail getDeliveryDetailByOrderId(Integer orderId);

}
