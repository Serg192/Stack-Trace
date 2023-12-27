package com.sstohnij.stacktraceqabackendv0.validation;

import com.sstohnij.stacktraceqabackendv0.entity.Category;
import com.sstohnij.stacktraceqabackendv0.repository.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class CategoryTitleValidator implements ConstraintValidator<CategoryTitle, String> {

    private final CategoryRepository categoryRepository;

    public CategoryTitleValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Category> category = categoryRepository.findCategoryByTitle(s);
        return category.isEmpty();
    }
}
