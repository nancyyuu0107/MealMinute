package com.example.demo.service;

import com.example.demo.model.Orders;
import com.example.demo.model.ReturnOrder;
import com.example.demo.model.request.ReturnOrderRequest;
import org.hibernate.query.Order;

import java.util.List;

public interface ReturnOrderService {


    List<ReturnOrder> getAllReturnOrders();

    ReturnOrder getReturnOrderById(Integer returnOrderId);

    Integer createReturnOrder(ReturnOrder returnOrder);

    void updateReturnOrder(Integer returnOrderId, ReturnOrder returnOrder);

    void deleteReturnOrder(Integer returnOrderId);

    boolean createReturnOrderCheck(ReturnOrderRequest returnOrderRequest);

    Orders getOrderDetail(Integer customerId , Integer orderId);
}
