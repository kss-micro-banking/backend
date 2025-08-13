package com.kss.backend.config;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kss.backend.util.Api;

/**
 * SecurityConfig
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig {

  private final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

  private final JwtFilter jwtFilter;

  public SecurityConfig(JwtFilter jwtFilter) {
    this.jwtFilter = jwtFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    var allowPaths = new String[] {
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/error",
        Api.Routes.ADMIN,
        Api.Routes.ADMIN + "/login",
        Api.Routes.ADMIN + "/verify-otp",
        Api.Routes.ADMIN + "/reset-password",
        Api.Routes.ADMIN + "/reset-password/*",
        Api.Routes.AGENT + "/login",
        Api.Routes.AGENT + "/verify-otp"

    };

    return http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(allowPaths).permitAll()
            .anyRequest().authenticated())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(handling -> handling
            .authenticationEntryPoint((request, response, ex) -> {
              log.error("Auth failed for: {}", request.getRequestURI());

              response.setContentType("application/problem+json");
              response.setStatus(HttpStatus.UNAUTHORIZED.value());

              ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
              problemDetail.setType(URI.create(request.getRequestURI()));
              problemDetail.setTitle("Unauthorized");
              problemDetail.setDetail(ex.getMessage());
              problemDetail.setInstance(URI.create(request.getRequestURI()));

              new ObjectMapper().writeValue(response.getWriter(), problemDetail);
            })
            .accessDeniedHandler((request, response, ex) -> {

              response.setContentType("application/problem+json");
              response.setStatus(HttpStatus.FORBIDDEN.value());

              ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
              problemDetail.setType(URI.create(request.getRequestURI()));
              problemDetail.setTitle("Access denied");
              problemDetail.setDetail(ex.getMessage());
              problemDetail.setInstance(URI.create(request.getRequestURI()));

              new ObjectMapper().writeValue(response.getWriter(), problemDetail);
            }))
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("*"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
    configuration.setAllowedHeaders(List.of("Content-Type", "Authorization"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
