package com.willoacademy.features.admin.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String view(Model model) {
        model.addAttribute("stats", dashboardService.getStats());
        model.addAttribute("weeklyData", dashboardService.getWeeklyProgress());
        model.addAttribute("contentView", "admin/dashboard");
        model.addAttribute("title", "Dashboard - Admin");
        return "layouts/default";
    }
}
