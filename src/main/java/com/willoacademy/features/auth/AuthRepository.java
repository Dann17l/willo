package com.willoacademy.features.auth;

import com.willoacademy.core.persistence.BaseRepository;
import com.willoacademy.core.security.Role;
import com.willoacademy.shared.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AuthRepository extends BaseRepository<User> {

    public AuthRepository(JdbcTemplate jdbc) {
        super(jdbc);
    }

    @Override
    protected String tableName() { return "users"; }

    @Override
    protected org.springframework.jdbc.core.RowMapper<User> rowMapper() {
        return (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                Role.fromString(rs.getString("role"))
        );
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        var results = jdbc.query(sql, rowMapper(), email);
        return results.stream().findFirst();
    }

    public void save(User user, String hashedPassword) {
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        update(sql, user.getName(), user.getEmail(), hashedPassword,
                user.getRole().name().toLowerCase());
    }
}
