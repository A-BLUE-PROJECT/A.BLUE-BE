# ALLBLUE 리팩토링 실행 계획 (Refactoring Plan)

**v1.0 | 2026.03.23**

---

## 현재 상태 (AS-IS)

DEKK 프로젝트 기반의 `Card` 중심 도메인 구조.

```
com.allblue
├── activelog      ← 스와이프 로그
├── admin          ← 어드민
├── auth           ← JWT/OAuth2 인증
├── card           ← ❌ DEKK 핵심 도메인 (제거 대상)
├── category       ← 카테고리
├── common         ← 공통 유틸
├── crawl          ← ❌ 크롤 배치 (대체 대상)
├── deck           ← ❌ DEKK 전용 (제거 대상)
├── global         ← 글로벌 설정
├── security       ← 보안 설정
└── user           ← 유저
```

---

## 목표 상태 (TO-BE)

카페24 API 기반의 `Lookbook` + `Product` 중심 도메인 구조.

```
com.allblue
├── activelog      ← 재활용
├── admin          ← 재활용
├── auth           ← 재활용
├── seller         ← ✅ 신설 (카페24 OAuth 연동)
├── product        ← ✅ 신설 (독립 상품 엔티티 + 카페24 동기화)
├── lookbook       ← ✅ 신설 (card 대체, AI 룩북 도메인)
├── category       ← 재활용 (정규화 로직 추가)
├── common         ← 재활용
├── global         ← 재활용
├── security       ← 재활용
└── user           ← 확장 (FitProfile 추가)
```

---

## 실행 계획

### Phase 1 — Seller 패키지 신설 (카페24 OAuth 연동)
> **목표:** 카페24 쇼핑몰 인증 및 Access Token 관리

- [ ] `seller` 패키지 생성
- [ ] `Seller` 엔티티 설계 (mallId, accessToken, refreshToken, tokenExpiresAt)
- [ ] 카페24 OAuth 2.0 인증 흐름 구현
  - [ ] `GET /oauth/authorize` → 카페24 인증 페이지 리다이렉트
  - [ ] `GET /oauth/callback` → 인증코드 수신 및 Access Token 발급
  - [ ] Access Token 만료 시 자동 갱신 (Refresh Token)
- [ ] `Seller` 저장 및 조회 Repository 구현
- [ ] 카페24 API 호출용 `Cafe24ApiClient` 공통 HTTP 클라이언트 구현

- [x] `seller` 패키지 생성
- [x] `Seller` 엔티티 설계 (mallId, accessToken, refreshToken, tokenExpiresAt)
- [x] 카페24 OAuth 2.0 인증 흐름 구현
  - [x] `GET /s/v1/oauth/authorize` → 카페24 인증 페이지 리다이렉트
  - [x] `GET /s/v1/oauth/callback` → 인증코드 수신 및 Access Token 발급
  - [x] `POST /s/v1/oauth/{mallId}/refresh` → Access Token 만료 시 갱신
- [x] `Seller` 저장 및 조회 Repository 구현
- [x] 카페24 API 호출용 `Cafe24ApiClient` 공통 HTTP 클라이언트 구현 (RestClient 기반)

**완료 기준:** 카페24 쇼핑몰 OAuth 인증 후 Access Token DB 저장 확인

---

### Phase 2 — Lookbook 도메인 신설 (card 교체)
> **목표:** DEKK의 Card 도메인을 ALLBLUE의 Lookbook 도메인으로 전면 교체

- [ ] `card` 패키지 제거
- [ ] `deck` 패키지 제거
- [ ] `lookbook` 패키지 생성
- [ ] `Lookbook` 엔티티 설계
  - StyleType (CASUAL, FORMAL, STREET, SPORTY)
  - Season (SPRING, SUMMER, FALL, WINTER)
  - LookbookStatus (PENDING, COMPLETED, FAILED)
- [ ] `LookbookItem` 엔티티 설계 (Lookbook ↔ Product 매핑, Position)
- [ ] `LookbookImage` 엔티티 설계 (AI 합성 이미지 URL)
- [ ] Flyway 마이그레이션 스크립트 작성
- [ ] 기존 `activelog` 의 Card 참조 → Lookbook 참조로 수정

**완료 기준:** Lookbook CRUD API 동작 확인

---

### Phase 3 — Product 패키지 신설 (카페24 상품 동기화)
> **목표:** 카페24 API에서 상품 데이터를 수집하여 독립 Product 엔티티로 저장

- [ ] `crawl` 패키지 제거
- [ ] `product` 패키지 생성
- [ ] `Product` 엔티티 설계
  - SellerId (FK), ExternalProductId, Category, BrandName
  - Price, SalePrice, ProductImageUrl, OriginUrl, StockStatus
