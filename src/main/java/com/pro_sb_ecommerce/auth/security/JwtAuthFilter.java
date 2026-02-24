package com.pro_sb_ecommerce.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.contains("/auth/");
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        System.out.println("JwtAuthFilter running for: " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);

        // If no token → continue request
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String email;
        String role;

        try {
            email = jwtUtil.extractEmail(token);
            role = jwtUtil.extractRole(token);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Extracted email: " + email);
        System.out.println("Extracted role: " + role);

        // If user not already authenticated
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Spring Security requires ROLE_ prefix
            SimpleGrantedAuthority authority =
                    new SimpleGrantedAuthority("ROLE_" + role);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
//                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                            List.of(authority)
                    );

//            authToken.setDetails(
//                    new WebAuthenticationDetailsSource().buildDetails(request)
//            );

//            System.out.println("Before setting auth: "
//                    + SecurityContextHolder.getContext().getAuthentication());

            SecurityContextHolder.getContext().setAuthentication(authToken);

//            System.out.println("After setting auth: "
//                    + SecurityContextHolder.getContext().getAuthentication());
        }

        filterChain.doFilter(request, response);
    }


}

