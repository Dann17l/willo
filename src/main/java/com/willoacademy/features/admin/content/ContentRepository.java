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
        String sql = """
            SELECT id, title, category,
                   (SELECT COUNT(*) FROM lessons WHERE course_id = courses.id) AS lessons_count,
                   CASE WHEN published THEN 'published' ELSE 'draft' END AS status
            FROM courses
            ORDER BY created_at DESC
        """;
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
        String sql = """
            SELECT id, title, description, category,
                   thumbnail_path AS thumbnail,
                   CASE WHEN published THEN 'published' ELSE 'draft' END AS status,
                   (SELECT COALESCE(SUM(duration_seconds), 0) || 's' FROM lessons WHERE course_id = courses.id) AS duration
            FROM courses
            WHERE id = ?
        """;
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
        // Find a default tutor user to associate this course with
        Long tutorId;
        try {
            tutorId = jdbc.queryForObject("SELECT id FROM users LIMIT 1", Long.class);
        } catch (Exception e) {
            tutorId = 1L; // fallback default
        }

        String sql = """
            INSERT INTO courses (title, description, category, published, thumbnail_path, tutor_id)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        jdbc.update(sql, form.getTitle(), form.getDescription(),
                form.getCategory(), "published".equalsIgnoreCase(form.getStatus()),
                form.getThumbnail(), tutorId != null ? tutorId : 1L);
    }

    public void update(CourseForm form) {
        String sql = """
            UPDATE courses
            SET title = ?, description = ?, category = ?,
                published = ?, thumbnail_path = ?
            WHERE id = ?
        """;
        jdbc.update(sql, form.getTitle(), form.getDescription(),
                form.getCategory(), "published".equalsIgnoreCase(form.getStatus()),
                form.getThumbnail(), form.getId());
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM courses WHERE id = ?", id);
    }
}
