package com.willoacademy.features.admin.content.dto;

public class CourseForm {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String status;
    private String thumbnail;
    private String duration;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
}
