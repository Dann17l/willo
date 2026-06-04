package com.willoacademy.features.player;

import com.willoacademy.features.player.dto.LessonView;
import com.willoacademy.core.security.AuthGuard;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    public LessonView getLesson(Long courseId, Long lessonId) {
        String userId = AuthGuard.currentUserId();
        if (userId == null) {
            throw new IllegalStateException("Usuario no autenticado");
        }
        return repository.findLessonWithProgress(lessonId, Long.parseLong(userId))
                .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada"));
    }

    public void updateProgress(Long lessonId, int progress) {
        String userId = AuthGuard.currentUserId();
        if (userId == null) return;
        repository.saveProgress(Long.parseLong(userId), lessonId, progress);
    }
}
