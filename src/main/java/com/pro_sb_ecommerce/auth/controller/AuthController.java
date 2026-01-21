package com.pro_sb_ecommerce.auth.controller;

import com.pro_sb_ecommerce.auth.dto.AuthResponse;
import com.pro_sb_ecommerce.auth.dto.RegisterResponse;
import com.pro_sb_ecommerce.auth.dto.RegisterRequest;
import com.pro_sb_ecommerce.auth.dto.LoginRequest;
//import com.pro_sb_ecommerce.auth.model.User;
import com.pro_sb_ecommerce.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

//    @PostMapping("/register")
//    public User register(@RequestBody User user) {
//        return authService.register(user);
//    }

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }


//    @PostMapping("/login")
//    public User login(@RequestParam String email,
//                      @RequestParam String password) {
//        return authService.login(email, password);
//    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }


    @GetMapping("/hello")
    public String sayHello(){
        return "Hello";
    }
}
