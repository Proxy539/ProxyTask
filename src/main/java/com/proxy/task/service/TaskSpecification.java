package com.proxy.task.service;

import com.proxy.task.entity.Task;
import com.proxy.task.entity.TaskStatus;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskSpecification {

    public static Specification<Task> filterBy(String title, String description, TaskStatus status, LocalDate dueDate) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotEmpty(title)) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }

            if (StringUtils.isNotEmpty(description)) {
                predicates.add(criteriaBuilder.like(root.get("description"), "%" + description + "%"));
            }

            if (Objects.nonNull(status)) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (Objects.nonNull(dueDate)) {
                predicates.add(criteriaBuilder.equal(root.get("dueDate"), dueDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
