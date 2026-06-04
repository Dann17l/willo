package com.willoacademy.features.catalog.dto;

import java.util.List;

public class CourseDetail {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String thumbnail;
    private String duration;
    private int lessonsCount;
    private List<LessonItem> lessons;

    public static class LessonItem {
        private Long id;
        private String title;
        private String duration;

        public LessonItem(Long id, String title, String duration) {
            this.id = id; this.title = title; this.duration = duration;
        }

        public Long getId() { return id; }
        public String getTitle() { return title; }
        public String getDuration() { return duration; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public int getLessonsCount() { return lessonsCount; }
    public void setLessonsCount(int lessonsCount) { this.lessonsCount = lessonsCount; }

    public List<LessonItem> getLessons() { return lessons; }
    public void setLessons(List<LessonItem> lessons) { this.lessons = lessons; }
}
