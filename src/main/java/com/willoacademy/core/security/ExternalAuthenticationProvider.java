package com.willoacademy.core.security;

import com.willoacademy.features.auth.AuthRepository;
import com.willoacademy.shared.model.User;
import com.willoacademy.shared.util.Validators;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
public class ExternalAuthenticationProvider implements AuthenticationProvider {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${willo.external-api-url}")
    private String externalApiUrl;

    public ExternalAuthenticationProvider(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Check if the username is a valid email format before calling external API
        boolean isAuthenticatedExternally = false;
        if (Validators.isValidEmail(email)) {
            isAuthenticatedExternally = callExternalLoginApi(email, password);
        }

        User user;
        if (isAuthenticatedExternally) {
            // Find or sync the user locally
            user = authRepository.findByEmail(email.toLowerCase().trim()).orElseGet(() -> {
                User newUser = new User();
                newUser.setFullName(email.split("@")[0]);
                newUser.setEmail(email.toLowerCase().trim());
                newUser.setRole(Role.STUDENT);
                authRepository.save(newUser, passwordEncoder.encode(password));
                return newUser;
            });
        } else {
            // Fallback: check local database
            user = authRepository.findByEmail(email.toLowerCase().trim())
                    .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Credenciales inválidas");
            }
        }

        return new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                password,
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getAuthority()))
        );
    }

    private boolean callExternalLoginApi(String email, String password) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String requestBody = String.format("username=%s&password=%s",
                    URLEncoder.encode(email, StandardCharsets.UTF_8),
                    URLEncoder.encode(password, StandardCharsets.UTF_8)
            );

            String url = externalApiUrl + "/auth/login";
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("ngrok-skip-browser-warning", "true")
                    .header("X-Tunnel-Skip-AntiPhishing-Page", "True")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
            System.out.println("External Login API response status: " + response.statusCode());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return true;
            }
            if (response.statusCode() >= 300 && response.statusCode() < 400) {
                String location = response.headers().firstValue("Location").orElse("");
                System.out.println("External Login API redirect location: " + location);
                if (location.contains("error") || location.contains("login")) {
                    return false;
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error calling external login API: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
