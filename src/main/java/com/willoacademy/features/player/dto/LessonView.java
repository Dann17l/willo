package com.willoacademy.features.player.dto;

public class LessonView {
    private Long id;
    private String title;
    private String description;
    private String videoUrl;
    private int progress;

    public LessonView(Long id, String title, String description,
                      String videoUrl, int progress) {
        this.id = id; this.title = title;
        this.description = description; this.videoUrl = videoUrl;
        this.progress = progress;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getVideoUrl() { return videoUrl; }
    public int getProgress() { return progress; }
}
