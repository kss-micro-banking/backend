package com.kss.backend.user.admin;

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
import com.kss.backend.user.admin.dto.AdminCreateDto;
import com.kss.backend.user.admin.dto.AdminLoginDto;
import com.kss.backend.user.dto.LoginResponse;
import com.kss.backend.user.dto.VerifyOtpDto;
import com.kss.backend.util.OtpService;

/**
 * AdminService
 */
@Service
public record AdminService(
    AdminRepository adminRepository,
    UserService userService,
    JwtService jwtService,
    PasswordEncoder passwordEncoder,
    OtpService otpService) {

  private static final Logger log = LoggerFactory.getLogger(AdminService.class);

  public Admin init(AdminCreateDto dto) {
    var hasBeenSetup = userService.hasBeenSetup();

    if (hasBeenSetup) {
      throw new RuntimeException("App has already been setup");
    } else {
      Admin admin = new Admin();
      admin.setName(dto.name());
      admin.setEmail(dto.email());
      admin.setPassword(passwordEncoder.encode(dto.password()));
      admin.setRole(User.Role.ADMIN);
      admin.setSuperAdmin(true);
      return adminRepository.save(admin);
    }
  }

  Admin getAdminByEmail(String email) {
    Admin user = (Admin) userService.findByEmail(email);
    if (user.getRole() != User.Role.ADMIN) {
      throw new RuntimeException("This user is not an Admin");
    }
    return user;
  }

  Admin getAdminById(UUID id) {
    Admin user = (Admin) userService.findById(id);
    if (user.getRole() != User.Role.ADMIN) {
      throw new RuntimeException("This user is not an Admin");
    }
    return user;
  }

  public Admin create(AdminCreateDto dto) {
    boolean userExists = userService.existsByEmail(dto.email());
    if (userExists) {
      throw new RuntimeException("User with email already exists");
    }
    Admin admin = new Admin();
    admin.setName(dto.name());
    admin.setEmail(dto.email());
    admin.setPassword(passwordEncoder.encode(dto.password()));
    admin.setRole(User.Role.ADMIN);
    admin.setSuperAdmin(dto.superAdmin());
    return adminRepository.save(admin);
  }

  public boolean login(AdminLoginDto dto) {
    Admin admin = getAdminByEmail(dto.email());

    if (passwordEncoder.matches(passwordEncoder.encode(dto.password()), admin.getPassword())) {
      throw new RuntimeException("Invalid credentials");
    }

    try {
      String otp = otpService.generateOtp();
      admin.setOtp(passwordEncoder.encode(otp));
      admin.setOtpExpiry(Instant.now().plus(5, ChronoUnit.MINUTES));
      adminRepository.save(admin);
      otpService.sendOtp(admin.getEmail(), otp);
      return true;
    } catch (Exception ex) {
      log.error(ex.getMessage());
      return false;
    }
  }

  public LoginResponse verifyOtp(VerifyOtpDto dto) {
    Admin admin = getAdminByEmail(dto.email());

    if (admin.getOtp() == null || admin.getOtpExpiry() == null) {
      throw new RuntimeException("Request an otp to login.");
    }

    if (Instant.now().isAfter(admin.getOtpExpiry())) {
      log.error("otp does not match");
      throw new RuntimeException("Invalid OTP: Expired");
    }

    if (!passwordEncoder.matches(dto.otp(), admin.getOtp())) {
      log.error("otp does not match");
      throw new RuntimeException("Invalid OTP: Mismatch");
    }

    admin.setOtp(null);
    admin.setOtpExpiry(null);
    adminRepository.save(admin);

    String token = jwtService.generateToken(admin.getEmail(), User.Role.ADMIN.toString());
    return new LoginResponse("Login success", token);
  }

  public void delete(UUID id) {
    this.getAdminById(id);
    adminRepository.deleteById(id);
  }

}
