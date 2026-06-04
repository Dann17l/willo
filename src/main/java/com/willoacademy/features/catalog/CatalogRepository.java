package com.willoacademy.features.catalog;

import com.willoacademy.core.persistence.BaseRepository;
import com.willoacademy.features.catalog.dto.CourseDetail;
import com.willoacademy.features.catalog.dto.CourseSummary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CatalogRepository extends BaseRepository<CourseSummary> {

    public CatalogRepository(JdbcTemplate jdbc) {
        super(jdbc);
    }

    @Override
    protected String tableName() { return "courses"; }

    @Override
    protected org.springframework.jdbc.core.RowMapper<CourseSummary> rowMapper() {
        return (rs, num) -> new CourseSummary(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("category"),
                rs.getString("thumbnail"),
                rs.getString("duration"),
                rs.getInt("lessons_count")
        );
    }

    public List<CourseSummary> search(String query, Long categoryId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM courses WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (query != null && !query.isBlank()) {
            sql.append(" AND LOWER(title) LIKE ?");
            params.add("%" + query.toLowerCase() + "%");
        }
        if (categoryId != null) {
            sql.append(" AND category_id = ?");
            params.add(categoryId);
        }
        sql.append(" ORDER BY created_at DESC");
        return jdbc.query(sql.toString(), rowMapper(), params.toArray());
    }

    public Optional<CourseDetail> findDetailById(Long id) {
        String sql = "SELECT * FROM courses WHERE id = ?";
        return jdbc.query(sql, (rs) -> {
            if (!rs.next()) return Optional.empty();
            CourseDetail d = new CourseDetail();
            d.setId(rs.getLong("id"));
            d.setTitle(rs.getString("title"));
            d.setDescription(rs.getString("description"));
            d.setCategory(rs.getString("category"));
            d.setThumbnail(rs.getString("thumbnail"));
            d.setDuration(rs.getString("duration"));
            d.setLessonsCount(rs.getInt("lessons_count"));
            d.setLessons(findLessonsByCourseId(id));
            return Optional.of(d);
        }, id);
    }

    public List<CourseDetail.LessonItem> findLessonsByCourseId(Long courseId) {
        String sql = "SELECT id, title, duration FROM lessons WHERE course_id = ? ORDER BY position";
        return jdbc.query(sql, (rs, num) ->
                new CourseDetail.LessonItem(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("duration")
                ), courseId);
    }

    public List<String> findAllCategories() {
        return jdbc.query("SELECT DISTINCT name FROM categories ORDER BY name",
                (rs, num) -> rs.getString("name"));
    }
}
