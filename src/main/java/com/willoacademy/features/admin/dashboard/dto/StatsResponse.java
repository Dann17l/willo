package com.willoacademy.features.admin.dashboard.dto;

public class StatsResponse {
    private int activeUsers;
    private int publishedCourses;
    private int completedLessons;
    private double revenue;

    public StatsResponse(int activeUsers, int publishedCourses,
                         int completedLessons, double revenue) {
        this.activeUsers = activeUsers;
        this.publishedCourses = publishedCourses;
        this.completedLessons = completedLessons;
        this.revenue = revenue;
    }

    public int getActiveUsers() { return activeUsers; }
    public int getPublishedCourses() { return publishedCourses; }
    public int getCompletedLessons() { return completedLessons; }
    public double getRevenue() { return revenue; }
}
