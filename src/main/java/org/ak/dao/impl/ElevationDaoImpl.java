package org.ak.dao.impl;

import org.ak.dto.Elevation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.ak.dao.JdbcTemplateDao;

import java.util.List;

@Component
public class ElevationDaoImpl implements JdbcTemplateDao<Elevation> {
    @Autowired
    private NamedParameterJdbcTemplate npJdbcTemplate;

    @Override
    public List<Elevation> findByQuery(String sql, SqlParameterSource paramSource) {
        return npJdbcTemplate.query(sql, paramSource, new BeanPropertyRowMapper<>(Elevation.class));
    }
}