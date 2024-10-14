package com.example.demo.rowmapper;

import com.example.demo.model.Logistics;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LogisticsRowMapper implements RowMapper<Logistics> {

    @Override
    public Logistics mapRow(ResultSet resultSet, int i) throws SQLException {
        Logistics logistics = new Logistics();

        logistics.setLogisticsID(resultSet.getInt("LogisticsID"));
        logistics.setCompanyName(resultSet.getString("CompanyName"));
        logistics.setLogisticsTelephone(resultSet.getString("LogisticsTelephone"));

        return logistics;
    }
}


