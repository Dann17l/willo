package com.willoacademy.features.admin.dashboard;

import com.willoacademy.features.admin.dashboard.dto.StatsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final DashboardRepository repository;

    public DashboardService(DashboardRepository repository) {
        this.repository = repository;
    }

    public StatsResponse getStats() {
        return repository.getStats();
    }

    public List<Object[]> getWeeklyProgress() {
        return repository.getWeeklyProgress();
    }
}
