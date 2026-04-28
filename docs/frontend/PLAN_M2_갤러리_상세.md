# Milestone 2 — B2C 갤러리 & 룩북 상세

> M1 완료 후 진행. LookbookStatus.APPROVED 없으면 필터 동작 안 함.

---

## 작업 목록

### [M2-1] 갤러리 피드 — APPROVED 필터 + 커서 페이지네이션

#### LookbookJpaRepository에 쿼리 추가

**파일**: `lookbook/infrastructure/jpa/LookbookJpaRepository.java`

```java
// cursor 없을 때 (첫 페이지)
@Query("SELECT l FROM Lookbook l WHERE l.status = :status ORDER BY l.id DESC")
List<Lookbook> findByStatusOrderByIdDesc(
        @Param("status") LookbookStatus status,
        Pageable pageable);

// cursor 있을 때 (이후 페이지)
@Query("SELECT l FROM Lookbook l WHERE l.status = :status AND l.id < :cursorId ORDER BY l.id DESC")
List<Lookbook> findByStatusAndIdLessThanOrderByIdDesc(
        @Param("status") LookbookStatus status,
        @Param("cursorId") Long cursorId,
        Pageable pageable);
```

---

#### LookbookRepository 인터페이스에 메서드 추가

**파일**: `lookbook/domain/repository/LookbookRepository.java`

```java
List<Lookbook> findApproved(Long cursorId, int size);
```

---

#### LookbookRepositoryImpl 구현

**파일**: `lookbook/infrastructure/LookbookRepositoryImpl.java`

```java
@Override
public List<Lookbook> findApproved(Long cursorId, int size) {
    Pageable pageable = PageRequest.of(0, size);
    if (cursorId == null) {
        return lookbookJpaRepository.findByStatusOrderByIdDesc(LookbookStatus.APPROVED, pageable);
    }
    return lookbookJpaRepository.findByStatusAndIdLessThanOrderByIdDesc(
            LookbookStatus.APPROVED, cursorId, pageable);
}
```

---

#### LookbookQueryService.findAll() 수정

**파일**: `lookbook/application/LookbookQueryService.java`

```java
public List<LookbookResult> findAll(LookbookSearchQuery query) {
    return lookbookRepository.findApproved(query.cursorId(), query.size())
            .stream()
            .map(LookbookResult::from)
            .toList();
}
```

---

#### LookbookController.findAll() 수정

**파일**: `lookbook/presentation/controller/LookbookController.java`

```java
@GetMapping
public ResponseEntity<ApiResponse<List<LookbookResponse>>> findAll(
        @RequestParam(required = false) Long cursor,
        @RequestParam(defaultValue = "20") int size) {
    LookbookSearchQuery query = new LookbookSearchQuery(null, null, null, cursor, size);
    ...
}
```

> `LookbookApi` 인터페이스도 동일하게 파라미터 수정.

---

### [M2-2] LookbookResult / LookbookResponse에 aiScore 추가

**파일**: `lookbook/application/dto/result/LookbookResult.java`

```java
public record LookbookResult(
        Long id,
        StyleType styleType,
        Season season,
        TargetGender targetGender,
        String tags,
        LookbookStatus status,
        String imageUrl,
        Integer aiScore) {      // 추가

    public static LookbookResult from(Lookbook lookbook) {
        return new LookbookResult(
                lookbook.getId(),
                lookbook.getStyleType(),
                lookbook.getSeason(),
                lookbook.getTargetGender(),
                lookbook.getTags(),
                lookbook.getStatus(),
                lookbook.getLookbookImage() != null ? lookbook.getLookbookImage().getImageUrl() : null,
                lookbook.getAiScore());   // 추가
    }
}
```

**파일**: `lookbook/presentation/response/LookbookResponse.java`

```java
public record LookbookResponse(
        Long id,
        StyleType styleType,
        Season season,
        TargetGender targetGender,
        String tags,
        LookbookStatus status,
        String imageUrl,
        Integer aiScore) {     // 추가

    public static LookbookResponse from(LookbookResult result) {
        return new LookbookResponse(
                result.id(), result.styleType(), result.season(),
                result.targetGender(), result.tags(), result.status(),
                result.imageUrl(), result.aiScore());   // 추가
    }
}
```

---

### [M2-3] 룩북 상세 — 상품 정보 JOIN

#### ProductRepository에 조회 메서드 확인

**파일**: `product/domain/repository/ProductRepository.java`

`findAllByIds(List<Long> ids)` 이미 존재 확인. 없으면 추가.

---

#### LookbookDetailResult 수정 — 상품 상세 포함

**파일**: `lookbook/application/dto/result/LookbookDetailResult.java`

