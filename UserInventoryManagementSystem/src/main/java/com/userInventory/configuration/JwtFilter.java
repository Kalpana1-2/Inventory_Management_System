package com.userInventory.configuration;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Get Authorization header
        String authHeader = request.getHeader("Authorization");

        String token = null;
        String email = null;

        // Check if header has Bearer token
        if (authHeader != null &&
            authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // remove "Bearer "
            try {
                email = jwtUtil.extractEmail(token);
            } catch (Exception e) {
                // invalid token — continue without auth
            }
        }

        // If token is valid set authentication
        if (email != null &&
            SecurityContextHolder.getContext()
                                 .getAuthentication() == null) {

            if (jwtUtil.validateToken(token)) {
                String role = jwtUtil.extractRole(token);

                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority(
                            "ROLE_" + role.toUpperCase()))
                    );

                SecurityContextHolder.getContext()
                                     .setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}