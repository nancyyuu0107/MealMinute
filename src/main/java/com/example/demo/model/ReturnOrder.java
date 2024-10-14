package com.example.demo.model;

import java.util.Date;

public class ReturnOrder {
    // 退貨單編號 (主鍵)
    private int returnOrderID;

    // 訂單編號 (外鍵，關聯到 Orders 表)
    private int orderID;

    // 退貨日期
    private Date returnDate;

    // 商品編號 (外鍵，關聯到 Ingredient 表)
    private int ingredientID;

    // 退貨數量
    private int returnQuantity;

    // 退貨原因 (例如：瑕疵、錯誤品項)
    private String returnReason;

    // 處理方式 (例如：退款、換貨)
    private String processMethod;

    public int getReturnOrderID() {
        return returnOrderID;
    }

    public void setReturnOrderID(int returnOrderID) {
        this.returnOrderID = returnOrderID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public int getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(int ingredientID) {
        this.ingredientID = ingredientID;
    }

    public int getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(int returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getProcessMethod() {
        return processMethod;
    }

    public void setProcessMethod(String processMethod) {
        this.processMethod = processMethod;
    }
}