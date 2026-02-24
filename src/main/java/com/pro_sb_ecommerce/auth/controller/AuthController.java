package com.pro_sb_ecommerce.auth.controller;

import com.pro_sb_ecommerce.auth.dto.LoginResponse;
import com.pro_sb_ecommerce.auth.dto.RegisterResponse;
import com.pro_sb_ecommerce.auth.dto.RegisterRequest;
import com.pro_sb_ecommerce.auth.dto.LoginRequest;
//import com.pro_sb_ecommerce.auth.model.User;
import com.pro_sb_ecommerce.auth.service.AuthService;
import com.pro_sb_ecommerce.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        RegisterResponse data = authService.register(request);

        ApiResponse<RegisterResponse> response = ApiResponse.<RegisterResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("User registered successfully")
                .data(data)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {

        LoginResponse data = authService.login(request);

        ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Login successful")
                .data(data)
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/hello")
    public String sayHello(){
        return "Hello";
    }
}
