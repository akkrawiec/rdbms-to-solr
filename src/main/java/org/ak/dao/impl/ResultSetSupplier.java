package org.ak.dao.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.sql.ResultSet;
import java.util.function.Supplier;

public class ResultSetSupplier<T> implements Supplier<T> {
    private ResultSet resultSet;
    private Class<T> type;
    private final BeanPropertyRowMapper<T> rowMapper;

    public ResultSetSupplier(ResultSet resultSet, Class<T> type) {
        this.resultSet = resultSet;
        this.type = type;
        rowMapper = new BeanPropertyRowMapper<>(type);
    }

    @Override
    public T get() {
        try {
            if (resultSet.next()) {
                return rowMapper.mapRow(resultSet, resultSet.getRow());
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return null;
    }
}
