package com.example.demo.dao.impl;

import com.example.demo.dao.LogisticsDao;
import com.example.demo.model.Logistics;
import com.example.demo.rowmapper.LogisticsRowMapper;
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
public class LogisticsDaoImpl implements LogisticsDao {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Logistics> getAllLogistics() {
        String sql = "SELECT logisticsID, companyName, logisticsTelephone " +
                "FROM logistics";

        Map<String, Object> map = new HashMap<>();

        List<Logistics> logisticsList = namedParameterJdbcTemplate.query(sql, map, new LogisticsRowMapper());

        return logisticsList;
    }

    @Override
    public Logistics getLogisticsById(Integer logisticsId) {
        String sql = "SELECT logisticsID, companyName, logisticsTelephone FROM logistics WHERE logisticsID = :logisticsID";

        Map<String, Object> map = new HashMap<>();
        map.put("logisticsID", logisticsId);

        List<Logistics> logisticsList = namedParameterJdbcTemplate.query(sql, map, new LogisticsRowMapper());

        if (logisticsList.size() > 0) {
            return logisticsList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Integer createLogistics(Logistics logistics) {
        String sql = "INSERT INTO logistics (companyName, logisticsTelephone) VALUES (:companyName, :logisticsTelephone)";
        Map<String, Object> map = new HashMap<>();
        map.put("companyName", logistics.getCompanyName());
        map.put("logisticsTelephone", logistics.getLogisticsTelephone());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        return keyHolder.getKey().intValue();  // 返回生成的物流 ID
    }

    @Override
    public void updateLogistics(Integer logisticsId, Logistics logistics) {
        String sql = "UPDATE logistics SET companyName = :companyName, logisticsTelephone = :logisticsTelephone WHERE logisticsID = :logisticsID";
        Map<String, Object> map = new HashMap<>();
        map.put("logisticsID", logisticsId);
        map.put("companyName", logistics.getCompanyName());
        map.put("logisticsTelephone", logistics.getLogisticsTelephone());

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map));
    }

    @Override
    public void deleteLogistics(Integer logisticsId) {
        String sql = "DELETE FROM logistics WHERE logisticsID = :logisticsID";
        Map<String, Object> map = new HashMap<>();
        map.put("logisticsID", logisticsId);

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map));
    }
}
