package com.willoacademy.features.catalog.dto;

public class CourseSummary {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String thumbnail;
    private String duration;
    private int lessonsCount;

    public CourseSummary(Long id, String title, String description,
                         String category, String thumbnail,
                         String duration, int lessonsCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.thumbnail = thumbnail;
        this.duration = duration;
        this.lessonsCount = lessonsCount;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getThumbnail() { return thumbnail; }
    public String getDuration() { return duration; }
    public int getLessonsCount() { return lessonsCount; }
}
