# ALLBLUE 시스템 아키텍처

---

## 1. 기술 스택

| 구성 요소 | 기술 | 역할 |
| :--- | :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.5 | 비즈니스 로직, REST API |
| **Frontend** | Next.js (TypeScript) | 갤러리 뷰, 어드민 대시보드 |
| **AI Worker** | n8n Webhook + Google Gemini API | 비동기 룩북 이미지 합성 |
| **Database** | PostgreSQL | 전체 도메인 데이터 |
| **Cache** | Redis | API 캐싱, 분산 락 |
| **Storage** | AWS S3 | 상품 이미지, AI 생성 이미지 |
| **Data Source** | 카페24 Open API (OAuth 2.0) | 상품 데이터 (Phase 3) |
| **Monitoring** | Prometheus, Grafana, Loki | 메트릭, 장애 모니터링 |

---

## 2. 도메인 모델

```
User
 └── Profile (닉네임, 체형 정보)

Lookbook (AI 생성 코디)
 ├── LookbookItem (상품 포지션 매핑)
 │    └── Product (상품 데이터)
 └── LookbookImage (AI 생성 이미지)
      └── ImageInspection (어드민 검수)

Seller (카페24 쇼핑몰 — Phase 3)
 └── Product
```

### 핵심 엔티티

| 엔티티 | 주요 필드 |
| :--- | :--- |
| `User` | email, role, provider, status(ACTIVE/DELETED) |
| `Lookbook` | styleType, season, targetGender, aiScore, status(PENDING/COMPLETED/FAILED) |
| `LookbookItem` | lookbookId(FK), productId(FK), position |
| `LookbookImage` | lookbookId(FK), originUrl, imageUrl |
| `ImageInspection` | lookbookImageId(FK), status, aiComment, adminId |
| `Product` | sellerId(FK), name, brandName, price, category, imageUrl, originUrl |
| `Seller` | mallId, accessToken, refreshToken, tokenExpiresAt |

---

## 3. 핵심 플로우

### 3-1. 소셜 로그인 (OAuth2)

```
유저 → GET /oauth2/authorization/{provider}
     → 소셜 로그인 페이지 리다이렉트
     → 사용자 동의
     → OAuth2SuccessHandler
     → JWT Access/Refresh Token 발급 (Cookie)
     → defaultRedirectUri 리다이렉트
```

### 3-2. AI 룩북 생성 파이프라인 (비동기)

```
어드민 → POST /adm/v1/lookbooks/generate
       → Lookbook 생성 (status: PENDING)
       → ImageInspection 생성 (status: PENDING)
       → AiWorkerClient → n8n Webhook 트리거

n8n    → Gemini API 이미지 합성
       → POST /i/v1/lookbooks/{id}/complete
            → LookbookImage 저장 (S3)
            → Lookbook status: COMPLETED
            → ImageInspection status: AI_PASSED or AI_FLAGGED
       → (실패 시) POST /i/v1/lookbooks/{id}/fail
            → Lookbook status: FAILED
```

### 3-3. 어드민 검수 플로우

```
PENDING → AI_PASSED / AI_FLAGGED (n8n 자동)
       → ADMIN_APPROVED  ← 갤러리 노출
       → ADMIN_REJECTED
```

### 3-4. 카페24 상품 동기화 (Phase 3)

```
Scheduler (매일 새벽)
  → GET /api/v2/admin/products (카페24 API)
  → 신규/변경 상품만 필터링 (Delta Sync)
  → 카테고리 정규화 (TOP/BOTTOM/SHOES/OUTER/ACC)
  → 이미지 S3 업로드
  → Product 저장/갱신 (DB)
```

---

## 4. 레이어드 아키텍처

```
[Presentation]   Controller / Api(Swagger) / Request / Response
      ↓
[Application]    CommandService / QueryService / DTOs (command, result)
      ↓
[Domain]         Entity / Repository(interface) / BusinessException / ErrorCode
      ↓
[Infrastructure] RepositoryImpl / JpaRepository / ExternalApiClient
```

---

## 5. URL 구조 컨벤션

| Prefix | 대상 |
| :--- | :--- |
| `/w/v1/` | 일반 유저 (Web) |
| `/adm/v1/` | 어드민 |
| `/i/v1/` | Internal (서버 간 통신, n8n 콜백 등) |
| `/s/v1/` | 셀러 (Phase 3) |
