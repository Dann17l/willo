package com.willoacademy.features.auth;

import com.willoacademy.core.security.Role;
import com.willoacademy.shared.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestApiController {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public TestApiController(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/api/test/use")
    public ResponseEntity<Map<String, Object>> testRegister(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String rol) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (fullName == null || fullName.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre completo es obligatorio");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("El email es obligatorio");
            }
            if (rol == null || rol.trim().isEmpty()) {
                throw new IllegalArgumentException("El rol es obligatorio");
            }

            String cleanEmail = email.toLowerCase().trim();
            if (authRepository.findByEmail(cleanEmail).isPresent()) {
                response.put("status", "error");
                response.put("message", "El usuario ya existe con este email: " + cleanEmail);
                return ResponseEntity.badRequest().body(response);
            }

            User user = new User();
            user.setFullName(fullName.trim());
            user.setEmail(cleanEmail);
            user.setRole(Role.fromString(rol));

            // Default password is password123
            String defaultPassword = "password123";
            authRepository.save(user, passwordEncoder.encode(defaultPassword));

            response.put("status", "success");
            response.put("message", "Usuario registrado exitosamente");
            
            Map<String, String> data = new HashMap<>();
            data.put("fullName", user.getFullName());
            data.put("email", user.getEmail());
            data.put("role", user.getRole().name());
            data.put("defaultPassword", defaultPassword);
            response.put("data", data);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
