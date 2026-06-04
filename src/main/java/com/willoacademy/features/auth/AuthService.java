package com.willoacademy.features.auth;

import com.willoacademy.core.security.Role;
import com.willoacademy.features.auth.dto.RegisterRequest;
import com.willoacademy.shared.model.User;
import com.willoacademy.shared.util.Validators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthRepository repository;
    private final PasswordEncoder encoder;

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
        user.setName(Validators.sanitize(request.getName()));
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setRole(Role.STUDENT);
        repository.save(user, encoder.encode(request.getPassword()));
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));
    }
}
