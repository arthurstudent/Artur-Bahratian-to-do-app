package com.testtask.taskservice.dao;

import com.testtask.taskservice.model.dto.TaskFilterDto;
import com.testtask.taskservice.model.entity.TaskEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {

    public static Specification<TaskEntity> filterBy(TaskFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getUserId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("userId"), filter.getUserId()));
            }

            if (filter.getTitle() != null && !filter.getTitle().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + filter.getTitle().toLowerCase() + "%"
                ));
            }

            if (filter.getTaskStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("taskStatus"), filter.getTaskStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
