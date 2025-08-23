package com.ToDoAPI.ToDo_APi.DTO;

import com.ToDoAPI.ToDo_APi.util.PriorityTask;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TaskFilterDTO {
    private Long id;
    private String title;
    private LocalDate dueData;
    private PriorityTask priority;

    @JsonProperty("completed")
    private Boolean status;
}
