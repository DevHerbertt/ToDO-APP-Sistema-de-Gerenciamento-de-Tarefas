package com.ToDoAPI.ToDo_APi.DTO;

import com.ToDoAPI.ToDo_APi.util.PriorityTask;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TaskEditDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueData;
    private String priority;
    private boolean status;
}
