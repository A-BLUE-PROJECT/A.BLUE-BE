package com.allblue.product.infrastructure;

import com.allblue.product.domain.model.Product;
import com.allblue.product.domain.model.enums.MappedCategory;
import com.allblue.product.domain.repository.ProductRepository;
import com.allblue.product.infrastructure.jpa.ProductJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public List<Product> saveAll(List<Product> products) {
        return productJpaRepository.saveAll(products);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id);
    }

    @Override
    public List<Product> findAllByIds(List<Long> ids) {
        return productJpaRepository.findAllByIdIn(ids);
    }

    @Override
    public Optional<Product> findByExternalProductId(String externalProductId) {
        return productJpaRepository.findByExternalProductId(externalProductId);
    }

    @Override
    public boolean existsByExternalProductId(String externalProductId) {
        return productJpaRepository.existsByExternalProductId(externalProductId);
    }

    @Override
    public List<Product> findAllForAdmin(MappedCategory category) {
        return productJpaRepository.findAllForAdmin(category);
    }
}
