package com.allblue.common.config;

import com.allblue.product.domain.model.Product;
import com.allblue.product.domain.model.enums.MappedCategory;
import com.allblue.product.domain.model.enums.StockStatus;
import com.allblue.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Profile({"local", "dev"})
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final ProductRepository productRepository;

    private static final String DUMMY_EXTERNAL_ID_PREFIX = "DUMMY_";

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (productRepository.existsByExternalProductId(DUMMY_EXTERNAL_ID_PREFIX + "TOP_001")) {
            log.info("[DataInitializer] 더미 상품 데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
            return;
        }

        log.info("[DataInitializer] 더미 상품 데이터 초기화 시작");
        insertDummyProducts();
        log.info("[DataInitializer] 더미 상품 데이터 초기화 완료");
    }

    private void insertDummyProducts() {
        // TOP 3개
        save("DUMMY_TOP_001", MappedCategory.TOP, "무신사 스탠다드", "오버핏 코튼 반팔 티셔츠", 29000, 25000,
                "https://image.musinsa.com/images/dummy/top_001.jpg",
                "https://www.musinsa.com/products/dummy_top_001");

        save("DUMMY_TOP_002", MappedCategory.TOP, "커버낫", "스트라이프 린넨 셔츠", 59000, 59000,
                "https://image.musinsa.com/images/dummy/top_002.jpg",
                "https://www.musinsa.com/products/dummy_top_002");

        save("DUMMY_TOP_003", MappedCategory.TOP, "아더에러", "그래픽 롱슬리브 티셔츠", 89000, 89000,
                "https://image.musinsa.com/images/dummy/top_003.jpg",
                "https://www.musinsa.com/products/dummy_top_003");

        // BOTTOM 3개
        save("DUMMY_BTM_001", MappedCategory.BOTTOM, "무신사 스탠다드", "슬림 테이퍼드 데님 팬츠", 49000, 42000,
                "https://image.musinsa.com/images/dummy/bottom_001.jpg",
                "https://www.musinsa.com/products/dummy_btm_001");

        save("DUMMY_BTM_002", MappedCategory.BOTTOM, "커버낫", "와이드 치노 팬츠", 69000, 69000,
                "https://image.musinsa.com/images/dummy/bottom_002.jpg",
                "https://www.musinsa.com/products/dummy_btm_002");

        save("DUMMY_BTM_003", MappedCategory.BOTTOM, "디스이즈네버댓", "나일론 트랙 팬츠", 79000, 79000,
                "https://image.musinsa.com/images/dummy/bottom_003.jpg",
                "https://www.musinsa.com/products/dummy_btm_003");

        // OUTER 2개
        save("DUMMY_OTR_001", MappedCategory.OUTER, "앤더슨벨", "오버핏 코치 재킷", 159000, 139000,
                "https://image.musinsa.com/images/dummy/outer_001.jpg",
                "https://www.musinsa.com/products/dummy_otr_001");

        save("DUMMY_OTR_002", MappedCategory.OUTER, "커버낫", "싱글 버튼 블레이저", 129000, 129000,
                "https://image.musinsa.com/images/dummy/outer_002.jpg",
                "https://www.musinsa.com/products/dummy_otr_002");

        // SHOES 2개
        save("DUMMY_SHS_001", MappedCategory.SHOES, "뉴발란스", "993 클래식 스니커즈", 189000, 189000,
                "https://image.musinsa.com/images/dummy/shoes_001.jpg",
                "https://www.musinsa.com/products/dummy_shs_001");

        save("DUMMY_SHS_002", MappedCategory.SHOES, "컨버스", "척 70 하이 캔버스", 109000, 95000,
                "https://image.musinsa.com/images/dummy/shoes_002.jpg",
                "https://www.musinsa.com/products/dummy_shs_002");

        // ACC 2개
        save("DUMMY_ACC_001", MappedCategory.ACC, "캉골", "버킷햇", 49000, 49000,
                "https://image.musinsa.com/images/dummy/acc_001.jpg",
                "https://www.musinsa.com/products/dummy_acc_001");

        save("DUMMY_ACC_002", MappedCategory.ACC, "아크네 스튜디오", "울 머플러", 129000, 129000,
                "https://image.musinsa.com/images/dummy/acc_002.jpg",
                "https://www.musinsa.com/products/dummy_acc_002");
    }

    private void save(String externalId, MappedCategory category, String brand, String name,
                      int price, int salePrice, String imageUrl, String originUrl) {
        Product product = Product.create(
                null,
                externalId,
                category.name(),
                category,
                brand,
                name,
                price,
                salePrice,
                imageUrl,
                originUrl,
                StockStatus.IN_STOCK);
        productRepository.save(product);
    }
}
