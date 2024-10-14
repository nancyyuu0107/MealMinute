package com.example.demo.dao;

import com.example.demo.model.Orders;
import com.example.demo.model.ReturnOrder;
import com.example.demo.model.request.ReturnOrderRequest;

import java.util.List;

public interface ReturnOrderDao {
    List<ReturnOrder> getAllReturnOrders();

    ReturnOrder getReturnOrderById(Integer returnOrderId);

    Integer createReturnOrder(ReturnOrder returnOrder);

    void updateReturnOrder(Integer returnOrderId, ReturnOrder returnOrder);

    void deleteReturnOrder(Integer returnOrderId);

    Orders getOrderDetail(Integer orderId);

    boolean createReturnOrderCheck(ReturnOrderRequest returnOrderRequest);
}
