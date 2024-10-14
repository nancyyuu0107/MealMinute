package com.example.demo.dao.impl;

import com.example.demo.dao.ReturnOrderDao;
import com.example.demo.dto.OrderDto;
import com.example.demo.dto.ReturnOrderDetailInfoDto;
import com.example.demo.model.Orders;
import com.example.demo.model.ReturnOrder;
import com.example.demo.model.request.ReturnOrderRequest;
import com.example.demo.rowmapper.ReturnOrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ReturnOrderDaoImpl implements ReturnOrderDao {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<ReturnOrder> getAllReturnOrders() {
        String sql = "SELECT ReturnOrderID, OrderID, ReturnDate, IngredientID, ReturnQuantity, ReturnReason, ProcessMethod " +
                "FROM ReturnOrder";

        Map<String, Object> map = new HashMap<>();

        List<ReturnOrder> returnOrderList = namedParameterJdbcTemplate.query(sql, map, new ReturnOrderRowMapper());

        return returnOrderList;
    }

    @Override
    public ReturnOrder getReturnOrderById(Integer returnOrderId) {
        String sql = "SELECT ReturnOrderID, OrderID, ReturnDate, IngredientID, ReturnQuantity, ReturnReason, ProcessMethod " +
                "FROM ReturnOrder WHERE ReturnOrderID = :returnOrderID";

        Map<String, Object> map = new HashMap<>();
        map.put("returnOrderID", returnOrderId);

        List<ReturnOrder> returnOrderList = namedParameterJdbcTemplate.query(sql, map, new ReturnOrderRowMapper());

        if (returnOrderList.size() > 0) {
            return returnOrderList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Integer createReturnOrder(ReturnOrder returnOrder) {
        String sql = "INSERT INTO ReturnOrder (OrderID, ReturnDate, IngredientID, ReturnQuantity, ReturnReason, ProcessMethod) " +
                "OUTPUT INSERTED.ReturnOrderID " +
                "VALUES (:orderID, :returnDate, :ingredientID, :returnQuantity, :returnReason, :processMethod)";

        Map<String, Object> map = new HashMap<>();
        map.put("orderID", returnOrder.getOrderID());
        map.put("returnDate", returnOrder.getReturnDate());
        map.put("ingredientID", returnOrder.getIngredientID());
        map.put("returnQuantity", returnOrder.getReturnQuantity());
        map.put("returnReason", returnOrder.getReturnReason());
        map.put("processMethod", returnOrder.getProcessMethod());

        Integer newReturnOrderID = namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource(map), Integer.class);
        return newReturnOrderID;
    }


    @Override
    public void updateReturnOrder(Integer returnOrderId, ReturnOrder returnOrder) {
        String sql = "UPDATE ReturnOrder SET OrderID = :orderID, ReturnDate = :returnDate, " +
                "IngredientID = :ingredientID, ReturnQuantity = :returnQuantity, " +
                "ReturnReason = :returnReason, ProcessMethod = :processMethod " +
                "WHERE ReturnOrderID = :returnOrderID";

        Map<String, Object> map = new HashMap<>();
        map.put("returnOrderID", returnOrderId);
        map.put("orderID", returnOrder.getOrderID());
        map.put("returnDate", returnOrder.getReturnDate());
        map.put("ingredientID", returnOrder.getIngredientID());
        map.put("returnQuantity", returnOrder.getReturnQuantity());
        map.put("returnReason", returnOrder.getReturnReason());
        map.put("processMethod", returnOrder.getProcessMethod());

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map));
    }

    @Override
    public void deleteReturnOrder(Integer returnOrderId) {
        String sql = "DELETE FROM ReturnOrder WHERE ReturnOrderID = :returnOrderID";
        Map<String, Object> map = new HashMap<>();
        map.put("returnOrderID", returnOrderId);

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map));
    }

    @Override
    public Orders getOrderDetail(Integer orderId) {

        OrderDto orderDto = new OrderDto();

        String orderSql =
                "SELECT orderId, orderAmount, orderStatus" +
                        "from orders" +
                        "WHERE orderId = :orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<ReturnOrder> order = namedParameterJdbcTemplate.query(orderSql, map, new ReturnOrderRowMapper());

        return null;
    }

    @Override
    public boolean createReturnOrderCheck(ReturnOrderRequest returnOrderRequest) {

        String sql = "INSERT INTO ReturnOrder (" +
                "OrderID," +
                " ReturnDate, " +
                "IngredientID, " +
                "ReturnQuantity," +
                " ReturnReason, " +
                "ProcessMethod) " +
                "VALUES (:orderId, :returnDate, :ingredientID, :returnQuantity, :returnReason, :processMethod)";

        List<SqlParameterSource> batchParams = new ArrayList<>();

        for (ReturnOrderDetailInfoDto orderDetailInfoDto : returnOrderRequest.getOrderInfo().getReturnOrderDetailDto()) {
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("orderId", returnOrderRequest.getOrderId())
                    .addValue("returnDate", returnOrderRequest.getReturnDate())
                    .addValue("ingredientID", orderDetailInfoDto.getIngredientId())
                    .addValue("returnQuantity", orderDetailInfoDto.getQuantity())
                    .addValue("returnReason", returnOrderRequest.getReturnReason())
                    .addValue("processMethod", returnOrderRequest.getHandlingMethod());

            batchParams.add(params);
        }

        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batchParams.toArray(new SqlParameterSource[0]));

        return Arrays.stream(updateCounts).allMatch(count -> count == 1);
    }
}
