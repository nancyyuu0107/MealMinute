package com.example.demo.model;

public class Logistics {
    private int logisticsID;  // 物流ID
    private String companyName;  // 物流公司
    private String logisticsTelephone;  // 物流連絡電話

    // Constructors, Getters, and Setters
    public Logistics() {}

    public Logistics(int logisticsID, String companyName, String logisticsTelephone) {
        this.logisticsID = logisticsID;
        this.companyName = companyName;
        this.logisticsTelephone = logisticsTelephone;
    }

    public int getLogisticsID() {
        return logisticsID;
    }

    public void setLogisticsID(int logisticsID) {
        this.logisticsID = logisticsID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLogisticsTelephone() {
        return logisticsTelephone;
    }

    public void setLogisticsTelephone(String logisticsTelephone) {
        this.logisticsTelephone = logisticsTelephone;
    }
}
