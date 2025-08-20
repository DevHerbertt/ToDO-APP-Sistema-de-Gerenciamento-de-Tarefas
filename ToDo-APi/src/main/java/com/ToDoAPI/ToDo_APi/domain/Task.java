package com.ToDoAPI.ToDo_APi.domain;

import com.ToDoAPI.ToDo_APi.util.PriorityTask;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @Column(name = "due_data")
    private LocalDate dueData;

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private boolean status;

    @Enumerated(EnumType.STRING)
    private PriorityTask priority;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;
}