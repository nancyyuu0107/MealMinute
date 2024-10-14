package com.example.demo.model.request;

import com.example.demo.dto.OrderDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @date 2024/8/31
 */
@Getter
@Setter
public class ReturnOrderRequest {

    private int orderId;

    private String returnReason;

    private String handlingMethod;

    private Date returnDate;

    private OrderDto orderInfo;
}
