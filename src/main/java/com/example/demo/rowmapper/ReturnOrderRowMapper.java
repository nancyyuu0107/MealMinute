package com.example.demo.rowmapper;

import com.example.demo.model.ReturnOrder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReturnOrderRowMapper implements RowMapper<ReturnOrder> {

    @Override
    public ReturnOrder mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        ReturnOrder returnOrder = new ReturnOrder();

        returnOrder.setReturnOrderID(resultSet.getInt("ReturnOrderID"));
        returnOrder.setOrderID(resultSet.getInt("OrderID"));
        returnOrder.setReturnDate(resultSet.getDate("ReturnDate"));
        returnOrder.setIngredientID(resultSet.getInt("IngredientID"));
        returnOrder.setReturnQuantity(resultSet.getInt("ReturnQuantity"));
        returnOrder.setReturnReason(resultSet.getString("ReturnReason"));
        returnOrder.setProcessMethod(resultSet.getString("ProcessMethod"));

        return returnOrder;
    }
}