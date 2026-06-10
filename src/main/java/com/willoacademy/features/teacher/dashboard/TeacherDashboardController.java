package com.willoacademy.features.teacher.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teacher/dashboard")
public class TeacherDashboardController {

    @GetMapping
    public String view(Model model) {
        model.addAttribute("contentView", "teacher/dashboard");
        model.addAttribute("title", "Dashboard - Profesor");
        return "layouts/default";
    }
}
