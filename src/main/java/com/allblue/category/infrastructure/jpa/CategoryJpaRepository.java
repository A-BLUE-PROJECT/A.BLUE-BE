package com.allblue.category.infrastructure.jpa;

import com.allblue.category.domain.model.Category;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    @EntityGraph(attributePaths = "children")
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL ORDER BY c.id")
    List<Category> findAllParentsWithChildren();

    @Modifying
    @Query(
            "UPDATE Category c SET c.deletedAt = CURRENT_TIMESTAMP WHERE c.parent.id = :parentId AND c.deletedAt IS NULL")
    void softDeleteAllByParentId(@Param("parentId") Long parentId);

    long countByIdInAndDepth(List<Long> ids, int depth);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.parent WHERE c.id IN :ids")
    List<Category> findAllByIdInWithParent(@Param("ids") List<Long> ids);
}
