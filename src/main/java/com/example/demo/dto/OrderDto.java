package com.example.demo.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @date 2024/8/31
 */
@Getter
@Setter
public class OrderDto {

    private Integer orderId;

    private Date orderTime;

    private Integer orderAmount;

    public List<ReturnOrderDetailInfoDto> returnOrderDetailDto;

}



