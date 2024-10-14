package com.example.demo.controller;

import com.example.demo.dto.OrderDto;
import com.example.demo.dto.ReturnOrderDetailInfoDto;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.Orders;
import com.example.demo.model.ReturnOrder;
import com.example.demo.model.request.ReturnOrderRequest;
import com.example.demo.service.ReturnOrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class ReturnOrderController {

    @Autowired
    private ReturnOrderService returnOrderService;

    @GetMapping("/returnorders")
    public ResponseEntity<List<ReturnOrder>> getAllReturnOrders() {
        List<ReturnOrder> returnOrderList = returnOrderService.getAllReturnOrders();
        return ResponseEntity.ok(returnOrderList);
    }

    @GetMapping("/returnorders/{returnOrderId}")
    public ResponseEntity<ReturnOrder> getReturnOrder(@PathVariable Integer returnOrderId) {
        ReturnOrder returnOrder = returnOrderService.getReturnOrderById(returnOrderId);
        if (returnOrder != null) {
            return ResponseEntity.ok(returnOrder);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found when return order not found
        }
    }

    @PostMapping("/returnorders")
    public ResponseEntity<ReturnOrder> createReturnOrder(@RequestBody ReturnOrder returnOrder) {
        returnOrderService.createReturnOrder(returnOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnOrder);
    }

    @PutMapping("/returnorders/{returnOrderId}")
    public ResponseEntity<ReturnOrder> updateReturnOrder(@PathVariable Integer returnOrderId, @RequestBody ReturnOrder returnOrder) {
        ReturnOrder existingReturnOrder = returnOrderService.getReturnOrderById(returnOrderId);
        if (existingReturnOrder != null) {
            // 設置正確的 ID
            returnOrder.setReturnOrderID(returnOrderId);

            returnOrderService.updateReturnOrder(returnOrderId, returnOrder);
            return ResponseEntity.ok(returnOrder);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found when return order not found
        }
    }

    @DeleteMapping("/returnorders/{returnOrderId}")
    public ResponseEntity<Void> deleteReturnOrder(@PathVariable Integer returnOrderId) {
        returnOrderService.deleteReturnOrder(returnOrderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content when successfully deleted
    }


    @GetMapping("/returnorders/order/{customerId}/{orderId}")
    public ResponseEntity<OrderDto> getOrder(
            @PathVariable Integer orderId,
            @PathVariable Integer customerId) {

        Orders order = returnOrderService.getOrderDetail(customerId, orderId);

        OrderDto orderDto = new OrderDto();

        orderDto.setOrderId(order.getOrderId());
        orderDto.setOrderAmount(order.getOrderAmount());

        List<ReturnOrderDetailInfoDto> orderDetailInfoDtos = new ArrayList<>();
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            ReturnOrderDetailInfoDto orderDetailInfoDto = new ReturnOrderDetailInfoDto();
            orderDetailInfoDto.setOrderDetailId(orderDetail.getOrderDetailId());
            orderDetailInfoDto.setQuantity(orderDetail.getQuantity());
            orderDetailInfoDto.setPrice(orderDetail.getIngredient().getPrice());
            orderDetailInfoDto.setIngredientId(orderDetail.getIngredient().getIngredientId());
            orderDetailInfoDto.setTotalAmount(orderDetail.getTotalAmount());
            orderDetailInfoDto.setIngredientName(orderDetail.getIngredient().getIngredientName());
            orderDetailInfoDtos.add(orderDetailInfoDto);
        }

        orderDto.setReturnOrderDetailDto(orderDetailInfoDtos);

        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/returnorders/check")
    public ResponseEntity<Boolean> createReturnOrderCheck
            (@RequestBody ReturnOrderRequest returnOrderRequest) {

        boolean isSuccess = returnOrderService.createReturnOrderCheck(returnOrderRequest);

        return ResponseEntity.ok(isSuccess);
    }
}
