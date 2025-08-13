package com.kss.backend.user.agent;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kss.backend.user.agent.dto.AgentCreateDto;
import com.kss.backend.user.agent.dto.AgentLoginDto;
import com.kss.backend.user.dto.LoginResponse;
import com.kss.backend.user.dto.VerifyOtpDto;
import com.kss.backend.util.Api;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * UserController
 */
@RestController
@RequestMapping(Api.Routes.AGENT)
@Tag(name = "Agent Endpoints")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<Agent> create(@Valid @RequestBody AgentCreateDto dto) {
        Agent agent = agentService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(agent);
    }

    @PostMapping("/login")
    ResponseEntity<Boolean> login(@Valid @RequestBody AgentLoginDto dto) {
        boolean res = agentService.sendOtp(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/verify-otp")
    ResponseEntity<LoginResponse> verifyOtp(@Valid @RequestBody VerifyOtpDto dto) {
        LoginResponse res = agentService.verifyOtp(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    ResponseEntity<Void> delete(@RequestParam UUID agentId) {
        agentService.delete(agentId);
        return ResponseEntity.noContent().build();
    }

}
