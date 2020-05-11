package org.ak.dao.impl;

import org.ak.dao.JdbcTemplateDao;
import org.ak.dto.Latitude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.stream.Stream;

public class LatitudeDaoImpl implements JdbcTemplateDao<Latitude> {
    @Autowired
    private NamedParameterJdbcTemplate npJdbcTemplate;

    @Override
    public List<Latitude> findByQuery(String sql, SqlParameterSource paramSource) {
        return npJdbcTemplate.query(sql, paramSource, new BeanPropertyRowMapper<>(Latitude.class));
    }
}
