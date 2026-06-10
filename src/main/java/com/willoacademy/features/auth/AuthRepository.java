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
        return (rs, rowNum) -> {
            User user = new User(
                    rs.getLong("id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    Role.fromString(rs.getString("role"))
            );
            user.setPassword(rs.getString("password_hash"));
            return user;
        };
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        var results = jdbc.query(sql, rowMapper(), email);
        return results.stream().findFirst();
    }

    public void save(User user, String hashedPassword) {
        String sql = "INSERT INTO users (full_name, email, password_hash, role) VALUES (?, ?, ?, ?)";
        update(sql, user.getFullName(), user.getEmail(), hashedPassword,
                user.getRole().name());
    }
}
