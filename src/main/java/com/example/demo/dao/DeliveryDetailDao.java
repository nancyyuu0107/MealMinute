package com.example.demo.dao;

import com.example.demo.model.DeliveryDetail;

import java.util.List;

public interface DeliveryDetailDao {

    List<DeliveryDetail> getAllDeliveryDetails();
    DeliveryDetail getDeliveryDetailById(Integer deliveryId);
    Integer createDeliveryDetail(DeliveryDetail deliveryDetail);
    void updateDeliveryDetail(Integer deliveryId, DeliveryDetail deliveryDetail);
    void deleteDeliveryDetail(Integer deliveryId);
    DeliveryDetail getDeliveryDetailByOrderId(Integer orderId);


}
