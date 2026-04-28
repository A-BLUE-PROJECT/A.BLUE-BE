# Milestone 1 — 기반 데이터 & 상태 정합성

> 모든 후속 작업의 전제 조건. M2~M4는 이 작업 완료 후 진행.

---

## 작업 목록

### [M1-1] LookbookStatus에 APPROVED / REJECTED 추가

**파일**: `lookbook/domain/model/enums/LookbookStatus.java`

현재: `PENDING, COMPLETED, FAILED`
변경: `PENDING, COMPLETED, FAILED, APPROVED, REJECTED` 추가

```java
APPROVED("갤러리 노출"),
REJECTED("노출 거부");
```

---

### [M1-2] Lookbook 엔티티에 aiScore 필드 + approve/reject 메서드 추가

**파일**: `lookbook/domain/model/Lookbook.java`

**추가할 필드**:
```java
@Column(name = "ai_score")
private Integer aiScore;
```

**추가할 팩토리/비즈니스 메서드**:
```java
// complete() 수정 — aiScore 파라미터 추가
public void complete(String originUrl, String imageUrl, Integer aiScore) {
    validateStatusIsPending();
    this.lookbookImage = LookbookImage.create(this, originUrl, imageUrl);
    this.status = LookbookStatus.COMPLETED;
    this.aiScore = aiScore;
}

// approve/reject — ImageInspection 승인 후 Lookbook 상태도 동기화
public void approve() {
    if (this.status != LookbookStatus.COMPLETED) {
        throw new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_STATUS_NOT_COMPLETED);
    }
    this.status = LookbookStatus.APPROVED;
}

public void reject() {
    if (this.status != LookbookStatus.COMPLETED) {
        throw new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_STATUS_NOT_COMPLETED);
    }
    this.status = LookbookStatus.REJECTED;
}
```

**관련 ErrorCode 추가** (`LookbookErrorCode.java`):
```java
LOOKBOOK_STATUS_NOT_COMPLETED("ELB40003", "룩북이 완료 상태가 아닙니다.", HttpStatus.BAD_REQUEST),
```

---

### [M1-3] LookbookCompleteCommand에 aiScore 추가

**파일**: `lookbook/application/dto/command/LookbookCompleteCommand.java`

```java
public record LookbookCompleteCommand(
        Long lookbookId,
        String originUrl,
        String imageUrl,
        Integer aiScore) {}   // 추가
```

---

### [M1-4] LookbookCompleteRequest에 aiScore 추가

**파일**: `lookbook/presentation/request/LookbookCompleteRequest.java`

```java
public record LookbookCompleteRequest(
        @NotBlank String originUrl,
        @NotBlank String imageUrl,
        Integer aiScore) {     // 추가 (nullable — n8n이 못 보낼 경우 대비)

    public LookbookCompleteCommand toCommand(Long lookbookId) {
        return new LookbookCompleteCommand(lookbookId, originUrl, imageUrl, aiScore);
    }
}
```

---

### [M1-5] LookbookCommandService.complete() 수정

**파일**: `lookbook/application/LookbookCommandService.java`

`complete()` 내부에서 `lookbook.complete(command.originUrl(), command.imageUrl(), command.aiScore())` 로 수정.

complete() 후 ImageInspection 자동 생성 로직 추가:
```java
// complete() 안에서 추가
ImageInspection inspection = ImageInspection.create(
        lookbook.getLookbookImage().getId(),
        command.imageUrl());
imageInspectionRepository.save(inspection);
```

> ⚠️ `LookbookCommandService`에 `ImageInspectionRepository` 의존성 주입 필요.
> 현재 ImageInspection 생성 위치가 명확하지 않으면, `InternalInspectionController` 콜백에서 생성 중인지 확인 필요.

---

### [M1-6] approve/reject 시 Lookbook 상태 동기화

**파일**: `admin/application/AdminInspectionCommandService.java`

`approveByLookbookId()` / `rejectByLookbookId()` 에서 LookbookRepository도 함께 업데이트:

```java
// 의존성 추가
private final LookbookRepository lookbookRepository;

@Transactional
public void approveByLookbookId(Long lookbookId, Long adminId) {
    ImageInspection inspection = imageInspectionRepository.getByLookbookId(lookbookId);
    inspection.updateAdminStatus(InspectionStatus.ADMIN_APPROVED, adminId);

    Lookbook lookbook = lookbookRepository.findById(lookbookId)
            .orElseThrow(() -> new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_NOT_FOUND));
    lookbook.approve();
}

@Transactional
public void rejectByLookbookId(Long lookbookId, Long adminId) {
    ImageInspection inspection = imageInspectionRepository.getByLookbookId(lookbookId);
    inspection.updateAdminStatus(InspectionStatus.ADMIN_REJECTED, adminId);

    Lookbook lookbook = lookbookRepository.findById(lookbookId)
            .orElseThrow(() -> new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_NOT_FOUND));
    lookbook.reject();
}
```

---

### [M1-7] 더미 Product 시드 데이터

**방법**: `DataInitializer.java` (ApplicationRunner 구현체) 신규 생성
- profile: `local`, `dev` 환경에서만 실행 (`@Profile({"local","dev"})`)
- 카테고리별 더미 상품 총 12개

| 카테고리 | 수량 |
|---|---|
| TOP | 3개 |
| BOTTOM | 3개 |
| OUTER | 2개 |
| SHOES | 2개 |
| ACC | 2개 |

**각 상품 필수 필드**: `brandName`, `productName`, `price`, `productImageUrl`, `originUrl`, `mappedCategory`, `stockStatus=IN_STOCK`

**파일 위치**: `common/config/DataInitializer.java`

---

## 완료 기준 체크리스트

- [x] `LookbookStatus.APPROVED / REJECTED` 컴파일 정상
- [x] `POST /i/v1/lookbooks/{id}/complete` 호출 시 aiScore 저장됨
- [x] `PATCH /adm/v1/lookbooks/{id}/approve` 호출 시 `Lookbook.status = APPROVED` + `ImageInspection.status = ADMIN_APPROVED` 동시 반영
- [x] 앱 기동 시 더미 Product 12개 DB에 삽입 확인 (DataInitializer — local/dev)
- [ ] 기존 `complete()` / `fail()` 단위 테스트 수정 통과

> ✅ **M1 완료** — 2026-03-27 (`develop` 브랜치 push 완료)
