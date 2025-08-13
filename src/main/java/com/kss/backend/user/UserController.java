package com.kss.backend.user;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kss.backend.util.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * UserController
 */

@RestController
@RequestMapping(Api.Routes.USERS)
@Tag(name = "User Endpoints")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  ResponseEntity<List<User>> getUsers(@RequestParam Optional<User.Role> role) {
    var users = userService.findUsers(role);
    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

}
