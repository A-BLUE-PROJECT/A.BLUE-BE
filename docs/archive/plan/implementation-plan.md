# ALLBLUE 리팩토링 상세 실행 플랜

**v1.0 | 2026.03.23**
> 설계 기준: `phase0-lookbook-design.md`

---

## 사전 파악: 외부 의존성 현황

제거 대상 패키지(`card`, `deck`, `crawl`)를 참조하는 파일 목록.
패키지 제거 전 반드시 이 파일들을 함께 수정해야 컴파일 에러가 발생하지 않는다.

| 파일 | 참조 내용 | 처리 방법 |
| :--- | :--- | :--- |
| `admin/domain/model/ImageInspection.java` | `cardImageId` 필드 | `lookbookImageId`로 변경 |
| `admin/.../ImageInspectionJpaRepository.java` | `cardImageId` 쿼리 | `lookbookImageId`로 변경 |
| `admin/.../AdminInspectionCommandService.java` | `cardImageId` 사용 | `lookbookImageId`로 변경 |
| `admin/.../ImageInspectionResult.java` | `cardImageId` 사용 | `lookbookImageId`로 변경 |
| `admin/.../InspectionCallbackCommand.java` | `cardImageId` 사용 | `lookbookImageId`로 변경 |
| `category/application/CategoryCommandService.java` | `CardCategoryRepository` import | import 제거, 관련 로직 삭제 |
| `activelog/domain/model/ActiveLog.java` | `cardId` 필드 | `lookbookId`로 변경 |
| `activelog/.../ActiveLogController.java` | `cardId` 사용 | `lookbookId`로 변경 |

---

## Step 1. 패키지 제거

### 1-1. deck 패키지 제거 (의존성 없음 — 가장 먼저)
```
삭제: src/main/java/com/allblue/deck/ (전체)
```

### 1-2. crawl 패키지 제거 (의존성 없음)
```
삭제: src/main/java/com/allblue/crawl/ (전체)
```

### 1-3. category 의존성 정리 (card 패키지 제거 전 선행 필수)
```
수정: category/application/CategoryCommandService.java
  - CardCategoryRepository import 제거
  - deleteParentCategory(), deleteChildCategory() 내 cardCategoryRepository 관련 로직 제거
```

### 1-4. card 패키지 제거
```
삭제: src/main/java/com/allblue/card/ (전체)
```

---

## Step 2. product 패키지 신설

```
com.allblue.product
├── domain
│   ├── model
│   │   ├── Product.java              -- 엔티티
│   │   └── enums
│   │       ├── MappedCategory.java   -- TOP | BOTTOM | SHOES | OUTER | ACC | HEADWEAR | BAG
│   │       └── StockStatus.java      -- IN_STOCK | OUT_OF_STOCK
│   ├── repository
│   │   └── ProductRepository.java    -- 인터페이스
│   └── exception
│       ├── ProductBusinessException.java
│       └── ProductErrorCode.java
├── infrastructure
│   ├── jpa
│   │   └── ProductJpaRepository.java
│   └── ProductRepositoryImpl.java
└── presentation
    ├── controller
    │   ├── InternalProductApi.java          -- Swagger 인터페이스
    │   └── InternalProductController.java   -- /i/v1/products
    ├── request
    │   └── ProductBatchCreateRequest.java
    └── response
        └── ProductResultCode.java
```

**Product 엔티티 핵심 설계:**
- `seller_id` nullable (Phase 3 전까지)
- `raw_category` / `mapped_category` 이중 저장
- `@SQLDelete` + `@Filter` Soft Delete 적용
- `create()` 팩토리 메서드만 허용

---

## Step 3. lookbook 패키지 신설

```
com.allblue.lookbook
├── domain
│   ├── model
│   │   ├── Lookbook.java
│   │   ├── LookbookItem.java
│   │   ├── LookbookImage.java
│   │   └── enums
│   │       ├── StyleType.java        -- CASUAL | FORMAL | STREET | SPORTY
│   │       ├── Season.java           -- SPRING | SUMMER | FALL | WINTER
│   │       ├── LookbookStatus.java   -- PENDING | COMPLETED | FAILED
│   │       ├── Position.java         -- TOP | BOTTOM | SHOES | OUTER | ACC | HEADWEAR | BAG
│   │       └── TargetGender.java     -- WOMEN | MEN | UNDEFINED
│   ├── repository
│   │   ├── LookbookRepository.java
│   │   ├── LookbookItemRepository.java
│   │   └── LookbookImageRepository.java
│   └── exception
│       ├── LookbookBusinessException.java
│       └── LookbookErrorCode.java
├── application
│   ├── dto
│   │   ├── command
│   │   │   ├── LookbookCreateCommand.java
│   │   │   └── LookbookCompleteCommand.java  -- AI 콜백 수신용
│   │   ├── query
│   │   │   └── LookbookSearchQuery.java
│   │   └── result
│   │       ├── LookbookResult.java
│   │       └── LookbookDetailResult.java
│   ├── LookbookCommandService.java
│   └── LookbookQueryService.java
├── infrastructure
│   ├── jpa
│   │   ├── LookbookJpaRepository.java
│   │   ├── LookbookItemJpaRepository.java
│   │   └── LookbookImageJpaRepository.java
│   ├── LookbookRepositoryImpl.java
│   ├── LookbookItemRepositoryImpl.java
│   └── LookbookImageRepositoryImpl.java
└── presentation
    ├── controller
    │   ├── LookbookApi.java               -- Swagger 인터페이스
    │   ├── LookbookController.java         -- /w/v1/lookbooks
    │   ├── InternalLookbookApi.java
    │   ├── InternalLookbookController.java -- /i/v1/lookbooks
    │   ├── AdminLookbookApi.java
    │   └── AdminLookbookController.java    -- /adm/lookbooks
    ├── request
    │   └── LookbookCompleteRequest.java
    ├── response
    │   ├── LookbookResponse.java
    │   ├── LookbookDetailResponse.java
    │   └── LookbookResultCode.java
```

