package com.allblue.category.infrastructure;

import com.allblue.category.domain.model.Category;
import com.allblue.category.domain.repository.CategoryRepository;
import com.allblue.category.infrastructure.jpa.CategoryJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    private static final int PARENT_DEPTH = 1;

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Category save(Category category) {
        return categoryJpaRepository.save(category);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryJpaRepository.findById(id);
    }

    @Override
    public List<Category> findAllParentsWithChildren() {
        return categoryJpaRepository.findAllParentsWithChildren();
    }

    @Override
    public void delete(Category category) {
        categoryJpaRepository.delete(category);
    }

    @Override
    public void softDeleteAllByParentId(Long parentId) {
        categoryJpaRepository.softDeleteAllByParentId(parentId);
    }

    @Override
    public long countChildCategoryByIdIn(List<Long> ids) {
        return categoryJpaRepository.countByIdInAndDepth(ids, PARENT_DEPTH);
    }

    @Override
    public List<Category> findAllByIdInWithParent(List<Long> ids) {
        return categoryJpaRepository.findAllByIdInWithParent(ids);
    }
}
