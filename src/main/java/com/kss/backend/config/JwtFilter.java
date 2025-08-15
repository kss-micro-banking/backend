package com.kss.backend.config;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JwtFilter
 */
@Component
class JwtFilter extends OncePerRequestFilter {

  private final Logger log = LoggerFactory.getLogger(JwtFilter.class);

  private final JwtService jwtService;

  public JwtFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    log.info("{}: {}", request.getMethod(), request.getRequestURL());

    try {
      String authHeader = request.getHeader("Authorization");

      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);

        String email = jwtService.getEmailFromToken(token);
        String role = jwtService.getRoleFromToken(token);

        var userVerified = jwtService.verifyUser(token);
        if (userVerified) {
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
              email,
              null,
              Collections.singletonList(new SimpleGrantedAuthority(String.format("ROLE_%s", role))));

          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      }

      filterChain.doFilter(request, response);

    } catch (AuthenticationException ex) {
      SecurityContextHolder.clearContext();
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
    } catch (Exception ex) {
      SecurityContextHolder.clearContext();
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
    }
  }
}
