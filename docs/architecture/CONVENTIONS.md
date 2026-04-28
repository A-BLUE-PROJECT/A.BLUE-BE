# ALLBLUE Coding Conventions

---

## 1. Entity 규칙

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE lookbook SET deleted_at = now() WHERE id = ?")
@FilterDef(name = "deletedFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedFilter", condition = "deleted_at IS NULL")
public class Lookbook extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ 팩토리 메서드만 허용
    public static Lookbook create(String styleType, String season) {
        Lookbook lookbook = new Lookbook();
        lookbook.styleType = styleType;
        lookbook.season = season;
        return lookbook;
    }
}
```

- `@Setter`, `@Builder`, `@Data` **금지**
- 모든 Entity는 `BaseTimeEntity` 상속
- Soft Delete: `@SQLDelete` + `@Filter`

---

## 2. DTO 규칙

```java
// Command (Application 입력)
public record LookbookCreateCommand(String styleType, String season) {}

// Result (Application 출력)
public record LookbookCreateResult(Long id, String styleType) {}

// Request (Presentation 입력) — toCommand() 포함
public record LookbookCreateRequest(
    @NotBlank String styleType,
    @NotBlank String season
) {
    public LookbookCreateCommand toCommand() {
        return new LookbookCreateCommand(styleType, season);
    }
}

// Response (Presentation 출력) — from() 포함
public record LookbookResponse(Long id, String styleType) {
    public static LookbookResponse from(LookbookCreateResult result) {
        return new LookbookResponse(result.id(), result.styleType());
    }
}
```

---

## 3. Presentation Layer

```java
// Api Interface — Swagger 어노테이션 전용
@Tag(name = "Lookbook API")
public interface LookbookApi {
    @Operation(summary = "룩북 생성")
    ResponseEntity<ApiResponse<LookbookResponse>> create(
        @AuthenticationPrincipal Long userId,
        @Valid @RequestBody LookbookCreateRequest request
    );
}

// Controller — 비즈니스 로직 금지
@RestController
@RequestMapping("/w/v1/lookbooks")
@RequiredArgsConstructor
public class LookbookController implements LookbookApi {
    private final LookbookCommandService lookbookCommandService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<LookbookResponse>> create(
        @AuthenticationPrincipal Long userId,
        @Valid @RequestBody LookbookCreateRequest request
    ) {
        LookbookCreateResult result = lookbookCommandService.create(request.toCommand(userId));
        return ResponseEntity.ok(ApiResponse.of(LookbookResponse.from(result)));
    }
}
```

---

## 4. Exception 규칙

```java
// ErrorCode Enum
public enum LookbookErrorCode implements ErrorCode {
    LOOKBOOK_NOT_FOUND("ELB40401", "룩북을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;
}

// BusinessException
public class LookbookBusinessException extends BusinessException {
    public LookbookBusinessException(LookbookErrorCode errorCode) {
        super(errorCode);
    }
}

// 사용
throw new LookbookBusinessException(LookbookErrorCode.LOOKBOOK_NOT_FOUND);
```

---

## 5. Result Code 형식

`[S/E][Domain약어][HTTP상태코드][순번2자리]`

| 도메인 | 약어 | 성공 예 | 에러 예 |
| :--- | :--- | :--- | :--- |
| Lookbook | LB | `SLB20001` | `ELB40401` |
| Seller | SL | `SSL20001` | `ESL40001` |
| Product | PD | `SPD20001` | `EPD40001` |
| User | US | `SUS20001` | `EUS40001` |
| Auth | AT | `SAT20001` | `EAT40101` |

---

## 6. 의존성 주입

```java
// ✅ 올바른 방식
@RequiredArgsConstructor
public class LookbookCommandService {
    private final LookbookRepository lookbookRepository;
}

// ❌ 금지
@Autowired
private LookbookRepository lookbookRepository;
```

---

## 7. SRP & DRY

- 서비스 메서드가 fetch + validate + external API call + save를 모두 하면 `private` 메서드로 분리
- 동일 트랜잭션 내 동일 엔티티 재조회 금지 (영속성 컨텍스트 신뢰)
- 비즈니스 규칙 관련 매직 리터럴은 `private static final` 상수로 추출

---

## 8. Self-Critique Checklist

코드 출력 전 반드시 확인:

- [ ] Entity에 `@Builder` / `@Setter` 없는가?
- [ ] Controller에 비즈니스 로직 없는가?
- [ ] Swagger 어노테이션이 `...Api` 인터페이스에만 있는가?
- [ ] 서비스 메서드가 단일 책임인가?
- [ ] 매직 리터럴이 상수로 추출되었는가?
- [ ] 동일 트랜잭션 내 중복 조회 없는가?
- [ ] 예외가 `BusinessException` 계층을 따르는가?
- [ ] 목록 조회에 `PageResponse` 사용했는가?
- [ ] Phase 승인 없이 다음 단계로 진행하려 하는가? → 멈추고 승인 요청
