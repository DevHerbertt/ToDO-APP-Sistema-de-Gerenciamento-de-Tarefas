package com.ToDoAPI.ToDo_APi.controller;

import com.ToDoAPI.ToDo_APi.DTO.TaskCreateDTO;
import com.ToDoAPI.ToDo_APi.domain.Task;
import com.ToDoAPI.ToDo_APi.domain.User;
import com.ToDoAPI.ToDo_APi.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ✅ VERIFICAÇÃO SIMPLES DE ROLE
    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("ROLE_ADMIN"));
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks(Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).body("Access denied");
        }
        return ResponseEntity.ok(adminService.getAllTasksForAdmin());
    }

    @PostMapping("/tasks/{userId}")
    public ResponseEntity<?> createTaskForUser(@RequestBody TaskCreateDTO dto,
                                               @PathVariable Long userId,
                                               Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).body("Access denied");
        }
        return ResponseEntity.ok(adminService.createTaskForUser(dto, userId));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> deleteAnyTask(@PathVariable Long id, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).body("Access denied");
        }
        adminService.deleteAnyTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).body("Access denied");
        }
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<?> changeUserRole(@PathVariable Long userId,
                                            @RequestParam String newRole,
                                            Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).body("Access denied");
        }
        return ResponseEntity.ok(adminService.changeUserRole(userId, newRole));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).body("Access denied");
        }
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}