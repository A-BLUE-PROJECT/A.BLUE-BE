package com.allblue.category.application;

import com.allblue.category.application.dto.result.CategoryListResult;
import com.allblue.category.domain.model.Category;
import com.allblue.category.domain.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryQueryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryListResult> getCategoryTree() {
        return categoryRepository.findAllParentsWithChildren().stream()
                .map(CategoryListResult::from)
                .toList();
    }

    public long countChildCategoryByIds(List<Long> categoryIds) {
        return categoryRepository.countChildCategoryByIdIn(categoryIds);
    }

    public List<CategoryListResult> getCategoriesByIds(List<Long> categoryIds) {
        if (categoryIds.isEmpty()) {
            return List.of();
        }
        return categoryRepository.findAllByIdInWithParent(categoryIds).stream()
                .filter(Category::isChild)
                .collect(java.util.stream.Collectors.groupingBy(Category::getParent))
                .entrySet()
                .stream()
                .map(entry -> CategoryListResult.fromParentWithFilteredChildren(entry.getKey(), entry.getValue()))
                .toList();
    }
}
