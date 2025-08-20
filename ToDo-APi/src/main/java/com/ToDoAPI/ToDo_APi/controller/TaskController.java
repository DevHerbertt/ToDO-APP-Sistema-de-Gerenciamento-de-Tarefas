package com.ToDoAPI.ToDo_APi.controller;


import com.ToDoAPI.ToDo_APi.DTO.TaskCreateDTO;
import com.ToDoAPI.ToDo_APi.DTO.TaskEditDTO;
import com.ToDoAPI.ToDo_APi.DTO.TaskFilterDTO;
import com.ToDoAPI.ToDo_APi.domain.Task;
import com.ToDoAPI.ToDo_APi.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskCreateDTO dto) {

        System.out.println("Recebendo tarefa: " + dto); // Debug
        try {
            Task task = taskService.createTask(dto);
            System.out.println("Tarefa criada: " + task); // Debug
            return ResponseEntity.status(201).body(task);
        } catch (Exception e) {
            System.out.println("Erro ao criar tarefa: " + e.getMessage()); // Debug
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        try {
            List<Task> tasks = taskService.getAllTasksByLoggedUser();
            return ResponseEntity.ok(tasks);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Task>> filterTasks(@RequestBody TaskFilterDTO filterDTO) {
        System.out.println("=== FILTER CONTROLLER ===");
        System.out.println("📥 Received filter DTO: " + filterDTO);
        System.out.println("🔍 Status value: " + filterDTO.getStatus());
        System.out.println("🔍 Status type: " + (filterDTO.getStatus() != null ? filterDTO.getStatus().getClass().getSimpleName() : "null"));
        System.out.println("🔍 Title: " + filterDTO.getTitle());
        System.out.println("🔍 DueData: " + filterDTO.getDueData());
        System.out.println("🔍 Priority: " + filterDTO.getPriority());

        try {
            List<Task> tasks = taskService.filterTasks(filterDTO);
            System.out.println("✅ Tasks found: " + tasks.size());
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            System.out.println("❌ Error in filter: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping
    public ResponseEntity<Task> updateTask(@RequestBody TaskEditDTO dto) {
        Task task = taskService.updateTask(dto);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id);
    }
}

