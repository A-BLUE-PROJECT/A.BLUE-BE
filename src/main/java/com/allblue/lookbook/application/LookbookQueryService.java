package com.allblue.lookbook.application;

import com.allblue.lookbook.application.dto.query.LookbookSearchQuery;
import com.allblue.lookbook.application.dto.result.LookbookDetailResult;
import com.allblue.lookbook.application.dto.result.LookbookResult;
import com.allblue.lookbook.domain.exception.LookbookBusinessException;
import com.allblue.lookbook.domain.exception.LookbookErrorCode;
import com.allblue.lookbook.domain.model.Lookbook;
import com.allblue.lookbook.domain.model.LookbookItem;
import com.allblue.lookbook.domain.repository.LookbookRepository;
import com.allblue.product.domain.model.Product;
import com.allblue.product.domain.repository.ProductRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LookbookQueryService {

    private final LookbookRepository lookbookRepository;
    private final ProductRepository productRepository;

    public List<LookbookResult> findAll(LookbookSearchQuery query) {
        return lookbookRepository.findApproved(query.cursorId(), query.size()).stream()
                .map(LookbookResult::from)
                .toList();
    }

    public LookbookDetailResult findById(Long lookbookId) {
        Lookbook lookbook = lookbookRepository.findById(lookbookId)
                .orElseThrow(() -> new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_NOT_FOUND));

        List<Long> productIds = lookbook.getLookbookItems().stream()
                .map(LookbookItem::getProductId)
                .toList();

        Map<Long, Product> productMap = productRepository.findAllByIds(productIds).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        return LookbookDetailResult.from(lookbook, productMap);
    }
}