**Lookbook 엔티티 핵심 설계:**
- `LookbookItem`, `LookbookImage` CascadeType.ALL + orphanRemoval
- `complete(imageUrl)` — PENDING → COMPLETED 상태 전환 메서드
- `fail()` — PENDING → FAILED 상태 전환 메서드
- Soft Delete 시 LookbookItem도 연쇄 처리

---

## Step 4. user 패키지 확장 (FitProfile)

```
추가 파일:
├── user/domain/model/FitProfile.java
├── user/domain/repository/FitProfileRepository.java
├── user/infrastructure/jpa/FitProfileJpaRepository.java
├── user/infrastructure/FitProfileRepositoryImpl.java
├── user/application/dto/command/FitProfileCreateCommand.java
├── user/application/dto/command/FitProfileUpdateCommand.java
├── user/application/dto/result/FitProfileResult.java
├── user/application/FitProfileCommandService.java
├── user/application/FitProfileQueryService.java
└── user/presentation/
    ├── controller/FitProfileApi.java
    ├── controller/FitProfileController.java   -- /w/v1/fit-profile
    ├── request/FitProfileCreateRequest.java
    ├── request/FitProfileUpdateRequest.java
    ├── response/FitProfileResponse.java
    └── response/FitProfileResultCode.java
```

---

## Step 5. 기존 패키지 수정

### activelog 수정
| 파일 | 변경 내용 |
| :--- | :--- |
| `ActiveLog.java` | `cardId` → `lookbookId` |
| `ActiveLog.create()` | 파라미터명 변경 |
| `SwipeCommand.java` | `cardId` → `lookbookId` |
| `ActiveLogJpaRepository.java` | 인덱스/쿼리 `card_id` → `lookbook_id` |

### admin 수정 (ImageInspection — card → lookbookImage 참조)
| 파일 | 변경 내용 |
| :--- | :--- |
| `ImageInspection.java` | `cardImageId` → `lookbookImageId` |
| `ImageInspectionJpaRepository.java` | 쿼리 변경 |
| `AdminInspectionCommandService.java` | 필드명 변경 |
| `ImageInspectionResult.java` | 필드명 변경 |
| `InspectionCallbackCommand.java` | 필드명 변경 |

---

## Step 6. 컴파일 검증

```bash
./gradlew compileJava
```
에러 없이 통과하면 Step 7 진행.

---

## Step 7. 서비스 로직 구현

| 서비스 | 주요 메서드 |
| :--- | :--- |
| `LookbookCommandService` | `create()`, `complete()`, `fail()`, `delete()` |
| `LookbookQueryService` | `getLookbooks()` (커서 기반), `getLookbook()` |
| `FitProfileCommandService` | `create()`, `update()` |
| `FitProfileQueryService` | `getMyFitProfile()` |

---

## Step 8. Controller / API 구현

| Controller | Path | 메서드 |
| :--- | :--- | :--- |
| `LookbookController` | `/w/v1/lookbooks` | GET (목록), GET /{id} |
| `InternalLookbookController` | `/i/v1/lookbooks` | POST /{id}/complete, POST /{id}/fail |
| `AdminLookbookController` | `/adm/lookbooks` | GET (목록), DELETE /{id} |
| `FitProfileController` | `/w/v1/fit-profile` | POST, GET, PUT |

---

## 진행 체크리스트

```
[ ] Step 1-1. deck 패키지 제거
[ ] Step 1-2. crawl 패키지 제거
[ ] Step 1-3. category CardCategoryRepository 의존성 제거
[ ] Step 1-4. card 패키지 제거
[ ] Step 2.   product 패키지 신설 (Entity, Repository, ErrorCode)
[ ] Step 3.   lookbook 패키지 신설 (Entity, Repository, ErrorCode)
[ ] Step 4.   FitProfile 추가 (user 패키지 확장)
[ ] Step 5-1. activelog card_id → lookbook_id 수정
[ ] Step 5-2. admin ImageInspection cardImageId → lookbookImageId 수정
[ ] Step 6.   ./gradlew compileJava 통과 확인
[ ] Step 7.   Service 로직 구현
[ ] Step 8.   Controller / API 구현
```
