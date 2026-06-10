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
                "", // description is not present in the new lessons schema
                rs.getString("hls_playlist_path"),
                0
        );
    }

    public Optional<LessonView> findLessonWithProgress(Long lessonId, Long userId) {
        String sql = """
            SELECT l.id, l.title, l.hls_playlist_path AS video_url,
                   CASE WHEN COALESCE(p.total_duration, 0) > 0 
                        THEN (p.seconds_watched * 100 / p.total_duration) 
                        ELSE 0 
                   END AS progress
            FROM lessons l
            LEFT JOIN user_progress p ON p.lesson_id = l.id AND p.user_id = ?
            WHERE l.id = ?
        """;
        List<LessonView> results = jdbc.query(sql, (rs, num) ->
                new LessonView(
                        rs.getLong("id"),
                        rs.getString("title"),
                        "", // description is not present in the new lessons schema
                        rs.getString("video_url"),
                        rs.getInt("progress")
                ), userId, lessonId);
        return results.stream().findFirst();
    }

    public void saveProgress(Long userId, Long lessonId, int progress) {
        // Fetch course_id and duration_seconds from lessons
        String fetchSql = "SELECT course_id, duration_seconds FROM lessons WHERE id = ?";
        List<Object[]> queryResult = jdbc.query(fetchSql, (rs, num) -> new Object[] {
                rs.getLong("course_id"),
                rs.getInt("duration_seconds")
        }, lessonId);

        if (queryResult.isEmpty()) {
            return;
        }

        Long courseId = (Long) queryResult.get(0)[0];
        int durationSeconds = (Integer) queryResult.get(0)[1];
        if (durationSeconds <= 0) {
            durationSeconds = 1000; // fallback default duration in seconds
        }

        int secondsWatched = (progress * durationSeconds) / 100;
        boolean completed = progress >= 100;

        String sql = """
            INSERT INTO user_progress (user_id, course_id, lesson_id, seconds_watched, total_duration, completed, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, NOW())
            ON CONFLICT (user_id, course_id, lesson_id)
            DO UPDATE SET seconds_watched = EXCLUDED.seconds_watched,
                          total_duration = EXCLUDED.total_duration,
                          completed = EXCLUDED.completed,
                          updated_at = NOW()
        """;
        update(sql, userId, courseId, lessonId, secondsWatched, durationSeconds, completed);
    }
}
