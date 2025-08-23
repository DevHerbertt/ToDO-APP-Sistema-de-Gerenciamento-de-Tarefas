package com.ToDoAPI.ToDo_APi.service;

import com.ToDoAPI.ToDo_APi.DTO.TaskCreateDTO;
import com.ToDoAPI.ToDo_APi.domain.Task;
import com.ToDoAPI.ToDo_APi.domain.User;
import com.ToDoAPI.ToDo_APi.repository.TaskRepository;
import com.ToDoAPI.ToDo_APi.repository.UserRespository;
import com.ToDoAPI.ToDo_APi.util.Role;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
@Log4j2
@Service
public class AdminService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRespository userRepository;


    public List<Task> getAllTasksForAdmin() {
        log.debug("Starting method getAllTasksForAdmin at {}",LocalDateTime.now());
        return taskRepository.findAll();
    }

    public Task createTaskForUser(TaskCreateDTO dto, Long userId) {
        log.debug("Starting method createTaskForUser at {}",LocalDateTime.now());
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
        log.debug("Title : {} \n Description : {} \n DueData : {} \n Status : {} \n CreatedAt: {} \n UpdateAt: {} \n Creator : {}",
                dto.getTitle(),dto.getDescription(),dto.getDueData(),false,LocalDateTime.now(),LocalDateTime.now(),user);
        return taskRepository.save(task);
    }

    public void deleteAnyTask(Long id) {
        log.debug("Starting method deleteAnyTask at {}",LocalDateTime.now());
        log.debug("Id Task : {}",id);
        if (!taskRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        taskRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        log.debug("Starting method getAllUsers at {}",LocalDateTime.now());
        return userRepository.findAll();
    }

    public User changeUserRole(Long userId, String newRole) {
        log.debug("Starting method changeUserRole at {}",LocalDateTime.now());
        log.debug("ID: {}  ROLE:{}",userId,newRole);
        Role role;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
        log.debug("User Id identified");
        try {
            role = Role.valueOf(newRole.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not exists : " + newRole);
        }
        user.setRole(role);
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        log.debug("Starting method deleteUser at {}",LocalDateTime.now());
        log.debug("Id of User is  {}",userId);
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        }
        userRepository.deleteById(userId);
    }
}
