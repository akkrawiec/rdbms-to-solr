package org.ak.dao;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.stream.Stream;

public interface JdbcTemplateDao<T> {
    public List<T> findByQuery(String sql, SqlParameterSource paramSource);
    default public Stream<T> findByQueryStream(String sql, SqlParameterSource paramSource) {
        throw new RuntimeException("Not yet implemented");
    }
}