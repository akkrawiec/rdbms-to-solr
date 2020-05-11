package org.ak.dao.impl;

import org.ak.dao.JdbcTemplateDao;
import org.ak.dto.Longitude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;

public class LongitudeDaoImpl implements JdbcTemplateDao<Longitude> {
    @Autowired
    private NamedParameterJdbcTemplate npJdbcTemplate;

    @Override
    public List<Longitude> findByQuery(String sql, SqlParameterSource paramSource) {
        return npJdbcTemplate.query(sql, paramSource, new BeanPropertyRowMapper<>(Longitude.class));
    }
}
