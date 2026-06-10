package com.willoacademy.features.admin.dashboard;

import com.willoacademy.features.admin.dashboard.dto.StatsResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DashboardRepository {

    private final JdbcTemplate jdbc;

    public DashboardRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public StatsResponse getStats() {
        int activeUsers = jdbc.queryForObject(
                "SELECT COUNT(*) FROM users WHERE last_login > NOW() - INTERVAL '30 days'",
                Integer.class);
        int publishedCourses = jdbc.queryForObject(
                "SELECT COUNT(*) FROM courses WHERE published = true",
                Integer.class);
        int completedLessons = jdbc.queryForObject(
                "SELECT COUNT(*) FROM user_progress WHERE completed = true",
                Integer.class);
        Double revenue = jdbc.queryForObject(
                "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE status = 'completed'",
                Double.class);
        return new StatsResponse(activeUsers, publishedCourses,
                completedLessons, revenue != null ? revenue : 0.0);
    }

    public List<Object[]> getWeeklyProgress() {
        String sql = """
            SELECT TO_CHAR(date_trunc('day', updated_at), 'Dy') AS day,
                   COUNT(*) AS count
            FROM user_progress
            WHERE updated_at > NOW() - INTERVAL '7 days'
              AND completed = true
            GROUP BY date_trunc('day', updated_at)
            ORDER BY date_trunc('day', updated_at)
        """;
        return jdbc.query(sql, (rs, num) -> new Object[]{
                rs.getString("day"), rs.getInt("count")
        });
    }
}
