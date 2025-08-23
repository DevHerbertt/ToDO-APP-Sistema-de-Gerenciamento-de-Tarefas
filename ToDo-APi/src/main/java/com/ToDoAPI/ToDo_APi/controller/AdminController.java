package com.ToDoAPI.ToDo_APi.controller;

import com.ToDoAPI.ToDo_APi.DTO.TaskCreateDTO;
import com.ToDoAPI.ToDo_APi.domain.Task;
import com.ToDoAPI.ToDo_APi.domain.User;
import com.ToDoAPI.ToDo_APi.service.AdminService;
import com.ToDoAPI.ToDo_APi.util.IsAdminUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@Log4j2
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final IsAdminUtil isAdminUtil;

    public AdminController(AdminService adminService,IsAdminUtil isAdminUtil) {
        this.adminService = adminService;
        this.isAdminUtil = isAdminUtil;
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks(Authentication authentication) {
        if (!isAdminUtil.isAdmin(authentication)) {
            log.warn("User is not admin - attempted to access getAllTasks at {}", LocalDateTime.now());
            return ResponseEntity.status(403).body("Access denied");
        }
        log.info("Showing the Tasks");
        return ResponseEntity.ok(adminService.getAllTasksForAdmin());
    }

    @PostMapping("/tasks/{userId}")
    public ResponseEntity<?> createTaskForUser(@RequestBody TaskCreateDTO dto, @PathVariable Long userId, Authentication authentication) {
        if (!isAdminUtil.isAdmin(authentication)) {
            log.warn("User is not admin - attempted to access createTaskForUser at {}", LocalDateTime.now());
            return ResponseEntity.status(403).body("Access denied");
        }
        return ResponseEntity.ok(adminService.createTaskForUser(dto, userId));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> deleteAnyTask(@PathVariable Long id, Authentication authentication) {
        if (!isAdminUtil.isAdmin(authentication)) {
            log.warn("User is not admin - attempted to access deleteAnyTask at {}", LocalDateTime.now());
            return ResponseEntity.status(403).body("Access denied");
        }
        adminService.deleteAnyTask(id);
        log.info("TaskId: {} Deleted",id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(Authentication authentication) {
        if (!isAdminUtil.isAdmin(authentication)) {
            log.warn("User is not admin - attempted to access getAllUsers at {}", LocalDateTime.now());
            return ResponseEntity.status(403).body("Access denied");
        }
        log.info("Showing the Users");
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<?> changeUserRole(@PathVariable Long userId, @RequestParam String newRole, Authentication authentication) {
        if (!isAdminUtil.isAdmin(authentication)) {
            log.warn("User is not admin - attempted to access changeUserRole at {}", LocalDateTime.now());
            return ResponseEntity.status(403).body("Access denied");
        }
        log.info("The role of UserId {} change at {}",userId, LocalDateTime.now());
        return ResponseEntity.ok(adminService.changeUserRole(userId, newRole));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, Authentication authentication) {
        if (!isAdminUtil.isAdmin(authentication)) {
            log.warn("User is not admin - attempted to access deleteUser at {}", LocalDateTime.now());
            return ResponseEntity.status(403).body("Access denied");
        }
        adminService.deleteUser(userId);
        log.info("User {} Deleted",userId);
        return ResponseEntity.noContent().build();
    }
}