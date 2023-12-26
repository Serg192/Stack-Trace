package com.sstohnij.stacktraceqabackendv0.validation;

import com.sstohnij.stacktraceqabackendv0.entity.Category;
import com.sstohnij.stacktraceqabackendv0.repository.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PostCategorySetValidator implements ConstraintValidator<PostCategorySet, Set<Long>> {

    private final CategoryRepository categoryRepository;

    public PostCategorySetValidator(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @Override
    public boolean isValid(Set<Long> longs, ConstraintValidatorContext constraintValidatorContext) {
        Set<Category> postCategories = categoryRepository.findByIdIn(longs)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        return longs.size() == postCategories.size();
    }
}
