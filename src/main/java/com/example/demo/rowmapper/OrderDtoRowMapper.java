package com.example.demo.rowmapper;

import com.example.demo.dto.OrderDto;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.jdbc.core.RowMapper;
import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author haohao
 * @date 2024/8/31
 */
public class OrderDtoRowMapper implements RowMapper<OrderDto> {

    @Override
    public OrderDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }
}
