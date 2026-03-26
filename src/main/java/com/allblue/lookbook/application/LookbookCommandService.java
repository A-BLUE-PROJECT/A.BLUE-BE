package com.allblue.lookbook.application;

import com.allblue.admin.domain.model.ImageInspection;
import com.allblue.admin.domain.repository.ImageInspectionRepository;
import com.allblue.lookbook.application.dto.command.LookbookCompleteCommand;
import com.allblue.lookbook.application.dto.command.LookbookCreateCommand;
import com.allblue.lookbook.application.dto.command.LookbookGenerateCommand;
import com.allblue.lookbook.application.port.out.AiWorkerClient;
import com.allblue.lookbook.application.port.out.AiWorkerPayload;
import com.allblue.lookbook.domain.exception.LookbookBusinessException;
import com.allblue.lookbook.domain.exception.LookbookErrorCode;
import com.allblue.lookbook.domain.model.Lookbook;
import com.allblue.lookbook.domain.repository.LookbookRepository;
import com.allblue.product.domain.model.Product;
import com.allblue.product.domain.repository.ProductRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LookbookCommandService {

    private final LookbookRepository lookbookRepository;
    private final ImageInspectionRepository imageInspectionRepository;
    private final ProductRepository productRepository;
    private final AiWorkerClient aiWorkerClient;

    public Long generate(LookbookGenerateCommand command) {
        Lookbook lookbook = Lookbook.create(
                command.styleType(),
                command.season(),
                command.targetGender(),
                command.tags(),
                command.items());
        Long lookbookId = lookbookRepository.save(lookbook).getId();

        List<Long> productIds = command.items().stream()
                .map(Lookbook.LookbookItemInfo::productId)
                .toList();
        Map<Long, Product> productMap = productRepository.findAllByIds(productIds).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<AiWorkerPayload.ProductInfo> productInfos = lookbook.getLookbookItems().stream()
                .map(item -> new AiWorkerPayload.ProductInfo(
                        item.getProductId(),
                        productMap.containsKey(item.getProductId())
                                ? productMap.get(item.getProductId()).getMappedCategory().name()
                                : null,
                        item.getPosition().name(),
                        productMap.containsKey(item.getProductId())
                                ? productMap.get(item.getProductId()).getProductImageUrl()
                                : null))
                .toList();

        AiWorkerPayload payload = new AiWorkerPayload(
                lookbookId,
                command.styleType().name(),
                command.season().name(),
                command.targetGender() != null ? command.targetGender().name() : null,
                command.prompt(),
                productInfos);

        try {
            aiWorkerClient.requestGeneration(payload);
        } catch (Exception e) {
            log.error("[AiWorker] n8n 트리거 실패 - lookbookId: {}", lookbookId, e);
        }

        return lookbookId;
    }

    public Long create(LookbookCreateCommand command) {
        Lookbook lookbook = Lookbook.create(
                command.styleType(),
                command.season(),
                command.targetGender(),
                command.tags(),
                command.items());
        return lookbookRepository.save(lookbook).getId();
    }

    public void complete(LookbookCompleteCommand command) {
        Lookbook lookbook = lookbookRepository.findById(command.lookbookId())
                .orElseThrow(() -> new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_NOT_FOUND));
        lookbook.complete(command.originUrl(), command.imageUrl(), command.aiScore());

        Lookbook saved = lookbookRepository.save(lookbook);
        Long lookbookImageId = saved.getLookbookImage().getId();
        ImageInspection inspection = ImageInspection.create(lookbookImageId, command.imageUrl());
        imageInspectionRepository.save(inspection);
    }

    public void fail(Long lookbookId) {
        Lookbook lookbook = lookbookRepository.findById(lookbookId)
                .orElseThrow(() -> new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_NOT_FOUND));
        lookbook.fail();
    }

    public void delete(Long lookbookId) {
        Lookbook lookbook = lookbookRepository.findById(lookbookId)
                .orElseThrow(() -> new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_NOT_FOUND));
        lookbookRepository.delete(lookbook);
    }
}
