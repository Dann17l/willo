package com.willoacademy.features.dashboard;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("studentDashboardController")
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    public String showStudentDashboard(Authentication authentication) {
        if (authentication != null) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isTeacher = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"));

            if (isAdmin) {
                return "redirect:/admin/dashboard";
            } else if (isTeacher) {
                return "redirect:/teacher/dashboard";
            }
        }
        return "dashboard/student";
    }

    @GetMapping("/certificates")
    public String showCertificates() {
        return "dashboard/certificates";
    }


    @GetMapping("/settings/public-profile")
    public String showPublicProfile() {
        return "dashboard/settings/public-profile";
    }

    @GetMapping("/settings/profile")
    public String showProfile() {
        return "dashboard/settings/profile";
    }

    @GetMapping("/settings/security")
    public String showSecurity() {
        return "dashboard/settings/security";
    }

    @GetMapping("/settings/privacy")
    public String showPrivacy() {
        return "dashboard/settings/privacy";
    }

    @GetMapping("/settings/notifications")
    public String showNotifications() {
        return "dashboard/settings/notifications";
    }

    @GetMapping("/settings/close-account")
    public String showCloseAccount() {
        return "dashboard/settings/close-account";
    }
}
