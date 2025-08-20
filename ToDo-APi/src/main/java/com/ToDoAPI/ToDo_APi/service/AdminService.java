package com.ToDoAPI.ToDo_APi.service;

import com.ToDoAPI.ToDo_APi.DTO.TaskCreateDTO;
import com.ToDoAPI.ToDo_APi.domain.Task;
import com.ToDoAPI.ToDo_APi.domain.User;
import com.ToDoAPI.ToDo_APi.repository.TaskRepository;
import com.ToDoAPI.ToDo_APi.repository.UserRespository;
import com.ToDoAPI.ToDo_APi.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRespository userRepository;


    public List<Task> getAllTasksForAdmin() {
        // Removido a verificação de admin
        return taskRepository.findAll();
    }

    public Task createTaskForUser(TaskCreateDTO dto, Long userId) {
        // Removido a verificação de admin

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueData(dto.getDueData());
        task.setStatus(false);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdateAt(LocalDateTime.now());
        task.setCreator(user);

        return taskRepository.save(task);
    }

    public void deleteAnyTask(Long id) {
        // Removido a verificação de admin
        if (!taskRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        taskRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        // Removido a verificação de admin
        return userRepository.findAll();
    }

    public User changeUserRole(Long userId, String newRole) {
        // Removido a verificação de admin

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Role role;
        try {
            role = Role.valueOf(newRole.toUpperCase()); // Converte string para enum
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Papel inválido: " + newRole);
        }

        user.setRole(role);
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        // Removido a verificação de admin

        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        userRepository.deleteById(userId);
    }
}
