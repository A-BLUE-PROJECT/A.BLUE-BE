# PLAN: 프론트엔드 MVP 연동 작업 계획

> 기준 문서: `docs/frontend/BACKEND_HANDOFF_MVP.md`
> 백엔드 구현 상태를 프론트엔드 요구사항 기준으로 갭 분석 후 작업 우선순위 정리

---

## 현황 요약 (2026-03-27 기준)

| 마일스톤 | 상태 | 완료일 |
|---|---|---|
| M1 — 기반 데이터 & 상태 정합성 | ✅ 완료 | 2026-03-27 |
| M2 — B2C 갤러리 & 룩북 상세 | ✅ 완료 | 2026-03-27 |
| M3 — AI 룩북 생성 파이프라인 | ✅ 완료 | 2026-03-27 |
| M4 — 어드민 상품 관리 | ✅ 완료 | 2026-03-27 |

---

## 1. 갭 분석 (Gap Analysis)

### ✅ 구현 완료

| 프론트 요구사항 | 백엔드 엔드포인트 | 비고 |
| :--- | :--- | :--- |
| 소셜 로그인 (카카오) | `GET /oauth2/authorization/{provider}` | JWT Cookie 발급 |
| 룩북 승인 | `PATCH /adm/v1/lookbooks/{id}/approve` | Lookbook.status = APPROVED 동기화 포함 |
| 룩북 거부 | `PATCH /adm/v1/lookbooks/{id}/reject` | Lookbook.status = REJECTED 동기화 포함 |
| 토큰 갱신 | `POST /w/v1/auth/refresh` | |
| 어드민 로그인 | `POST /adm/v1/auth/login` | |
| 갤러리 피드 (APPROVED 필터) | `GET /w/v1/lookbooks` | cursor 페이지네이션 포함, aiScore 반환 |
| 룩북 상세 (상품 정보 포함) | `GET /w/v1/lookbooks/{id}` | items에 brandName/price/imageUrl/originUrl 포함 |
| AI 룩북 생성 트리거 | `POST /adm/v1/lookbooks/generate` | 202 Accepted, n8n Webhook 연동 |
| AI 완료 콜백 | `POST /i/v1/lookbooks/{id}/complete` | aiScore 저장 + ImageInspection 자동 생성 |
| 어드민 룩북 목록 | `GET /adm/v1/lookbooks` | 전체 상태 조회 (PENDING 포함) |

### ✅ M4 구현 완료

| 프론트 요구사항 | 백엔드 엔드포인트 | 비고 |
| :--- | :--- | :--- |
| 어드민 상품 목록 | `GET /adm/v1/products` | 카테고리 필터(`?category=TOP`) 지원 |
| 상품 숨김/공개 | `PATCH /adm/v1/products/{id}/hidden` | `hidden` boolean 필드 |

### ❌ Phase 3 (미구현)

| 프론트 요구사항 | 구현할 엔드포인트 | 우선순위 |
| :--- | :--- | :--- |
| 상품 동기화 트리거 | `POST /adm/v1/products/sync` | 🟡 카페24 연동 시 |

---

## 2. 실제 구현 사항 (M1~M3)

### M1 — 기반 데이터 & 상태 정합성

- `LookbookStatus`: `APPROVED`, `REJECTED` 추가
- `Lookbook`: `aiScore` 필드, `approve()` / `reject()` 메서드 추가
- `LookbookCompleteCommand` / `LookbookCompleteRequest`: `aiScore` 파라미터 추가
- `LookbookCommandService.complete()`: `ImageInspection` 자동 생성 로직 포함
- `AdminInspectionCommandService`: approve/reject 시 `Lookbook.status` 동기화
- `DataInitializer`: 더미 Product 12개 시드 (local/dev 환경만)

### M2 — B2C 갤러리 & 룩북 상세

- `LookbookJpaRepository`: 커서 기반 APPROVED 필터 쿼리 추가
- `LookbookRepository` / `LookbookRepositoryImpl`: `findApproved(cursor, size)` 구현
- `LookbookQueryService`:
  - `findAll()` → APPROVED 필터 + 커서 페이지네이션
  - `findAllForAdmin()` 추가 → 전체 상태 목록
  - `findById()` → Product 일괄 조회 후 상품 상세 JOIN
- `LookbookResult` / `LookbookResponse`: `aiScore` 추가
- `LookbookDetailResult` / `LookbookDetailResponse`: 상품 상세 필드 추가
- `SecurityConfig`: `/w/v1/lookbooks`, `/w/v1/lookbooks/**` permitAll 추가

### M3 — AI 룩북 생성 파이프라인

- `LookbookGenerateRequest` (신규): 생성 요청 DTO
- `LookbookGenerateCommand` (신규): Application 레이어 Command
- `AiWorkerPayload`: `prompt` 필드 추가
- `LookbookCommandService.generate()`: Lookbook 생성 + payload 빌드 + n8n 트리거
- `AiWorkerClientImpl` (신규): RestTemplate 기반 n8n Webhook HTTP POST 구현
- `AdminLookbookController` / `AdminLookbookApi`: `POST /generate` 엔드포인트
- `LookbookResultCode`: `LOOKBOOK_GENERATE_ACCEPTED` (202) 추가

> **실제 구현 차이점 (plan vs 실제)**:
> - `ai.worker.webhook-url` → 기존 설정 `worker.n8n.webhook-url` 사용
> - RestClient → 기존 `RestTemplateConfig` 재사용 (RestTemplate 사용)

---

## 3. 남은 작업 — M4

→ `docs/frontend/PLAN_M4_어드민상품관리.md` 참고

---

## 4. URL 정렬 확인

프론트 `BACKEND_HANDOFF_MVP.md` 는 `/api/public/...`, `/api/admin/...` 경로 사용.
백엔드는 `/w/v1/...`, `/adm/v1/...` 경로 사용.

> **조치 필요**: 프론트에서 `NEXT_PUBLIC_API_URL` + `/w/v1/`, `/adm/v1/` 경로로 변경하거나,
> Nginx reverse proxy에서 `/api/public/` → `/w/v1/`, `/api/admin/` → `/adm/v1/` 매핑.
> **백엔드 URL은 변경하지 않음** (컨벤션 준수).

---

## 5. 알려진 이슈 / 논의 필요 사항

| 항목 | 내용 | 상태 |
|---|---|---|
| 갤러리 `hasNext` 필드 | 커서 기반 페이지네이션에서 다음 페이지 존재 여부를 응답에 포함해야 UX 개선 가능 | 🟡 논의 필요 |
| 비로그인 갤러리 접근 | `/w/v1/lookbooks` permitAll 처리 완료. 상세 페이지는 인증 불필요 여부 확인 필요 | 🟡 프론트 확인 필요 |
| `LookbookAiPipelineService` | 재시도 스케줄러용으로 별도 존재. `generate()`와 역할 중복 여부 확인 필요 | 🟡 검토 필요 |
