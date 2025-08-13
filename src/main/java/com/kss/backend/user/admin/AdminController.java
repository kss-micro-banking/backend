package com.kss.backend.user.admin;

import java.security.Principal;
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
import com.kss.backend.user.admin.dto.ResetPasswordDto;
import com.kss.backend.user.admin.dto.SendPasswordResetLinkDto;
import com.kss.backend.user.dto.LoginResponse;
import com.kss.backend.user.dto.VerifyOtpDto;
import com.kss.backend.util.Api;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

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

  @PostMapping("init")
  ResponseEntity<User> init(@Valid @RequestBody AdminCreateDto dto) {
    Admin admin = adminService.init(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(admin);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  ResponseEntity<User> create(@Valid @RequestBody AdminCreateDto dto, Principal principal) {
    System.out.println(principal);
    Admin admin = adminService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(admin);
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

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping
  ResponseEntity<Void> delete(@RequestParam UUID adminId) {
    adminService.delete(adminId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/reset-password/send")
  ResponseEntity<Void> sendPasswordResetLink(@RequestBody SendPasswordResetLinkDto dto) {
    adminService.sendPasswordResetLink(dto.email());
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping("/reset-password")
  ResponseEntity<Void> resetPassword(@PathParam("otp") String otp, @RequestBody ResetPasswordDto dto) {
    adminService.resetPassoword(dto, otp);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
