package com.willoacademy.features.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("studentDashboardController")
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    public String showStudentDashboard() {
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

    @GetMapping("/settings/subscriptions")
    public String showSubscriptions() {
        return "dashboard/settings/subscriptions";
    }

    @GetMapping("/settings/payment")
    public String showPayment() {
        return "dashboard/settings/payment";
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
