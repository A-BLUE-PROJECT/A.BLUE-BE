# ALLBLUE — Agent Orientation

> AI 패션 오케스트레이션 플랫폼. 카페24 Open API로 상품 데이터를 수집하고, Google Gemini로 AI 룩북을 자동 생성한다.
> 세부 기획: `docs/full-service-design.md` / 아키텍처: `docs/ARCHITECTURE.md` / 컨벤션: `docs/CONVENTIONS.md`

## Package Structure

```
com.allblue
├── seller      # 카페24 OAuth 연동, Access Token 관리 (Phase 3)
├── product     # 독립 상품 엔티티 (MVP는 더미 데이터)
├── lookbook    # AI 룩북 핵심 도메인 (Lookbook / LookbookItem / LookbookImage)
├── user        # 사용자 (Profile 포함, FitProfile 제거됨)
├── admin       # 어드민 인증 + 룩북 검수 (ImageInspection)
├── auth        # JWT / OAuth2 인증
├── category    # 카테고리 정규화 (TOP/BOTTOM/SHOES/OUTER/ACC)
├── common      # BaseTimeEntity, ApiResponse, PageResponse, BusinessException
└── security    # Spring Security 설정
```

---

## Key Constraints (요약)

상세 규칙 → `docs/CONVENTIONS.md`

1. Entity: `@Getter` + `@NoArgsConstructor(PROTECTED)` + `create()` 팩토리만. `@Setter/@Builder/@Data` 금지.
2. DI: `@RequiredArgsConstructor` + `private final`. `@Autowired` 금지.
3. DTO: 모두 `record`.
4. Exception: `[Domain]BusinessException` + `[Domain]ErrorCode` 필수.
5. Response: `ApiResponse<T>` 래핑. 목록은 `PageResponse`.
6. API Path: `/w/v1/[domain]` (유저) · `/i/v1/[domain]` (내부) · `/adm/v1/[domain]` (어드민) · `/s/v1/[domain]` (셀러)
7. Result Code: `[S/E][Domain][HTTP][Seq2]` 형식 (예: `SLB20001`, `ESL40001`)

---

## Git 규칙

- **커밋은 사용자가 명시적으로 요청할 때만** 생성한다.
- **푸쉬(push)는 사용자가 명시적으로 요청할 때만** 실행한다. 커밋 후 자동으로 푸쉬하지 않는다.

---

## Interaction Protocol

> 사용자의 명시적 승인 없이 다음 Phase로 절대 진행하지 않는다.

| Phase | Output | Wait |
| :--- | :--- | :--- |
| **0. Blueprint** | Sequence Diagram (Mermaid), Entity 모델링, Endpoint 명세 | *"이 설계대로 개발을 진행할까요?"* |
| **1. Foundation** | Entity, Repository interface, ErrorCode, ResultCode, Flyway script | *"Phase 1 완료. 서비스 로직으로 넘어갈까요?"* |
| **2. Business Logic** | CommandService, QueryService, DTOs | *"Phase 2 완료. 인터페이스 레이어로 넘어갈까요?"* |
| **3. Interface** | Controller, Api (Swagger), RepositoryImpl, External Client | — |

---

## Docs Structure

```
docs/
├── ARCHITECTURE.md        # 시스템 설계 상세
├── CONVENTIONS.md         # 코딩 컨벤션 상세
├── full-service-design.md # 서비스 기획 전문
├── domain-model-changes.md
├── technical-architecture.md
├── service-overview.md
├── roadmap.md
├── plan/                  # 진행 중인 작업 플랜
├── draft/                 # 초안
└── review/                # 리뷰·검증
```

현재 진행 플랜 → `docs/plan/plan.md`
