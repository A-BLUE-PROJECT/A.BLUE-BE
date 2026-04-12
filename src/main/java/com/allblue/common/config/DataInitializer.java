package com.allblue.common.config;

import com.allblue.admin.domain.model.Admin;
import com.allblue.admin.domain.model.AdminRole;
import com.allblue.admin.domain.repository.AdminRepository;
import com.allblue.product.domain.model.Product;
import com.allblue.product.domain.model.enums.MappedCategory;
import com.allblue.product.domain.model.enums.StockStatus;
import com.allblue.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Profile({"local", "dev"})
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.image.base-url}")
    private String imageBaseUrl;

    private static final String DUMMY_EXTERNAL_ID_PREFIX = "DUMMY_";

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        initAdmin();

        if (productRepository.existsByExternalProductId(DUMMY_EXTERNAL_ID_PREFIX + "TOP_M_001")) {
            log.info("[DataInitializer] 더미 상품 데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
            return;
        }

        log.info("[DataInitializer] 더미 상품 데이터 초기화 시작");
        insertDummyProducts();
        log.info("[DataInitializer] 더미 상품 데이터 초기화 완료");
    }

    private void initAdmin() {
        String adminEmail = "admin@allblue.com";
        if (adminRepository.existsByEmail(adminEmail)) {
            log.info("[DataInitializer] 어드민 계정이 이미 존재합니다.");
            return;
        }
        Admin admin = Admin.create(adminEmail, passwordEncoder.encode("Test1234!"), AdminRole.SUPER_ADMIN);
        adminRepository.save(admin);
        log.info("[DataInitializer] 어드민 계정 생성 완료: {}", adminEmail);
    }

    private void insertDummyProducts() {
        // 남성 상의 (TOP) — 남성상의 폴더
        save("DUMMY_TOP_M_001", MappedCategory.TOP, "무신사 스탠다드", "오버핏 코튼 반팔 티셔츠", 29000, 25000,
                img("남성상의", "176a9750ef745689202dd802b81d9647.jpg"));
        save("DUMMY_TOP_M_002", MappedCategory.TOP, "커버낫", "스트라이프 린넨 셔츠", 59000, 59000,
                img("남성상의", "1f239497a17ebc8ddcaf2cb7a81e39a9.jpg"));
        save("DUMMY_TOP_M_003", MappedCategory.TOP, "아더에러", "그래픽 롱슬리브 티셔츠", 89000, 89000,
                img("남성상의", "3750154_17020176371345_big.webp"));
        save("DUMMY_TOP_M_004", MappedCategory.TOP, "디스이즈네버댓", "나이론 트랙 재킷", 79000, 75000,
                img("남성상의", "4443308_17277795027763_big.webp"));
        save("DUMMY_TOP_M_005", MappedCategory.TOP, "무신사 스탠다드", "코튼 맨투맨 스웨트셔츠", 49000, 42000,
                img("남성상의", "54d2d2961af98f5c5aa81015ba836b21.jpg"));

        // 여성 상의 (TOP) — 여성상의 폴더
        save("DUMMY_TOP_F_001", MappedCategory.TOP, "무신사 스탠다드", "린넨 루즈핏 블라우스", 39000, 35000,
                img("여성상의", "5922964_17726093563341_big.webp"));
        save("DUMMY_TOP_F_002", MappedCategory.TOP, "폴로 랄프로렌", "스트라이프 피케 티셔츠", 89000, 89000,
                img("여성상의", "5a223d5f8b3694903eccd81d2141e7c2.jpg"));
        save("DUMMY_TOP_F_003", MappedCategory.TOP, "커버낫", "크롭 반팔 티셔츠", 45000, 45000,
                img("여성상의", "6056974_17731464449588_big.webp"));
        save("DUMMY_TOP_F_004", MappedCategory.TOP, "앤더슨벨", "오프숄더 슬리브리스 탑", 79000, 69000,
                img("여성상의", "6065168_17730409592422_big.webp"));

        save("DUMMY_TOP_M_006", MappedCategory.TOP, "무신사 스탠다드", "베이직 크루넥 니트", 55000, 49000,
                img("남성상의", "7f770cd225c0f7f1132dd00be6fc2709.jpg"));
        save("DUMMY_TOP_M_007", MappedCategory.TOP, "커버낫", "체크 플란넬 셔츠", 69000, 65000,
                img("남성상의", "82db489860ca12cfb241021248142fd9.jpg"));
        save("DUMMY_TOP_M_008", MappedCategory.TOP, "아더에러", "오버사이즈 코치 재킷", 119000, 119000,
                img("남성상의", "c17352a1-ddac-4ecb-8307-762a4b064204_fixing_v2.png"));
        save("DUMMY_TOP_M_009", MappedCategory.TOP, "디스이즈네버댓", "헤비웨이트 후드 스웨트셔츠", 99000, 89000,
                img("남성상의", "f62f7a4178f43a1fcf3a4128151413c5.jpg"));

        // 남성 하의 (BOTTOM) — 남성하의 폴더
        save("DUMMY_BTM_M_001", MappedCategory.BOTTOM, "무신사 스탠다드", "슬림 테이퍼드 데님 팬츠", 49000, 42000,
                img("남성하의", "5283166_17682662012441_big.webp"));
        save("DUMMY_BTM_M_002", MappedCategory.BOTTOM, "커버낫", "와이드 치노 팬츠", 69000, 69000,
                img("남성하의", "detail_3187939_17198083322655_big.webp"));
        save("DUMMY_BTM_M_003", MappedCategory.BOTTOM, "디스이즈네버댓", "나일론 트랙 팬츠", 79000, 79000,
                img("남성하의", "detail_5863716_17694041028697_big.webp"));
        save("DUMMY_BTM_M_004", MappedCategory.BOTTOM, "무신사 스탠다드", "릴렉스드 조거 팬츠", 55000, 49000,
                img("남성하의", "detail_3187939_17198083368326_big.webp"));
        save("DUMMY_BTM_M_005", MappedCategory.BOTTOM, "커버낫", "와이드 슬랙스", 75000, 69000,
                img("남성하의", "detail_5956403_17715452374653_big.webp"));
        save("DUMMY_BTM_M_006", MappedCategory.BOTTOM, "아더에러", "데님 와이드 팬츠", 89000, 89000,
                img("남성하의", "detail_6001388_17715447502370_big.webp"));

        // 여성 상의 추가 — 여성상의 폴더
        save("DUMMY_TOP_F_005", MappedCategory.TOP, "무신사 스탠다드", "리브드 슬리브리스 탑", 35000, 29000,
                img("여성상의", "6157656_17738868741464_big.webp"));
        save("DUMMY_TOP_F_006", MappedCategory.TOP, "커버낫", "오버핏 스트라이프 셔츠", 65000, 59000,
                img("여성상의", "6172932_17743709644809_big.webp"));
        save("DUMMY_TOP_F_007", MappedCategory.TOP, "폴로 랄프로렌", "코튼 피케 카라 티셔츠", 95000, 95000,
                img("여성상의", "PSS26-TS-05-01_8.jpg"));

        // 여성 하의 (BOTTOM) — 여성하의 폴더
        save("DUMMY_BTM_F_001", MappedCategory.BOTTOM, "무신사 스탠다드", "와이드 데님 팬츠", 55000, 49000,
                img("여성하의", "1762328833000-Ee6Tpn.jpg"));
        save("DUMMY_BTM_F_002", MappedCategory.BOTTOM, "커버낫", "플리츠 미니 스커트", 59000, 59000,
                img("여성하의", "shopping.webp"));
        save("DUMMY_BTM_F_003", MappedCategory.BOTTOM, "앤더슨벨", "새틴 미디 스커트", 79000, 69000,
                img("여성하의", "shopping%20(1).webp"));
        save("DUMMY_BTM_F_004", MappedCategory.BOTTOM, "무신사 스탠다드", "슬림 스트레이트 데님 팬츠", 49000, 43000,
                img("여성하의", "shopping%20(2).webp"));
    }

    private String img(String folder, String filename) {
        return imageBaseUrl + "/products/" + folder + "/" + filename;
    }

    private void save(String externalId, MappedCategory category, String brand, String name,
                      int price, int salePrice, String imageUrl) {
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
                "#",
                StockStatus.IN_STOCK);
        productRepository.save(product);
    }
}
