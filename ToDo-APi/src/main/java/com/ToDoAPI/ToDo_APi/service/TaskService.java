package com.ToDoAPI.ToDo_APi.service;

import com.ToDoAPI.ToDo_APi.DTO.TaskCreateDTO;
import com.ToDoAPI.ToDo_APi.DTO.TaskEditDTO;
import com.ToDoAPI.ToDo_APi.DTO.TaskFilterDTO;
import com.ToDoAPI.ToDo_APi.domain.Task;
import com.ToDoAPI.ToDo_APi.domain.User;
import com.ToDoAPI.ToDo_APi.repository.TaskRepository;
import com.ToDoAPI.ToDo_APi.repository.UserRespository;
import com.ToDoAPI.ToDo_APi.util.TaskSpecificationUtil;
import com.ToDoAPI.ToDo_APi.util.PriorityTask; // ✅ Importe o enum
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRespository userRespository;

    public Task createTask(TaskCreateDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        System.out.println("=== CREATE TASK SERVICE ===");
        System.out.println("DTO: " + dto);
        System.out.println("DueData: " + dto.getDueData());
        System.out.println("Priority: " + dto.getPriority());

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueData(dto.getDueData()); // ✅ Já está correto
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdateAt(LocalDateTime.now());
        task.setStatus(false);

        // ✅ CONVERSÃO CORRETA DA PRIORIDADE
        if (dto.getPriority() != null && !dto.getPriority().isEmpty()) {
            try {
                PriorityTask priority = PriorityTask.valueOf(dto.getPriority().toUpperCase());
                task.setPriority(priority);
                System.out.println("Priority converted: " + priority);
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Invalid priority: " + dto.getPriority());
                // Define uma prioridade padrão instead de lançar erro
                task.setPriority(PriorityTask.MEDIUM);
            }
        } else {
            System.out.println("ℹ️ Priority is null, setting default: MEDIUM");
            task.setPriority(PriorityTask.MEDIUM); // Valor padrão
        }

        User user = userRespository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        task.setCreator(user);

        Task savedTask = taskRepository.save(task);
        System.out.println("✅ Task saved: " + savedTask);
        return savedTask;
    }

    public List<Task> filterTasks(TaskFilterDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println("=== FILTER SERVICE ===");
        System.out.println("👤 User email: " + email);
        System.out.println("🎯 Filter DTO: " + dto);

        Specification<Task> spec = TaskSpecificationUtil.filter(dto, email);

        System.out.println("🔍 Specification created");
        List<Task> tasks = taskRepository.findAll(spec);

        System.out.println("📊 Tasks found: " + tasks.size());
        for (Task task : tasks) {
            System.out.println("   - " + task.getTitle() + " (Status: " + task.isStatus() + ")");
        }

        return tasks;
    }

    public List<Task> getAllTasksByLoggedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRespository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Task> tasks = taskRepository.findByCreator(user);
        if (tasks.isEmpty()) {
            throw new RuntimeException("No tasks found for user " + email);
        }
        return tasks;
    }

    public Task updateTask(TaskEditDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<Task> optionalTask = taskRepository.findById(dto.getId());
        if (optionalTask.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        Task task = optionalTask.get();

        if (!task.getCreator().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to update this task");
        }

        if (dto.getTitle() != null) task.setTitle(dto.getTitle());
        if (dto.getDescription() != null) task.setDescription(dto.getDescription());
        if (dto.getDueData() != null) task.setDueData(dto.getDueData());
        task.setUpdateAt(LocalDateTime.now());
        task.setStatus(dto.isStatus());

        // ✅ CONVERSÃO DA PRIORIDADE NO UPDATE TAMBÉM
        if (dto.getPriority() != null && !dto.getPriority().isEmpty()) {
            try {
                PriorityTask priority = PriorityTask.valueOf(dto.getPriority().toUpperCase());
                task.setPriority(priority);
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Invalid priority in update: " + dto.getPriority());
                task.setPriority(PriorityTask.MEDIUM);
            }
        }

        return taskRepository.save(task);
    }

    public ResponseEntity<?> deleteTask(long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        Task task = optionalTask.get();
        if (!task.getCreator().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to delete this task");
        }

        taskRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}