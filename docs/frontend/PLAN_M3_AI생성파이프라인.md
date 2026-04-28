# Milestone 3 — AI 룩북 생성 파이프라인

> M1 완료 후 진행 (더미 Product 시드 필요). 가장 복잡한 구현.
> n8n Webhook URL 환경 변수 세팅이 선행되어야 함.

---

## 전체 플로우

```
POST /adm/v1/lookbooks/generate
  → [1] Lookbook 생성 (status: PENDING)
  → [2] productIds로 Product 조회 → AiWorkerPayload 구성
  → [3] AiWorkerClient → n8n Webhook HTTP POST (비동기 트리거)
  → 202 Accepted 즉시 응답

n8n
  → Gemini API 이미지 합성
  → (성공) POST /i/v1/lookbooks/{id}/complete { originUrl, imageUrl, aiScore }
      → Lookbook.complete() → LookbookImage 저장 → status: COMPLETED
      → ImageInspection 생성 (status: PENDING)
      → AI 결과 → ImageInspection.status: AI_PASSED or AI_FLAGGED
  → (실패) POST /i/v1/lookbooks/{id}/fail
      → Lookbook.status: FAILED
```

---

## 작업 목록

### [M3-1] LookbookGenerateRequest (신규)

**파일**: `lookbook/presentation/request/LookbookGenerateRequest.java`

```java
public record LookbookGenerateRequest(
        @NotNull StyleType styleType,
        @NotNull Season season,
        TargetGender targetGender,
        String tags,
        @NotEmpty List<LookbookItemRequest> items,
        String prompt) {

    public record LookbookItemRequest(
            @NotNull Long productId,
            @NotNull Position position) {}

    public LookbookGenerateCommand toCommand() {
        List<LookbookItemInfo> itemInfos = items.stream()
                .map(i -> new LookbookItemInfo(i.productId(), i.position()))
                .toList();
        return new LookbookGenerateCommand(styleType, season, targetGender, tags, itemInfos, prompt);
    }
}
```

---

### [M3-2] LookbookGenerateCommand (신규)

**파일**: `lookbook/application/dto/command/LookbookGenerateCommand.java`

```java
public record LookbookGenerateCommand(
        StyleType styleType,
        Season season,
        TargetGender targetGender,
        String tags,
        List<LookbookItemInfo> items,
        String prompt) {}
```

---

### [M3-3] LookbookCommandService.generate() 추가

**파일**: `lookbook/application/LookbookCommandService.java`

**의존성 추가**:
- `ProductRepository productRepository`
- `AiWorkerClient aiWorkerClient`

```java
@Transactional
public Long generate(LookbookGenerateCommand command) {
    // 1. Lookbook 생성
    Lookbook lookbook = Lookbook.create(
            command.styleType(),
            command.season(),
            command.targetGender(),
            command.tags(),
            command.items());
    Long lookbookId = lookbookRepository.save(lookbook).getId();

    // 2. Product 조회 → AiWorkerPayload 구성
    List<Long> productIds = command.items().stream()
            .map(LookbookItemInfo::productId)
            .toList();
    Map<Long, Product> productMap = productRepository.findAllByIds(productIds).stream()
            .collect(Collectors.toMap(Product::getId, p -> p));

    List<AiWorkerPayload.ProductInfo> productInfos = command.items().stream()
            .map(item -> {
                Product p = productMap.get(item.productId());
                return new AiWorkerPayload.ProductInfo(
                        item.productId(),
                        p != null ? p.getMappedCategory().name() : null,
                        item.position().name(),
                        p != null ? p.getProductImageUrl() : null);
            })
            .toList();

    AiWorkerPayload payload = new AiWorkerPayload(
            lookbookId,
            command.styleType().name(),
            command.season().name(),
            command.targetGender() != null ? command.targetGender().name() : null,
            productInfos);

    // 3. n8n 트리거 (실패 시 로그만 남기고 예외 전파 안 함)
    try {
        aiWorkerClient.requestGeneration(payload);
    } catch (Exception e) {
        log.error("AI 워커 트리거 실패 - lookbookId: {}", lookbookId, e);
        // 룩북은 PENDING 상태로 유지, 별도 재시도 정책 필요
    }

    return lookbookId;
}
```

---

### [M3-4] AiWorkerClientImpl 구현 (신규)

**파일**: `lookbook/infrastructure/client/AiWorkerClientImpl.java`

```java
@Component
@RequiredArgsConstructor
@Slf4j
public class AiWorkerClientImpl implements AiWorkerClient {

    private final RestClient restClient;

    @Value("${ai.worker.webhook-url}")
    private String webhookUrl;

    @Override
    public void requestGeneration(AiWorkerPayload payload) {
        restClient.post()
                .uri(webhookUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .toBodilessEntity();
        log.info("AI 워커 트리거 성공 - lookbookId: {}", payload.lookbookId());
    }
}
```

**RestClient Bean 등록** (`common/config/RestClientConfig.java` 신규):
```java
@Configuration
public class RestClientConfig {
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .build();
    }
}
```

---

### [M3-5] application.yml에 n8n URL 추가

**파일**: `src/main/resources/application.yml`

