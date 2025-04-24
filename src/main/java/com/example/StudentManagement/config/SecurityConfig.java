package com.example.StudentManagement.config;

import com.example.StudentManagement.Utilities.JwtFilter;
import com.example.StudentManagement.Utilities.JwtAuthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security Configuration:
 * - Stateless session
 * - JWT filter integration
 * - Public endpoints for login/register
 * - Custom auth entry point to handle missing/invalid tokens
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final JwtFilter jwtFilter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    // Inject the JwtFilter and custom entry point
    @Autowired
    public SecurityConfig(JwtFilter jwtFilter, JwtAuthEntryPoint jwtAuthEntryPoint) {
        this.jwtFilter = jwtFilter;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    // Bean for password encryption
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configure security settings
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF (not needed for stateless APIs)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No session stored on server
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers("/api/auth/**").permitAll() // Allow access to login and registration
                            .anyRequest().authenticated(); // Require token for everything else
                })
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthEntryPoint)) // Use custom error handler
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Run JWT check before default authentication

        return http.build();
    }
}


