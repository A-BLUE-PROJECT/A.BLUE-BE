package com.allblue.product.infrastructure.jpa;

import com.allblue.product.domain.model.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByExternalProductId(String externalProductId);
    boolean existsByExternalProductId(String externalProductId);
    List<Product> findAllByIdIn(List<Long> ids);
}