- [ ] `Cafe24ProductSyncService` 구현
  - `GET /api/v2/admin/products` 호출 및 파싱
  - Delta Sync (신규/변경 상품만 저장)
- [ ] 카테고리 정규화 로직 구현 (카페24 카테고리 → TOP/BOTTOM/SHOES/OUTER/ACC)
- [ ] 상품 이미지 AWS S3 저장 파이프라인 구현
- [ ] 동기화 스케줄러 설정

**완료 기준:** 카페24 쇼핑몰 상품 목록 DB 저장 및 이미지 S3 업로드 확인

---

### Phase 4 — User 확장 (FitProfile)
> **목표:** 사용자 체형 데이터 저장 및 핏 스코어 계산 기반 마련

- [ ] `FitProfile` 엔티티 설계 (height, weight, shoulderWidth, chestSize, waistSize, hipSize)
- [ ] FitProfile CRUD API 구현
- [ ] 핏 스코어 계산 로직 설계 (FitScore = 체형 vs 상품 실측 비교)

**완료 기준:** 사용자 체형 등록 및 조회 API 동작 확인

---

### Phase 5 — AI 룩북 생성 파이프라인
> **목표:** Gemini API를 통한 비동기 룩북 이미지 합성

- [ ] Gemini API 연동 클라이언트 구현
- [ ] 룩북 생성 요청 시 PENDING 상태 저장
- [ ] n8n Webhook 연동으로 비동기 이미지 합성 트리거
- [ ] 합성 완료 콜백 수신 → COMPLETED 상태 전환 및 이미지 S3 저장
- [ ] 실패 시 FAILED 처리 및 재시도 로직

**완료 기준:** 룩북 생성 요청 → AI 합성 → 이미지 저장까지 End-to-End 동작 확인

---

## 패키지별 처리 요약

| 패키지 | 처리 | 비고 |
| :--- | :--- | :--- |
| `card` | ❌ 제거 | `lookbook` 으로 전면 교체 |
| `deck` | ❌ 제거 | DEKK 전용, 불필요 |
| `crawl` | ❌ 제거 | 카페24 API 동기화로 대체 |
| `seller` | ✅ 신설 | 카페24 OAuth + API 클라이언트 |
| `product` | ✅ 신설 | 독립 상품 엔티티 + 동기화 |
| `lookbook` | ✅ 신설 | AI 룩북 핵심 도메인 |
| `user` | 🔧 확장 | FitProfile 추가 |
| `category` | 🔧 수정 | 카테고리 정규화 로직 추가 |
| `activelog` | ♻️ 재활용 | Card → Lookbook 참조 수정 |
| `admin` | ♻️ 재활용 | 룩북 검수 로직으로 전환 |
| `auth` | ♻️ 재활용 | 그대로 사용 |
| `common` | ♻️ 재활용 | 그대로 사용 |
| `global` | ♻️ 재활용 | 그대로 사용 |
| `security` | ♻️ 재활용 | 그대로 사용 |

---

## 진행 현황

| Phase | 상태 |
| :--- | :--- |
| Phase 1 — Seller / 카페24 OAuth | ✅ 완료 |
| Phase 2 — Lookbook 도메인 | ✅ 완료 |
| Phase 3 — Product / 카페24 동기화 | 🔧 구조 완료 (동기화 로직 미착수) |
| Phase 4 — FitProfile | ✅ 완료 |
| Phase 5 — AI 룩북 파이프라인 | 🔲 대기 |

### Phase 2 상세

- [x] `card` 패키지 제거
- [x] `deck` 패키지 제거
- [x] `lookbook` 패키지 생성
- [x] `Lookbook` 엔티티 설계 (StyleType, Season, LookbookStatus)
- [x] `LookbookItem` 엔티티 설계 (Lookbook ↔ Product 매핑)
- [x] `LookbookImage` 엔티티 설계 (AI 합성 이미지 URL)
- [x] 기존 `activelog` 의 Card 참조 → Lookbook 참조로 수정
- [ ] Flyway 마이그레이션 스크립트 작성 (ddl-auto: create 방식으로 대체)

### Phase 3 상세

- [x] `crawl` 패키지 제거
- [x] `product` 패키지 생성
- [x] `Product` 엔티티 설계
- [ ] `Cafe24ProductSyncService` 구현
- [ ] 카테고리 정규화 로직 구현
- [ ] 상품 이미지 S3 저장 파이프라인 구현
- [ ] 동기화 스케줄러 설정

### Phase 4 상세

- [x] `FitProfile` 엔티티 설계
- [x] FitProfile CRUD API 구현
- [ ] 핏 스코어 계산 로직 설계
