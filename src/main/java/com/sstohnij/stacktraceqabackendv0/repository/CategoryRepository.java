package com.sstohnij.stacktraceqabackendv0.repository;

import com.sstohnij.stacktraceqabackendv0.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Set<Category> findByIdIn(Set<Long> ids);

    Optional<Category> findCategoryById(Long id);
    Optional<Category> findCategoryByTitle(String title);

}
