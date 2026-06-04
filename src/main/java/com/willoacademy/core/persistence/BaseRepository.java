package com.willoacademy.core.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T> {

    protected final JdbcTemplate jdbc;

    protected BaseRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    protected abstract RowMapper<T> rowMapper();

    protected abstract String tableName();

    public Optional<T> findById(Long id) {
        String sql = "SELECT * FROM " + tableName() + " WHERE id = ?";
        List<T> results = jdbc.query(sql, rowMapper(), id);
        return results.stream().findFirst();
    }

    public List<T> findAll() {
        String sql = "SELECT * FROM " + tableName();
        return jdbc.query(sql, rowMapper());
    }

    public int update(String sql, Object... params) {
        return jdbc.update(sql, params);
    }

    public <V> V queryForObject(String sql, Class<V> type, Object... params) {
        return jdbc.queryForObject(sql, type, params);
    }
}
