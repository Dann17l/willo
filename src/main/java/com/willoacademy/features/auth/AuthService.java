package com.willoacademy.features.auth;

import com.willoacademy.core.security.Role;
import com.willoacademy.features.auth.dto.RegisterRequest;
import com.willoacademy.shared.model.User;
import com.willoacademy.shared.util.Validators;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class AuthService {

    private final AuthRepository repository;
    private final PasswordEncoder encoder;

    @Value("${willo.external-api-url}")
    private String externalApiUrl;

    public AuthService(AuthRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public void register(RegisterRequest request) {
        if (!Validators.isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }
        if (!Validators.isValidPassword(request.getPassword())) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("El email ya está registrado");
        }
        User user = new User();
        user.setFullName(Validators.sanitize(request.getName()));
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setRole(Role.STUDENT);
        repository.save(user, encoder.encode(request.getPassword()));

        callExternalApi(user.getFullName(), user.getEmail(), request.getPassword(), user.getRole().name());
    }

    private void callExternalApi(String fullName, String email, String password, String role) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String requestBody = String.format("fullName=%s&email=%s&password=%s&rol=%s&role=%s",
                    URLEncoder.encode(fullName, StandardCharsets.UTF_8),
                    URLEncoder.encode(email, StandardCharsets.UTF_8),
                    URLEncoder.encode(password, StandardCharsets.UTF_8),
                    URLEncoder.encode(role, StandardCharsets.UTF_8),
                    URLEncoder.encode(role, StandardCharsets.UTF_8)
            );

            String url = externalApiUrl + "/api/auth/register";
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("ngrok-skip-browser-warning", "true")
                    .header("X-Tunnel-Skip-AntiPhishing-Page", "True")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Envío asíncrono para no bloquear la experiencia de usuario
            client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println("API Register response status: " + response.statusCode());
                        System.out.println("API Register response body: " + response.body());
                    })
                    .exceptionally(ex -> {
                        System.err.println("Failed to call API: " + ex.getMessage());
                        return null;
                    });
        } catch (Exception e) {
            System.err.println("Error invoking API: " + e.getMessage());
        }
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));
    }
}
