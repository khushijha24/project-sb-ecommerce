package com.pro_sb_ecommerce.auth.service;

import com.pro_sb_ecommerce.auth.dto.AuthResponse;
import com.pro_sb_ecommerce.auth.dto.LoginRequest;
import com.pro_sb_ecommerce.auth.dto.RegisterRequest;
import com.pro_sb_ecommerce.auth.model.User;
import com.pro_sb_ecommerce.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    // Constructor Injection
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public User register(User user) {
//        return userRepository.save(user);
//    }
//
//    public User login(String email, String password) {
//        return userRepository.findByEmail(email)
//                .filter(u -> u.getPassword().equals(password))
//                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
//    }


    //REGISTER
    public AuthResponse register(RegisterRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // ⚠ plain for now (bcrypt later)
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    //LOGIN

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .filter(u -> u.getPassword().equals(request.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        return new AuthResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

}
