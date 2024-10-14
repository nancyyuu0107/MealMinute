package com.example.demo.model;

import java.util.Date;
import java.math.BigDecimal;

public class DeliveryDetail {

    private int deliveryID;                  // 配送編號 (主鍵)
    private int orderID;                     // 訂單編號 (外鍵)
    private int logisticsID;                 // 物流ID (外鍵)
    private String driver;                   // 物流送貨人員
    private String driverContact;            // 司機聯絡方式
    private String customerAddress;          // 客戶地址
    private Date shippingDate;               // 出貨日期
    private Date estimatedArrivalTime;       // 預計到達時間
    private Date deliveryDate;               // 配送日期
    private BigDecimal temperatureDuringTransport; // 運輸期間的溫度
    private String deliveryStatus;           // 配送狀態 (配送中、已送達)

    // Getters and Setters

    public int getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(int deliveryID) {
        this.deliveryID = deliveryID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getLogisticsID() {
        return logisticsID;
    }

    public void setLogisticsID(int logisticsID) {
        this.logisticsID = logisticsID;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public Date getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(Date estimatedArrivalTime) {
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public BigDecimal getTemperatureDuringTransport() {
        return temperatureDuringTransport;
    }

    public void setTemperatureDuringTransport(BigDecimal temperatureDuringTransport) {
        this.temperatureDuringTransport = temperatureDuringTransport;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
