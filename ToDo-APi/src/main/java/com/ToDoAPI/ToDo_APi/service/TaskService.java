package com.ToDoAPI.ToDo_APi.service;

import com.ToDoAPI.ToDo_APi.DTO.TaskCreateDTO;
import com.ToDoAPI.ToDo_APi.DTO.TaskEditDTO;
import com.ToDoAPI.ToDo_APi.DTO.TaskFilterDTO;
import com.ToDoAPI.ToDo_APi.domain.Task;
import com.ToDoAPI.ToDo_APi.domain.User;
import com.ToDoAPI.ToDo_APi.repository.TaskRepository;
import com.ToDoAPI.ToDo_APi.repository.UserRespository;
import com.ToDoAPI.ToDo_APi.util.TaskSpecificationUtil;
import com.ToDoAPI.ToDo_APi.util.PriorityTask;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRespository userRespository;

    public Task createTask(TaskCreateDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        System.out.println("==========================================");
        log.info("Starting the method createTask for user {}", email);
        log.debug("DTO received: {}", dto);
        log.debug("DueData: {}", dto.getDueData());
        log.debug("Priority: {}", dto.getPriority());

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueData(dto.getDueData());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdateAt(LocalDateTime.now());
        task.setStatus(false);

        if (dto.getPriority() != null && !dto.getPriority().isEmpty()) {
            try {
                PriorityTask priority = PriorityTask.valueOf(dto.getPriority().toUpperCase());
                task.setPriority(priority);
                log.debug("Priority converted successfully: {}", priority);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid priority value '{}', setting default MEDIUM", dto.getPriority());
                task.setPriority(PriorityTask.MEDIUM);
            }
        } else {
            log.info("Priority is null or empty, setting default: MEDIUM");
            task.setPriority(PriorityTask.MEDIUM);
        }

        User user = userRespository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        task.setCreator(user);

        Task savedTask = taskRepository.save(task);
        log.info("Task saved successfully: {}", savedTask);
        return savedTask;
    }

    public List<Task> filterTasks(TaskFilterDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("==========================================");
        log.info("Starting filterTasks for user {}", email);
        log.debug("Filter DTO: {}", dto);

        Specification<Task> spec = TaskSpecificationUtil.filter(dto, email);

        log.debug("Specification created for filtering");
        List<Task> tasks = taskRepository.findAll(spec);

        log.info("Tasks found: {}", tasks.size());
        tasks.forEach(task -> log.debug("Task title: '{}' - Status: {}", task.getTitle(), task.isStatus()));

        return tasks;
    }

    public List<Task> getAllTasksByLoggedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("==========================================");
        log.info("Fetching all tasks for user {}", email);
        User user = userRespository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Task> tasks = taskRepository.findByCreator(user);
        if (tasks.isEmpty()) {
            log.warn("No tasks found for user {}", email);
            throw new RuntimeException("No tasks found for user " + email);
        }
        log.info("Found {} tasks for user {}", tasks.size(), email);
        return tasks;
    }

    public Task updateTask(TaskEditDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        System.out.println("==========================================");
        log.info("Updating task with ID {} by user {}", dto.getId(), email);
        Optional<Task> optionalTask = taskRepository.findById(dto.getId());
        if (optionalTask.isEmpty()) {
            log.warn("Task with ID {} not found", dto.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        Task task = optionalTask.get();

        if (!task.getCreator().getEmail().equals(email)) {
            log.warn("User {} tried to update task ID {} without permission", email, dto.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to update this task");
        }

        if (dto.getTitle() != null) task.setTitle(dto.getTitle());
        if (dto.getDescription() != null) task.setDescription(dto.getDescription());
        if (dto.getDueData() != null) task.setDueData(dto.getDueData());
        task.setUpdateAt(LocalDateTime.now());
        task.setStatus(dto.isStatus());

        if (dto.getPriority() != null && !dto.getPriority().isEmpty()) {
            try {
                PriorityTask priority = PriorityTask.valueOf(dto.getPriority().toUpperCase());
                task.setPriority(priority);
                log.debug("Priority updated to {}", priority);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid priority '{}' on update, setting default MEDIUM", dto.getPriority());
                task.setPriority(PriorityTask.MEDIUM);
            }
        }

        Task updatedTask = taskRepository.save(task);
        log.info("Task with ID {} updated successfully", dto.getId());
        return updatedTask;
    }

    public ResponseEntity<?> deleteTask(long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        System.out.println("==========================================");
        log.info("User {} requested deletion of task ID {}", email, id);
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            log.warn("Task with ID {} not found for deletion", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        Task task = optionalTask.get();
        if (!task.getCreator().getEmail().equals(email)) {
            log.warn("User {} attempted to delete task ID {} without permission", email, id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to delete this task");
        }

        taskRepository.deleteById(id);
        log.info("Task with ID {} deleted by user {}", id, email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}