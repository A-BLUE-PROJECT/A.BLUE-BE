package com.allblue.product.application;

import com.allblue.product.application.dto.result.ProductResult;
import com.allblue.product.domain.model.enums.MappedCategory;
import com.allblue.product.domain.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductRepository productRepository;

    public List<ProductResult> findAllForAdmin(MappedCategory category) {
        return productRepository.findAllForAdmin(category).stream()
                .map(ProductResult::from)
                .toList();
    }
}
