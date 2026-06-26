package com.userInventory.configuration;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config
.annotation.web.builders.HttpSecurity;

import org.springframework.security.config
.annotation.web.configuration
.EnableWebSecurity;

import org.springframework.security.config.http
.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt
.BCryptPasswordEncoder;

import org.springframework.security.crypto.password
.PasswordEncoder;

import org.springframework.security.web
.SecurityFilterChain;

import org.springframework.security.web
.authentication
.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity

public class SecurityConfig {
	
    @Autowired

    private JwtFilter jwtFilter;

    @Bean

    public SecurityFilterChain filterChain(

            HttpSecurity http)

            throws Exception {

    	http

    	.cors(cors -> {})

    	.csrf(csrf -> csrf.disable())

        .sessionManagement(

        session ->

        session.sessionCreationPolicy(

        SessionCreationPolicy.STATELESS))

        .authorizeHttpRequests(auth -> auth

        .requestMatchers(

        "/auth/**")

        .permitAll()

        .requestMatchers(

        "/swagger-ui/**",

        "/v3/api-docs/**",

        "/api-docs/**")

        .permitAll()

        .anyRequest()

        .authenticated()

        );

        http.addFilterBefore(

        jwtFilter,

        UsernamePasswordAuthenticationFilter.class

        );

        return http.build();

    }
    @Bean
	public CorsConfigurationSource corsConfigurationSource() {

	    CorsConfiguration configuration =
	            new CorsConfiguration();

	    configuration.addAllowedOrigin(
	            "http://localhost:5000");

	    configuration.addAllowedMethod("*");

	    configuration.addAllowedHeader("*");

	    configuration.setAllowCredentials(true);

	    UrlBasedCorsConfigurationSource source =
	            new UrlBasedCorsConfigurationSource();

	    source.registerCorsConfiguration(
	            "/**",
	            configuration);

	    return source;
	}

    @Bean

    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

}