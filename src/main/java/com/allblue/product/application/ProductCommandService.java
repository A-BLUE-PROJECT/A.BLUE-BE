package com.allblue.product.application;

import com.allblue.product.application.dto.command.ProductBatchCreateCommand;
import com.allblue.product.domain.exception.ProductBusinessException;
import com.allblue.product.domain.exception.ProductErrorCode;
import com.allblue.product.domain.model.Product;
import com.allblue.product.domain.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCommandService {

    private final ProductRepository productRepository;

    public void batchCreate(ProductBatchCreateCommand command) {
        List<Product> products = command.products().stream()
                .filter(cmd -> !productRepository.existsByExternalProductId(cmd.externalProductId()))
                .map(cmd -> Product.create(
                        cmd.sellerId(),
                        cmd.externalProductId(),
                        cmd.rawCategory(),
                        cmd.mappedCategory(),
                        cmd.brandName(),
                        cmd.productName(),
                        cmd.price(),
                        cmd.salePrice(),
                        cmd.productImageUrl(),
                        cmd.originUrl(),
                        cmd.stockStatus()))
                .toList();

        productRepository.saveAll(products);
    }

    public void updateHidden(Long productId, boolean hidden) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductBusinessException(ProductErrorCode.PRODUCT_NOT_FOUND));
        if (hidden) {
            product.hide();
        } else {
            product.reveal();
        }
    }
}
