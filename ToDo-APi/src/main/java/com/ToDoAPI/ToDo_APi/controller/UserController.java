package com.ToDoAPI.ToDo_APi.controller;

import com.ToDoAPI.ToDo_APi.DTO.UserDto;
import com.ToDoAPI.ToDo_APi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;  // ou @Slf4j se preferir
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
@Log4j2
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        System.out.println("==========================================");
        log.info("Starting the method login");
        log.debug("Received userDto for registration: {}", userDto);
        try {
            userService.registerAccount(userDto);
            log.info("User registered successfully: {}", userDto.getEmail());
            return ResponseEntity.status(201).body("User registered successfully");
        } catch (IllegalArgumentException e) {
            log.warn("User registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        System.out.println("==========================================");
        log.info("Starting the method login, User Email : {} ", userDto.getEmail());
        try {
            String token = userService.login(userDto);
            log.info(" Token generated successfully for user {}", userDto.getEmail());

            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "token", token,
                            "message", "Login successful"
                    ));

        } catch (RuntimeException e) {
            log.warn("Login failed for user {}: {}", userDto.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(401)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        } catch (Exception e) {
            log.error(" Unexpected error during login for user {}: {}", userDto.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body(Map.of(
                            "success", false,
                            "message", "Internal server error"
                    ));
        }
    }
}
