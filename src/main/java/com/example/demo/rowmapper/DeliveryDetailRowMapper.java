package com.example.demo.rowmapper;

import com.example.demo.model.DeliveryDetail;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

public class DeliveryDetailRowMapper implements RowMapper<DeliveryDetail> {

    @Override
    public DeliveryDetail mapRow(ResultSet resultSet, int i) throws SQLException {
        DeliveryDetail deliveryDetail = new DeliveryDetail();
        deliveryDetail.setDeliveryID(resultSet.getInt("DeliveryID"));
        deliveryDetail.setOrderID(resultSet.getInt("OrderID"));
        deliveryDetail.setLogisticsID(resultSet.getInt("LogisticsID"));
        deliveryDetail.setDriver(resultSet.getString("Driver"));
        deliveryDetail.setDriverContact(resultSet.getString("DriverContact"));
        deliveryDetail.setCustomerAddress(resultSet.getString("CustomerAddress"));
        deliveryDetail.setShippingDate(resultSet.getDate("ShippingDate"));
        deliveryDetail.setEstimatedArrivalTime(resultSet.getTimestamp("EstimatedArrivalTime"));
        deliveryDetail.setDeliveryDate(resultSet.getDate("DeliveryDate"));
        deliveryDetail.setTemperatureDuringTransport(resultSet.getBigDecimal("TemperatureDuringTransport"));
        deliveryDetail.setDeliveryStatus(resultSet.getString("DeliveryStatus"));

        return deliveryDetail;
    }
}