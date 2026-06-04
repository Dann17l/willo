package com.willoacademy.features.auth;

import com.willoacademy.features.auth.dto.LoginRequest;
import com.willoacademy.features.auth.dto.RegisterRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        model.addAttribute("contentView", "auth/login");
        model.addAttribute("title", "Iniciar Sesión");
        return "layouts/default";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        model.addAttribute("contentView", "auth/register");
        model.addAttribute("title", "Crear Cuenta");
        return "layouts/default";
    }

    @PostMapping("/register")
    public String register(RegisterRequest request, Model model) {
        try {
            authService.register(request);
            return "redirect:/auth/login";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerRequest", request);
            model.addAttribute("contentView", "auth/register");
            model.addAttribute("title", "Crear Cuenta");
            return "layouts/default";
        }
    }
}
