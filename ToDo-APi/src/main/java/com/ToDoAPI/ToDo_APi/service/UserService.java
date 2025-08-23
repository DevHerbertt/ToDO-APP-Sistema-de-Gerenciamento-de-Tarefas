package com.ToDoAPI.ToDo_APi.service;

import com.ToDoAPI.ToDo_APi.DTO.UserDto;
import com.ToDoAPI.ToDo_APi.domain.User;
import com.ToDoAPI.ToDo_APi.repository.UserRespository;
import com.ToDoAPI.ToDo_APi.util.JwtUtil;
import com.ToDoAPI.ToDo_APi.util.Role;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Log4j2
@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private JwtUtil jwtUtil;

    public void registerAccount(UserDto userDto) {

        log.info("Starting the method registerAccount at {}", LocalDateTime.now());
        if (userRespository.existsByName(userDto.getName())) {
            throw new IllegalArgumentException("userName already exists");
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        log.debug("Name : {} \n Email : {} \n Role : {} Password : {} ",userDto.getName(),userDto.getEmail(),Role.USER,userDto.getPassword());

        log.info("User Registered");
        userRespository.save(user);
    }

    public String login(UserDto userDto){

        Optional<User> userOP = userRespository.findByEmail(userDto.getEmail());
        if (userOP.isEmpty()){
            throw new RuntimeException("UserName not found");
        }

        User user = userOP.get();
        if (!passwordEncoder.matches(userDto.getPassword(),user.getPassword())){
            throw new RuntimeException("invalid Password");
        }

        System.out.println("Generating token for: " + user.getEmail() + " with role: " + user.getRole());
        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }
}