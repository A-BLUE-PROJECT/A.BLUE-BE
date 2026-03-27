# Milestone 4 — 어드민 상품 관리

> M1~M3 이후 진행. 갤러리/생성 플로우가 먼저 동작해야 실용성 있음.
> Phase 3 카페24 연동 전까지는 더미 데이터 + 수동 관리 수준으로 MVP 구성.

---

## 작업 목록

### [M4-1] Product 엔티티에 displayStatus 추가

현재 `StockStatus` (재고 상태: IN_STOCK / OUT_OF_STOCK / DISCONTINUED) 만 존재.
어드민 숨김 처리는 별도 `hidden` boolean 컬럼으로 분리.

**파일**: `product/domain/model/Product.java`

추가 필드:
```java
@Column(name = "hidden", nullable = false)
private boolean hidden = false;
```

비즈니스 메서드 추가:
```java
public void hide() {
    this.hidden = true;
}

public void reveal() {
    this.hidden = false;
}
```

---

### [M4-2] ProductRepository에 어드민용 조회 추가

**파일**: `product/domain/repository/ProductRepository.java`

```java
// 전체 목록 (카테고리 필터, hidden 포함 조회)
List<Product> findAllForAdmin(MappedCategory category);
```

---

### [M4-3] ProductJpaRepository 쿼리 추가

**파일**: `product/infrastructure/jpa/ProductJpaRepository.java` (기존 파일 확인 후 추가)

```java
// category null이면 전체 조회
@Query("SELECT p FROM Product p WHERE (:category IS NULL OR p.mappedCategory = :category) ORDER BY p.id DESC")
List<Product> findAllForAdmin(@Param("category") MappedCategory category);
```

---

### [M4-4] ProductQueryService 추가 (또는 기존 파일 확인)

**파일**: `product/application/ProductQueryService.java`

```java
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
```

---

### [M4-5] ProductResult (신규)

**파일**: `product/application/dto/result/ProductResult.java`

```java
public record ProductResult(
        Long id,
        String brandName,
        String productName,
        Integer price,
        Integer salePrice,
        String productImageUrl,
        String originUrl,
        MappedCategory mappedCategory,
        StockStatus stockStatus,
        boolean hidden) {

    public static ProductResult from(Product product) {
        return new ProductResult(
                product.getId(),
                product.getBrandName(),
                product.getProductName(),
                product.getPrice(),
                product.getSalePrice(),
                product.getProductImageUrl(),
                product.getOriginUrl(),
                product.getMappedCategory(),
                product.getStockStatus(),
                product.isHidden());
    }
}
```

---

### [M4-6] ProductCommandService에 updateHidden() 추가

**파일**: `product/application/ProductCommandService.java` (기존 파일 확인)

```java
@Transactional
public void updateHidden(Long productId, boolean hidden) {
    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductBusinessException(ProductErrorCode.PRODUCT_NOT_FOUND));
    if (hidden) {
        product.hide();
    } else {
        product.reveal();
    }
}
```

---

### [M4-7] AdminProductController (신규)

**파일**: `product/presentation/controller/AdminProductController.java`

```java
@RestController
@RequestMapping("/adm/v1/products")
@RequiredArgsConstructor
public class AdminProductController implements AdminProductApi {

    private final ProductQueryService productQueryService;
    private final ProductCommandService productCommandService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findAll(
            @RequestParam(required = false) MappedCategory category) {
        List<ProductResponse> response = productQueryService.findAllForAdmin(category).stream()
                .map(ProductResponse::from)
                .toList();
        return ResponseEntity.ok(ApiResponse.of(ProductResultCode.PRODUCT_LIST_OK, response));
    }

    @PatchMapping("/{id}/hidden")
    public ResponseEntity<ApiResponse<Void>> updateHidden(
            @PathVariable Long id,
            @RequestBody ProductHiddenRequest request) {
        productCommandService.updateHidden(id, request.hidden());
        return ResponseEntity.ok(ApiResponse.from(ProductResultCode.PRODUCT_HIDDEN_UPDATED));
    }
}
```

---

### [M4-8] AdminProductApi 인터페이스 (신규)

**파일**: `product/presentation/controller/AdminProductApi.java`

```java
@Tag(name = "Admin Product API", description = "어드민 상품 관리 API")
public interface AdminProductApi {

    @Operation(summary = "상품 목록 조회", description = "카테고리별 상품 목록을 조회합니다.")
    ResponseEntity<ApiResponse<List<ProductResponse>>> findAll(MappedCategory category);

    @Operation(summary = "상품 숨김/공개 처리", description = "상품의 갤러리 노출 여부를 변경합니다.")
    ResponseEntity<ApiResponse<Void>> updateHidden(Long id, ProductHiddenRequest request);
}
```

---

### [M4-9] 신규 파일 목록

| 파일 | 위치 |
|---|---|
| `ProductResult.java` | `product/application/dto/result/` |
| `ProductResponse.java` | `product/presentation/response/` |
| `ProductHiddenRequest.java` | `product/presentation/request/` |
| `ProductResultCode.java` | `product/presentation/response/` |
| `AdminProductController.java` | `product/presentation/controller/` |
| `AdminProductApi.java` | `product/presentation/controller/` |

---

### [M4-10] ProductResultCode (신규)

**파일**: `product/presentation/response/ProductResultCode.java`

```java
@RequiredArgsConstructor
public enum ProductResultCode implements ResultCode {
    PRODUCT_LIST_OK("SPD20001", "상품 목록 조회가 완료되었습니다.", HttpStatus.OK),
    PRODUCT_HIDDEN_UPDATED("SPD20002", "상품 노출 상태가 변경되었습니다.", HttpStatus.OK),
    PRODUCT_NOT_FOUND("EPD40401", "상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;

    @Override public HttpStatus status() { return status; }
    @Override public String code() { return code; }
    @Override public String message() { return message; }
}
```

---

## 요청/응답 명세

### 상품 목록 조회
```
GET /adm/v1/products
GET /adm/v1/products?category=TOP

200 OK
{
  "code": "SPD20001",
  "data": [
    {
      "id": 1,
      "brandName": "무신사 스탠다드",
      "productName": "린넨 반팔 셔츠",
      "price": 39000,
      "salePrice": 35000,
      "productImageUrl": "https://...",
      "originUrl": "https://...",
      "mappedCategory": "TOP",
      "stockStatus": "IN_STOCK",
      "hidden": false
    }
  ]
}
```

### 상품 숨김 처리
```
PATCH /adm/v1/products/1/hidden

{ "hidden": true }

200 OK
{ "code": "SPD20002", "message": "상품 노출 상태가 변경되었습니다." }
```

---

## Phase 3 — 카페24 연동 시 추가 작업 (현재 제외)

```
POST /adm/v1/products/sync
  → Cafe24 API GET /api/v2/admin/products
  → Delta Sync (신규/변경만 필터)
  → 카테고리 정규화 → S3 이미지 업로드 → DB 저장
```

> 카페24 OAuth 토큰 (Seller 도메인) 환경 세팅 완료 후 구현.

---

## 완료 기준 체크리스트

- [ ] `GET /adm/v1/products` — 전체 상품 목록 반환
- [ ] `GET /adm/v1/products?category=TOP` — TOP 카테고리 필터 정상 동작
- [ ] `PATCH /adm/v1/products/{id}/hidden` — `hidden: true` → 숨김 처리
- [ ] 숨김 처리된 상품이 generate 시 productIds에 포함돼도 이미지 합성은 계속 진행 (AI 워커에 imageUrl 전달되므로)
- [ ] Swagger UI에서 Admin Product API 노출 확인
