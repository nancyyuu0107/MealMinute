package com.example.demo.dto;

import com.example.demo.model.IngredientBean;
import com.example.demo.model.Orders;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author haohao
 * @date 2024/8/31
 */
@Getter
@Setter
public class ReturnOrderDetailInfoDto {

    private Integer orderDetailId;

    private Integer quantity;

    private Integer totalAmount;

    private Integer ingredientId; // 食材id

    private String ingredientName; // 食材名稱

    private String category; // 分類

    private Integer price; // 單價

    private String ingredientPicUrl; // 食材照片連結

    private String description; // 食材描述

    private Integer specification; // 食材規格

    private String unit; // 食材單位

    private Integer stockQuantity; // 庫存數量

    private String shelfLife; // 保存期限

    private Float requiredTemperature; // 所需溫度

    private String productStatus; // 商品狀態

}
