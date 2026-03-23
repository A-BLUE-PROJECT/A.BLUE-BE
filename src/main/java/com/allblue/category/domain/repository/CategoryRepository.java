package com.allblue.category.domain.repository;

import com.allblue.category.domain.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);

    Optional<Category> findById(Long id);

    List<Category> findAllParentsWithChildren();

    void delete(Category category);

    void softDeleteAllByParentId(Long parentId);

    long countChildCategoryByIdIn(List<Long> ids);

    List<Category> findAllByIdInWithParent(List<Long> ids);
}
