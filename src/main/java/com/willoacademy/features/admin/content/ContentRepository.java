package com.willoacademy.features.admin.content;

import com.willoacademy.features.admin.content.dto.CourseForm;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContentRepository {

    private final JdbcTemplate jdbc;

    public ContentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<CourseForm> findAll() {
        String sql = "SELECT id, title, category, lessons_count, status FROM courses ORDER BY created_at DESC";
        return jdbc.query(sql, (rs, num) -> {
            CourseForm c = new CourseForm();
            c.setId(rs.getLong("id"));
            c.setTitle(rs.getString("title"));
            c.setCategory(rs.getString("category"));
            c.setStatus(rs.getString("status"));
            return c;
        });
    }

    public CourseForm findById(Long id) {
        String sql = "SELECT * FROM courses WHERE id = ?";
        return jdbc.queryForObject(sql, (rs, num) -> {
            CourseForm c = new CourseForm();
            c.setId(rs.getLong("id"));
            c.setTitle(rs.getString("title"));
            c.setDescription(rs.getString("description"));
            c.setCategory(rs.getString("category"));
            c.setStatus(rs.getString("status"));
            c.setThumbnail(rs.getString("thumbnail"));
            c.setDuration(rs.getString("duration"));
            return c;
        }, id);
    }

    public void save(CourseForm form) {
        String sql = """
            INSERT INTO courses (title, description, category, status, thumbnail, duration)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        jdbc.update(sql, form.getTitle(), form.getDescription(),
                form.getCategory(), form.getStatus(),
                form.getThumbnail(), form.getDuration());
    }

    public void update(CourseForm form) {
        String sql = """
            UPDATE courses SET title = ?, description = ?, category = ?,
                status = ?, thumbnail = ?, duration = ?
            WHERE id = ?
        """;
        jdbc.update(sql, form.getTitle(), form.getDescription(),
                form.getCategory(), form.getStatus(),
                form.getThumbnail(), form.getDuration(), form.getId());
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM courses WHERE id = ?", id);
    }
}
