package com.pro_sb_ecommerce.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/test")
public class RoleTestController {

    @GetMapping("/public")
    public String publicApi() {
        return "Public API - anyone can access";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String userApi() {
        return "USER API - only USER can access";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminApi() {
        return "ADMIN API - only ADMIN can access";
    }
}

//@RestController
//@RequestMapping("/api/test")
//public class TestController {
//
//    @GetMapping
//    public String testJwt() {
//        return "JWT is working successfully!";
//    }
//}
