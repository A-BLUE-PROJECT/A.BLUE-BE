package com.allblue.product.domain.repository;

import com.allblue.product.domain.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    List<Product> saveAll(List<Product> products);
    Optional<Product> findById(Long id);
    List<Product> findAllByIds(List<Long> ids);
    Optional<Product> findByExternalProductId(String externalProductId);
    boolean existsByExternalProductId(String externalProductId);
}
