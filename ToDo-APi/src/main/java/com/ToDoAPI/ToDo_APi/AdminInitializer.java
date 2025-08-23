package com.ToDoAPI.ToDo_APi;

import com.ToDoAPI.ToDo_APi.domain.User;
import com.ToDoAPI.ToDo_APi.repository.UserRespository;
import com.ToDoAPI.ToDo_APi.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRespository userRepository;

    @Override
    public void run(String... args) throws Exception {
        Optional<User> existingAdmin = userRepository.findByEmail("admin@ad");
        if (existingAdmin.isEmpty()) {
            createAdminUser();
        } else {
            System.out.println("Usuário admin já existe no sistema!");
        }
    }

    private void createAdminUser() {
        try {
            User admin = new User();
            admin.setName("admin");
            admin.setEmail("admin@ad");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.ADMIN);

            // Salva o admin no banco de dados
            userRepository.save(admin);

            System.out.println("Admin inicial criado com sucesso!");
            System.out.println("Usuário: admin");
            System.out.println("Senha: admin");
            System.out.println("email: admin@ad");

        } catch (Exception e) {
            System.out.println("Erro ao criar admin: " + e.getMessage());

        }
    }
}