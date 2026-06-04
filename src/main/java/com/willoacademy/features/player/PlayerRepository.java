package com.willoacademy.features.player;

import com.willoacademy.core.persistence.BaseRepository;
import com.willoacademy.features.player.dto.LessonView;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PlayerRepository extends BaseRepository<LessonView> {

    public PlayerRepository(JdbcTemplate jdbc) {
        super(jdbc);
    }

    @Override
    protected String tableName() { return "lessons"; }

    @Override
    protected org.springframework.jdbc.core.RowMapper<LessonView> rowMapper() {
        return (rs, num) -> new LessonView(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("video_url"),
                0
        );
    }

    public Optional<LessonView> findLessonWithProgress(Long lessonId, Long userId) {
        String sql = """
            SELECT l.id, l.title, l.description, l.video_url,
                   COALESCE(p.progress, 0) AS progress
            FROM lessons l
            LEFT JOIN progress p ON p.lesson_id = l.id AND p.user_id = ?
            WHERE l.id = ?
        """;
        List<LessonView> results = jdbc.query(sql, (rs, num) ->
                new LessonView(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("video_url"),
                        rs.getInt("progress")
                ), userId, lessonId);
        return results.stream().findFirst();
    }

    public void saveProgress(Long userId, Long lessonId, int progress) {
        String sql = """
            INSERT INTO progress (user_id, lesson_id, progress)
            VALUES (?, ?, ?)
            ON CONFLICT (user_id, lesson_id)
            DO UPDATE SET progress = EXCLUDED.progress, updated_at = NOW()
        """;
        update(sql, userId, lessonId, Math.min(progress, 100));
    }
}
