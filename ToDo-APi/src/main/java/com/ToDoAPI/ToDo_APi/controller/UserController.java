package com.ToDoAPI.ToDo_APi.controller;

import com.ToDoAPI.ToDo_APi.DTO.UserDto;
import com.ToDoAPI.ToDo_APi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        System.out.println("Received userDto: " + userDto); // <-- log para debug
        try {
            userService.registerAccount(userDto);
            return ResponseEntity.status(201).body("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        System.out.println("=== LOGIN CONTROLLER START ===");
        try {
            String token = userService.login(userDto);
            System.out.println("✅ Token generated successfully");

            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "token", token,
                            "message", "Login successful"
                    ));

        } catch (RuntimeException e) {
            System.out.println("❌ Login failed: " + e.getMessage());
            e.printStackTrace(); // ← ADICIONE ESTA LINHA

            return ResponseEntity.status(401)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace(); // ← ADICIONE ESTA LINHA

            return ResponseEntity.status(500)
                    .body(Map.of(
                            "success", false,
                            "message", "Internal server error"
                    ));
        }
    }
}
