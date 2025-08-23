package com.ToDoAPI.ToDo_APi.repository;

import com.ToDoAPI.ToDo_APi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRespository extends JpaRepository<User, Long> {
    boolean existsByName(String name);

    Optional<User> findByEmail(String email);




}
