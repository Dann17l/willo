package com.willoacademy.features.player;

import com.willoacademy.features.player.dto.ProgressRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/{courseId}/{lessonId}")
    public String view(@PathVariable Long courseId,
                       @PathVariable Long lessonId,
                       Model model) {
        model.addAttribute("lesson", playerService.getLesson(courseId, lessonId));
        model.addAttribute("contentView", "player/video");
        model.addAttribute("title", "Reproduciendo");
        return "layouts/default";
    }

    @PostMapping("/{lessonId}/progress")
    @ResponseBody
    public void progress(@PathVariable Long lessonId,
                         @RequestBody ProgressRequest request) {
        playerService.updateProgress(lessonId, request.getProgress());
    }
}