```yaml
ai:
  worker:
    webhook-url: ${AI_WORKER_WEBHOOK_URL:http://localhost:5678/webhook/lookbook-generate}
```

환경 변수 `AI_WORKER_WEBHOOK_URL` 로 실제 n8n URL 주입.

---

### [M3-6] AdminLookbookController에 generate 엔드포인트 추가

**파일**: `lookbook/presentation/controller/AdminLookbookController.java`

```java
@PostMapping("/generate")
public ResponseEntity<ApiResponse<Long>> generate(
        @Valid @RequestBody LookbookGenerateRequest request) {
    Long lookbookId = lookbookCommandService.generate(request.toCommand());
    return ResponseEntity
            .status(LookbookResultCode.LOOKBOOK_GENERATE_ACCEPTED.status())
            .body(ApiResponse.of(LookbookResultCode.LOOKBOOK_GENERATE_ACCEPTED, lookbookId));
}
```

---

### [M3-7] AdminLookbookApi 인터페이스에 generate 추가

**파일**: `lookbook/presentation/controller/AdminLookbookApi.java`

```java
@Operation(summary = "AI 룩북 생성", description = "룩북 생성을 AI 워커에 요청합니다. (비동기)")
ResponseEntity<ApiResponse<Long>> generate(@RequestBody LookbookGenerateRequest request);
```

---

### [M3-8] LookbookResultCode에 202 코드 추가

**파일**: `lookbook/presentation/response/LookbookResultCode.java`

```java
LOOKBOOK_GENERATE_ACCEPTED("SLB20201", "룩북 생성 요청이 접수되었습니다.", HttpStatus.ACCEPTED),
```

---

### [M3-9] complete 콜백에서 ImageInspection 자동 생성 확인

**파일**: `lookbook/application/LookbookCommandService.java`

`complete()` 내부에서 `LookbookImage` 저장 직후 `ImageInspection` 생성:

```java
public void complete(LookbookCompleteCommand command) {
    Lookbook lookbook = lookbookRepository.findById(command.lookbookId())
            .orElseThrow(...);
    lookbook.complete(command.originUrl(), command.imageUrl(), command.aiScore());

    // LookbookImage가 cascade로 저장된 이후 flush 필요
    lookbookRepository.save(lookbook);  // or entityManager.flush()

    Long lookbookImageId = lookbook.getLookbookImage().getId();
    ImageInspection inspection = ImageInspection.create(lookbookImageId, command.imageUrl());
    imageInspectionRepository.save(inspection);
}
```

> ⚠️ `LookbookImage.id` 가 `complete()` 호출 직후 null일 수 있음.
> `@Transactional` + `save()` 후 flush 되어야 ID가 채워짐. 테스트 필수.

---

### [M3-10] SecurityConfig — generate 엔드포인트 인증 확인

`/adm/v1/lookbooks/generate` 는 어드민 인증 필요.
현재 `anyRequest().authenticated()` + 어드민 SecurityFilterChain이 별도로 있는지 확인 필요.

---

## 요청/응답 명세

### 요청
```
POST /adm/v1/lookbooks/generate
Authorization: Bearer {adminToken}

{
  "styleType": "CASUAL",
  "season": "SPRING",
  "targetGender": "FEMALE",
  "tags": "미니멀,데일리",
  "items": [
    { "productId": 1, "position": "TOP" },
    { "productId": 4, "position": "BOTTOM" },
    { "productId": 9, "position": "SHOES" }
  ],
  "prompt": "밝은 자연광, 야외 카페, 매거진 화보 스타일"
}
```

### 응답 (202 Accepted)
```json
{
  "code": "SLB20201",
  "message": "룩북 생성 요청이 접수되었습니다.",
  "data": 42
}
```

### n8n → 백엔드 콜백 (완료)
```
POST /i/v1/lookbooks/42/complete

{
  "originUrl": "https://s3.../original_42.png",
  "imageUrl": "https://s3.../lookbook_42.webp",
  "aiScore": 87
}
```

---

## 완료 기준 체크리스트

- [x] `POST /adm/v1/lookbooks/generate` 호출 → Lookbook PENDING 생성 + 응답에 lookbookId 포함
- [ ] n8n Webhook으로 payload 전송 확인 (로컬에서 n8n 또는 RequestBin으로 검증)
- [x] `POST /i/v1/lookbooks/{id}/complete` 콜백 → LookbookImage 생성 + aiScore 저장 + ImageInspection PENDING 생성
- [x] `PATCH /adm/v1/lookbooks/{id}/approve` → Lookbook APPROVED + ImageInspection ADMIN_APPROVED
- [x] `GET /w/v1/lookbooks` → APPROVED 룩북 갤러리에 노출

> ✅ **M3 완료** — 2026-03-27 (`develop` 브랜치 push 완료)
>
> **실제 구현 차이점**:
> - `ai.worker.webhook-url` 대신 기존 설정 키 `worker.n8n.webhook-url` 사용
> - RestClient 대신 기존 `RestTemplate` Bean 재사용 (`AiWorkerClientImpl`)
> - n8n 실 연동 검증은 n8n 환경 구성 후 별도 진행 필요
