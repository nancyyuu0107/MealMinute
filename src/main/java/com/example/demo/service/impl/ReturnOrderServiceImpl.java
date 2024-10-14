package com.example.demo.service.impl;

import com.example.demo.dao.ReturnOrderDao;
import com.example.demo.model.Orders;
import com.example.demo.model.OrdersRepository;
import com.example.demo.model.ReturnOrder;
import com.example.demo.model.request.ReturnOrderRequest;
import com.example.demo.service.ReturnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReturnOrderServiceImpl implements ReturnOrderService {

    @Autowired
    private ReturnOrderDao returnOrderDao;

    @Autowired
    private OrdersRepository ordersRepository;


    @Override
    public List<ReturnOrder> getAllReturnOrders() {
        return returnOrderDao.getAllReturnOrders();
    }

    @Override
    public ReturnOrder getReturnOrderById(Integer returnOrderId) {
        return returnOrderDao.getReturnOrderById(returnOrderId);
    }

    @Override
    public Integer createReturnOrder(ReturnOrder returnOrder) {
        return returnOrderDao.createReturnOrder(returnOrder);
    }

    @Override
    public void updateReturnOrder(Integer returnOrderId, ReturnOrder returnOrder) {
        returnOrderDao.updateReturnOrder(returnOrderId, returnOrder);
    }

    @Override
    public void deleteReturnOrder(Integer returnOrderId) {
        returnOrderDao.deleteReturnOrder(returnOrderId);
    }

    @Override
    public Orders getOrderDetail(Integer customerId, Integer orderId) {

        List<Orders> orders = ordersRepository.findByCustomerId(customerId);

        Optional<Orders> firstMatchingOrder = orders.stream()
                .filter(order -> Objects.equals(order.getOrderId(), orderId))
                .findFirst();

        return firstMatchingOrder.orElse(null);
    }

    @Override
    public boolean createReturnOrderCheck(ReturnOrderRequest returnOrderRequest) {


        return returnOrderDao.createReturnOrderCheck(returnOrderRequest);
    }
}
