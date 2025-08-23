package com.ToDoAPI.ToDo_APi.util;

import com.ToDoAPI.ToDo_APi.DTO.TaskFilterDTO;
import com.ToDoAPI.ToDo_APi.domain.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecificationUtil {
    public static Specification<Task> filter(TaskFilterDTO dto, String email) {
        return (root, query, cb) -> {
            Predicate predicate = cb.equal(root.get("creator").get("email"), email);


            if (dto.getStatus() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), dto.getStatus()));
            }

            if (dto.getTitle() != null)
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("title")), "%" + dto.getTitle().toLowerCase() + "%"));

            if (dto.getDueData() != null)
                predicate = cb.and(predicate, cb.equal(root.get("dueData"), dto.getDueData()));

            if (dto.getPriority() != null)
                predicate = cb.and(predicate, cb.equal(root.get("priority"), dto.getPriority()));

            return predicate;
        };
    }
}