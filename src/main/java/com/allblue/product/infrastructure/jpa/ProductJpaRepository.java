package com.allblue.product.infrastructure.jpa;

import com.allblue.product.domain.model.Product;
import com.allblue.product.domain.model.enums.MappedCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByExternalProductId(String externalProductId);
    boolean existsByExternalProductId(String externalProductId);
    List<Product> findAllByIdIn(List<Long> ids);

    @Query("SELECT p FROM Product p WHERE (:category IS NULL OR p.mappedCategory = :category) ORDER BY p.id DESC")
    List<Product> findAllForAdmin(@Param("category") MappedCategory category);
}
