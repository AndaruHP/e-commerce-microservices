package com.ecommerce.authservice.service;

import com.ecommerce.authservice.dto.*;
import com.ecommerce.authservice.entity.RefreshToken;
import com.ecommerce.authservice.entity.User;
import com.ecommerce.authservice.repository.RefreshTokenRepository;
import com.ecommerce.authservice.repository.UserRepository;
import com.ecommerce.authservice.entity.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already exist");
        }

        String hashedPassword = passwordEncoder.encode(request.password());

        User user = new User(
                UUID.randomUUID(),
                request.name(),
                request.email(),
                hashedPassword,
                Role.ROLE_USER.name(),
                LocalDateTime.now()
        );

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId().toString(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Email is not exist"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Password is incorrect");
        }

        String accessToken = jwtService.generateAccessToken(
                user.getId(), user.getEmail(), user.getRole()
        );

        String refreshToken = jwtService.generateRefreshToken(user.getId());

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .token(refreshToken)
                .revoked(false)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now())
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        return new LoginResponse(
                user.getId().toString(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                accessToken,
                refreshToken
        );
    }

    @Override
    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (storedToken.isRevoked() || storedToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token already expired");
        }

        User user = userRepository.findById(storedToken.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtService.generateAccessToken(
                user.getId(), user.getEmail(), user.getRole()
        );

        String newRefreshToken = jwtService.generateRefreshToken(user.getId());

        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .token(newRefreshToken)
                .revoked(false)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now())
                .build();

        refreshTokenRepository.save(refreshTokenEntity);

        return new LoginResponse(
                user.getId().toString(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                newAccessToken,
                newRefreshToken
        );
    }
}