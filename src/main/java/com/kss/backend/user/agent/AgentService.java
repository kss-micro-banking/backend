package com.kss.backend.user.agent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kss.backend.config.JwtService;
import com.kss.backend.user.User;
import com.kss.backend.user.UserService;
import com.kss.backend.user.agent.dto.AgentCreateDto;
import com.kss.backend.user.agent.dto.AgentLoginDto;
import com.kss.backend.user.dto.LoginResponse;
import com.kss.backend.user.dto.VerifyOtpDto;
import com.kss.backend.util.OtpService;

/**
 * AgentService
 */
@Service
public record AgentService(
        AgentRepository agentRepository,
        UserService userService,
        JwtService jwtService,
        PasswordEncoder passwordEncoder,
        OtpService otpService) {

    private static final Logger log = LoggerFactory.getLogger(AgentService.class);

    public Agent getAgentByEmail(String email) {
        User user = userService.findByEmail(email);
        if (user.getRole() != User.Role.AGENT) {
            throw new RuntimeException("This user is not an Agent");
        }
        return (Agent) user;
    }

    public Agent getAgentById(UUID id) {
        User user = userService.findById(id);
        if (user.getRole() != User.Role.AGENT) {
            throw new RuntimeException("This user is not an Agent");
        }
        return (Agent) user;
    }

    public Agent create(AgentCreateDto dto) {
        boolean userExists = userService.existsByEmail(dto.email());
        if (userExists) {
            throw new RuntimeException("User with email already exists");
        }

        Agent agent = new Agent();
        agent.setName(dto.name());
        agent.setEmail(dto.email());
        agent.setRole(User.Role.AGENT);
        return agentRepository.save(agent);
    }

    public boolean sendOtp(AgentLoginDto dto) {
        Agent agent = getAgentByEmail(dto.email());

        try {
            String otp = otpService.generateOtp();
            agent.setOtp(passwordEncoder.encode(otp));
            agent.setOtpExpiry(Instant.now().plus(5, ChronoUnit.MINUTES));
            agentRepository.save(agent);
            otpService.sendOtp(agent.getEmail(), otp);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    public LoginResponse verifyOtp(VerifyOtpDto dto) {
        Agent agent = getAgentByEmail(dto.email());

        if (agent.getOtp() == null || agent.getOtpExpiry() == null) {
            throw new RuntimeException("Request an otp to login.");
        }

        if (Instant.now().isAfter(agent.getOtpExpiry())) {
            log.error("otp does not match");
            throw new RuntimeException("Invalid OTP: Expired");
        }

        if (!passwordEncoder.matches(dto.otp(), agent.getOtp())) {
            log.error("otp does not match");
            throw new RuntimeException("Invalid OTP: Mismatch");
        }

        agent.setOtp(null);
        agent.setOtpExpiry(null);
        agentRepository.save(agent);

        String token = jwtService.generateToken(agent.getEmail(), User.Role.AGENT.toString());
        return new LoginResponse("Login success", token);
    }

    public void delete(UUID id) {
        Agent agent = this.getAgentById(id);
        agentRepository.deleteById(agent.getId());
    }
}
