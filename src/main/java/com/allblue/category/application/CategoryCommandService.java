package com.allblue.category.application;

import com.allblue.category.application.dto.command.CreateCategoryCommand;
import com.allblue.category.application.dto.command.UpdateCategoryNameCommand;
import com.allblue.category.domain.exception.CategoryBusinessException;
import com.allblue.category.domain.exception.CategoryErrorCode;
import com.allblue.category.domain.model.Category;
import com.allblue.category.domain.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryCommandService {

    private final CategoryRepository categoryRepository;

    public Long createParentCategory(CreateCategoryCommand command) {
        Category parent = Category.createParent(command.name());
        Category savedCategory = categoryRepository.save(parent);
        return savedCategory.getId();
    }

    public Long createChildCategory(Long parentId, CreateCategoryCommand command) {
        Category parent = categoryRepository
                .findById(parentId)
                .orElseThrow(() -> new CategoryBusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        Category child = Category.createChild(parent, command.name());
        Category savedCategory = categoryRepository.save(child);
        return savedCategory.getId();
    }

    public void updateCategoryName(Long categoryId, UpdateCategoryNameCommand command) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new CategoryBusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        category.updateName(command.name());
    }

    public void deleteParentCategory(Long categoryId) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new CategoryBusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        List<Long> childIds =
                category.getChildren().stream().map(Category::getId).toList();

        if (!childIds.isEmpty()) {
            categoryRepository.softDeleteAllByParentId(categoryId);
        }

        categoryRepository.delete(category);
    }

    public void deleteChildCategory(Long categoryId) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new CategoryBusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        categoryRepository.delete(category);
    }
}
