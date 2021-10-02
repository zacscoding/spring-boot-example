package io.spring.batch.database.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CustomerRowMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet resultSet, int i) throws SQLException {
        return Customer.builder()
                       .id(resultSet.getLong("id"))
                       .address(resultSet.getString("address"))
                       .city(resultSet.getString("city"))
                       .firstName(resultSet.getString("firstName"))
                       .lastName(resultSet.getString("lastName"))
                       .middleInitial(resultSet.getString("middleInitial"))
                       .state(resultSet.getString("state"))
                       .zipCode(resultSet.getString("zipCode"))
                       .build();
    }
}
