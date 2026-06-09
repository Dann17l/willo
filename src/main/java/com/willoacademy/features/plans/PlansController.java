package com.willoacademy.features.plans;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlansController {

    @GetMapping("/plans")
    public String showPlans() {
        return "plans";
    }
}
