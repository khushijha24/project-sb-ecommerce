package com.pro_sb_ecommerce.auth.service;

import com.pro_sb_ecommerce.auth.dto.LoginResponse;
import com.pro_sb_ecommerce.auth.dto.RegisterResponse;
import com.pro_sb_ecommerce.auth.dto.LoginRequest;
import com.pro_sb_ecommerce.auth.dto.RegisterRequest;
import com.pro_sb_ecommerce.auth.model.User;
import com.pro_sb_ecommerce.auth.repository.UserRepository;
import com.pro_sb_ecommerce.auth.security.JwtUtil;
import com.pro_sb_ecommerce.exception.InvalidCredentialsException;
import com.pro_sb_ecommerce.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ✅ REGISTER (password encrypted)
    public RegisterResponse register(RegisterRequest request) {

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // 🔐
                .role(request.getRole())
                .build();

        userRepository.save(user);

//        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return new RegisterResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
//                token
        );
    }

    // ✅ LOGIN (password matched securely)
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with this Email"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                token
        );
    }
}

