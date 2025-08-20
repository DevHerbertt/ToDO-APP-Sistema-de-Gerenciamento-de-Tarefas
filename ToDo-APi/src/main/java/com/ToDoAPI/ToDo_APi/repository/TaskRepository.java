package com.ToDoAPI.ToDo_APi.repository;

import com.ToDoAPI.ToDo_APi.domain.Task;
import com.ToDoAPI.ToDo_APi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long>, JpaSpecificationExecutor<Task> {


    List<Task> findByTitleContainingIgnoreCase(String title);

    List<Task> findByCreator(User creator);


    List<Task> findByCreatorEmail(String email);
}