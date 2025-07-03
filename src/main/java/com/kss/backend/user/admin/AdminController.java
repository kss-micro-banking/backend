package com.kss.backend.user.admin;

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

import com.kss.backend.user.User;
import com.kss.backend.user.UserService;
import com.kss.backend.user.admin.dto.AdminCreateDto;
import com.kss.backend.user.admin.dto.AdminLoginDto;
import com.kss.backend.user.dto.LoginResponse;
import com.kss.backend.user.dto.VerifyOtpDto;
import com.kss.backend.util.Api;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * AdminController
 */
@RestController
@RequestMapping(Api.Routes.ADMIN)
@Tag(name = "Admin Endpoints")
public class AdminController {

    private final AdminService adminService;

    public AdminController(UserService userService, AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    ResponseEntity<User> create(@Valid @RequestBody AdminCreateDto dto) {
        var user = adminService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    ResponseEntity<Boolean> login(@Valid @RequestBody AdminLoginDto dto) {
        boolean res = adminService.login(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/verify-otp")
    ResponseEntity<LoginResponse> verifyOtp(@Valid @RequestBody VerifyOtpDto dto) {
        LoginResponse res = adminService.verifyOtp(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    ResponseEntity<Void> delete(@RequestParam UUID adminId) {
        adminService.delete(adminId);
        return ResponseEntity.noContent().build();
    }
}
