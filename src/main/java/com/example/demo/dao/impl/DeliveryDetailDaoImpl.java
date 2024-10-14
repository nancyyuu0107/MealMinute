package com.example.demo.dao.impl;

import com.example.demo.dao.DeliveryDetailDao;
import com.example.demo.model.DeliveryDetail;
import com.example.demo.rowmapper.DeliveryDetailRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DeliveryDetailDaoImpl implements DeliveryDetailDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<DeliveryDetail> getAllDeliveryDetails() {
        String sql = "SELECT DeliveryID, OrderID, LogisticsID, Driver, DriverContact, CustomerAddress, " +
                "ShippingDate, EstimatedArrivalTime, DeliveryDate, TemperatureDuringTransport, DeliveryStatus " +
                "FROM DeliveryDetail";

        Map<String, Object> map = new HashMap<>();

        List<DeliveryDetail> deliveryDetailList = namedParameterJdbcTemplate.query(sql, map, new DeliveryDetailRowMapper());

        return deliveryDetailList;
    }

    @Override
    public DeliveryDetail getDeliveryDetailById(Integer deliveryId) {
        String sql = "SELECT DeliveryID, OrderID, LogisticsID, Driver, DriverContact, CustomerAddress, " +
                "ShippingDate, EstimatedArrivalTime, DeliveryDate, TemperatureDuringTransport, DeliveryStatus " +
                "FROM DeliveryDetail WHERE DeliveryID = :deliveryId";

        Map<String, Object> map = new HashMap<>();
        map.put("deliveryId", deliveryId);

        List<DeliveryDetail> deliveryDetailList = namedParameterJdbcTemplate.query(sql, map, new DeliveryDetailRowMapper());

        if (deliveryDetailList.size() > 0) {
            return deliveryDetailList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Integer createDeliveryDetail(DeliveryDetail deliveryDetail) {
        String sql = "INSERT INTO DeliveryDetail (OrderID, LogisticsID, Driver, DriverContact, CustomerAddress, " +
                "ShippingDate, EstimatedArrivalTime, DeliveryDate, TemperatureDuringTransport, DeliveryStatus) " +
                "VALUES (:orderID, :logisticsID, :driver, :driverContact, :customerAddress, " +
                ":shippingDate, :estimatedArrivalTime, :deliveryDate, :temperatureDuringTransport, :deliveryStatus)";

        Map<String, Object> map = new HashMap<>();
        map.put("orderID", deliveryDetail.getOrderID());
        map.put("logisticsID", deliveryDetail.getLogisticsID());
        map.put("driver", deliveryDetail.getDriver());
        map.put("driverContact", deliveryDetail.getDriverContact());
        map.put("customerAddress", deliveryDetail.getCustomerAddress());
        map.put("shippingDate", deliveryDetail.getShippingDate());
        map.put("estimatedArrivalTime", deliveryDetail.getEstimatedArrivalTime());
        map.put("deliveryDate", deliveryDetail.getDeliveryDate());
        map.put("temperatureDuringTransport", deliveryDetail.getTemperatureDuringTransport());
        map.put("deliveryStatus", deliveryDetail.getDeliveryStatus());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int deliveryId = keyHolder.getKey().intValue();

        return deliveryId;
    }

    @Override
    public void updateDeliveryDetail(Integer deliveryId, DeliveryDetail deliveryDetail) {
        String sql = "UPDATE DeliveryDetail SET " +
                "OrderID = :orderID, " +
                "LogisticsID = :logisticsID, " +
                "Driver = :driver, " +
                "DriverContact = :driverContact, " +
                "CustomerAddress = :customerAddress, " +
                "ShippingDate = :shippingDate, " +
                "EstimatedArrivalTime = :estimatedArrivalTime, " +
                "DeliveryDate = :deliveryDate, " +
                "TemperatureDuringTransport = :temperatureDuringTransport, " +
                "DeliveryStatus = :deliveryStatus " +
                "WHERE DeliveryID = :deliveryID";

        Map<String, Object> map = new HashMap<>();
        map.put("deliveryID", deliveryId);
        map.put("orderID", deliveryDetail.getOrderID());
        map.put("logisticsID", deliveryDetail.getLogisticsID());
        map.put("driver", deliveryDetail.getDriver());
        map.put("driverContact", deliveryDetail.getDriverContact());
        map.put("customerAddress", deliveryDetail.getCustomerAddress());
        map.put("shippingDate", deliveryDetail.getShippingDate());
        map.put("estimatedArrivalTime", deliveryDetail.getEstimatedArrivalTime());
        map.put("deliveryDate", deliveryDetail.getDeliveryDate());
        map.put("temperatureDuringTransport", deliveryDetail.getTemperatureDuringTransport());
        map.put("deliveryStatus", deliveryDetail.getDeliveryStatus());

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map));
    }

    @Override
    public void deleteDeliveryDetail(Integer deliveryId) {
        String sql = "DELETE FROM DeliveryDetail WHERE DeliveryID = :deliveryId";

        Map<String, Object> map = new HashMap<>();
        map.put("deliveryId", deliveryId);

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public DeliveryDetail getDeliveryDetailByOrderId(Integer orderId) {


        String sql = "SELECT DeliveryID, OrderID, LogisticsID, Driver, DriverContact, CustomerAddress, " +
                "ShippingDate, EstimatedArrivalTime, DeliveryDate, TemperatureDuringTransport, DeliveryStatus " +
                "FROM DeliveryDetail WHERE OrderID = :orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<DeliveryDetail> deliveryDetailList = namedParameterJdbcTemplate.query(sql, map, new DeliveryDetailRowMapper());

        if (deliveryDetailList.size() > 0) {
            return deliveryDetailList.get(0);
        } else {
            return null;
        }


    }
}