package org.ak.dao.impl;

import org.ak.dto.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.ak.dao.JdbcTemplateDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Stream;

@Component
public class LocationDaoImpl implements JdbcTemplateDao<Location> {
    public static final int LOCATION_QUERY_TIMEOUT = 90000;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Location> findByQuery(String sql, SqlParameterSource paramSource) {
        jdbcTemplate.setQueryTimeout(LOCATION_QUERY_TIMEOUT);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Location.class));
    }

    @Override
    public Stream<Location> findByQueryStream(String sql, SqlParameterSource paramSource) {
        jdbcTemplate.setQueryTimeout(LOCATION_QUERY_TIMEOUT);
        Stream stream;
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            stream = Stream.generate(new ResultSetSupplier(rs, Location.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stream;
    }
}