```java
public record LookbookDetailResult(
        Long id,
        StyleType styleType,
        Season season,
        TargetGender targetGender,
        String tags,
        LookbookStatus status,
        String originUrl,
        String imageUrl,
        Integer aiScore,                  // 추가
        List<LookbookItemResult> items) {

    public record LookbookItemResult(
            Long productId,
            Position position,
            String brandName,             // 추가
            String productName,           // 추가
            Integer price,                // 추가
            String productImageUrl,       // 추가
            String originUrl) {}          // 추가
}
```

---

#### LookbookQueryService.findById() 수정 — ProductRepository 주입

**파일**: `lookbook/application/LookbookQueryService.java`

```java
// 의존성 추가
private final ProductRepository productRepository;

public LookbookDetailResult findById(Long lookbookId) {
    Lookbook lookbook = lookbookRepository.findById(lookbookId)
            .orElseThrow(() -> new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_NOT_FOUND));

    // productId 목록 수집
    List<Long> productIds = lookbook.getLookbookItems().stream()
            .map(LookbookItem::getProductId)
            .toList();

    // Product 일괄 조회
    Map<Long, Product> productMap = productRepository.findAllByIds(productIds).stream()
            .collect(Collectors.toMap(Product::getId, p -> p));

    return LookbookDetailResult.from(lookbook, productMap);
}
```

`LookbookDetailResult.from()` 시그니처도 `productMap` 받도록 수정:
```java
public static LookbookDetailResult from(Lookbook lookbook, Map<Long, Product> productMap) {
    List<LookbookItemResult> items = lookbook.getLookbookItems().stream()
            .map(item -> {
                Product p = productMap.get(item.getProductId());
                return new LookbookItemResult(
                        item.getProductId(),
                        item.getPosition(),
                        p != null ? p.getBrandName() : null,
                        p != null ? p.getProductName() : null,
                        p != null ? p.getPrice() : null,
                        p != null ? p.getProductImageUrl() : null,
                        p != null ? p.getOriginUrl() : null);
            })
            .toList();
    ...
}
```

---

#### LookbookDetailResponse 수정

**파일**: `lookbook/presentation/response/LookbookDetailResponse.java`

`LookbookDetailResult` 필드 변경에 맞춰 `aiScore`, 상품 상세 필드 반영.

---

### [M2-4] SecurityConfig — 갤러리 API permitAll 확인

**파일**: `security/config/SecurityConfig.java`

`/w/v1/lookbooks`, `/w/v1/lookbooks/**` 가 비로그인 접근 가능한지 확인.
현재 `anyRequest().authenticated()` → 로그인 없이 갤러리 접근 불가.

프론트가 비로그인 갤러리 열람을 허용한다면:
```java
.requestMatchers("/w/v1/lookbooks", "/w/v1/lookbooks/**").permitAll()
```

> 프론트팀 확인 후 결정. 비로그인 허용 여부는 기획 사항.

---

## 최종 갤러리 응답 형태

```json
GET /w/v1/lookbooks?cursor=10&size=20

{
  "code": "SLB20001",
  "message": "룩북 목록 조회가 완료되었습니다.",
  "data": [
    {
      "id": 9,
      "styleType": "CASUAL",
      "season": "SPRING",
      "targetGender": "FEMALE",
      "tags": "미니멀,데일리",
      "status": "APPROVED",
      "imageUrl": "https://s3.../lookbook_9.webp",
      "aiScore": 87
    }
  ]
}
```

## 최종 상세 응답 형태

```json
GET /w/v1/lookbooks/9

{
  "code": "SLB20002",
  "data": {
    "id": 9,
    "styleType": "CASUAL",
    "season": "SPRING",
    "targetGender": "FEMALE",
    "tags": "미니멀,데일리",
    "status": "APPROVED",
    "imageUrl": "https://...",
    "originUrl": "https://...",
    "aiScore": 87,
    "items": [
      {
        "productId": 3,
        "position": "TOP",
        "brandName": "무신사 스탠다드",
        "productName": "린넨 반팔 셔츠",
        "price": 39000,
        "productImageUrl": "https://...",
        "originUrl": "https://..."
      }
    ]
  }
}
```

---

## 완료 기준 체크리스트

- [x] `GET /w/v1/lookbooks` — APPROVED 룩북만 반환
- [x] `GET /w/v1/lookbooks?cursor=5&size=10` — cursor 이후 10개 반환
- [x] `GET /w/v1/lookbooks/{id}` — items에 상품 상세 정보 포함
- [x] aiScore 갤러리 목록 + 상세 모두 포함
- [x] SecurityConfig `/w/v1/lookbooks`, `/w/v1/lookbooks/**` permitAll 추가

> ✅ **M2 완료** — 2026-03-27 (`develop` 브랜치 push 완료)
