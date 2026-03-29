# ALLBLUE 서비스 설계서

**v2.0 | 2026.03.25**
**현재 개발 상태 기반 최신화**

---

## 1. 서비스 개요

ALLBLUE(올블루)는 AI가 여러 상품을 조합하여 완성된 코디(룩북)를 자동 생성하고,
사용자에게 통합된 패션 탐색 경험을 제공하는 **AI 패션 오케스트레이션 플랫폼**입니다.

| 항목 | 내용 |
| :--- | :--- |
| **슬로건** | 파편화된 쇼핑몰 데이터를 하나로, AI 룩북으로 완성하다 |
| **핵심 가치** | 단품 나열이 아닌, AI가 제안하는 완성된 스타일링 |
| **타겟 유저** | 코디 조합에 고민이 많은 남녀 온라인 쇼핑 유저 |
| **AI 엔진** | Google Gemini API + n8n 워커 |

---

## 2. MVP 범위 (현재 개발 기준)

### 포함
- 소셜 로그인 (카카오, 구글)
- AI 룩북 갤러리 피드
- 룩북 상세 Quick View (이미지 + AI 스코어 + 연관 상품)
- 어드민 검수 대시보드 (PENDING → APPROVED / REJECTED)
- AI 룩북 생성 파이프라인 (어드민 트리거 → n8n → Gemini → 콜백)

### 제외 (확정)
| 항목 | 사유 |
| :--- | :--- |
| Fit Score / FitProfile | 서비스 방향 변경으로 완전 제거 |
| Profile (닉네임, 체형 정보) | MVP 범위 축소. User 엔티티에서 제거 |
| 스와이프 / ActiveLog | UI 제거 결정 |
| 스와이프 기반 추천 | 스와이프 제거에 따라 함께 제거 |

### 보류 (Phase 3)
| 항목 | 사유 |
| :--- | :--- |
| 카페24 OAuth / 상품 동기화 | API 환경 미셋팅. 더미 데이터로 MVP 진행 |
| 통합 결제 (PortOne) | 카페24 연동 이후 |

---

## 3. 도메인 모델

```
User


Lookbook (AI 생성 코디)
 ├── LookbookItem (상품 포지션 매핑)
 │    └── Product (상품 데이터)
 └── ImageInspection (어드민 검수 기록)

Seller (카페24 쇼핑몰 — Phase 3)
 └── Product
```

### 핵심 엔티티

| 엔티티 | 주요 필드 | 비고 |
| :--- | :--- | :--- |
| `User` | email, role, provider, status | ACTIVE / DELETED. Profile 제거됨 |
| `Lookbook` | styleType, season, aiScore, status | PENDING / COMPLETED / FAILED |
| `LookbookItem` | lookbookId, productId, position | 상의/하의/신발/아우터/ACC |
| `Product` | name, brandName, price, category, imageUrl, originUrl | MVP는 더미 데이터 |
| `ImageInspection` | lookbookId, status, aiComment, adminComment | 어드민 검수 상태 관리 |

---

## 4. 상태 흐름

### 4-1. 룩북 생성 상태 (LookbookStatus)

```
[생성 요청]
    ↓
PENDING  ──(n8n 성공)──► COMPLETED
    │                        │
    └──(n8n 실패/타임아웃)──► FAILED
```

| 상태 | 의미 |
| :--- | :--- |
| `PENDING` | AI 이미지 생성 중 |
| `COMPLETED` | AI 생성 완료, 어드민 검수 대기 |
| `FAILED` | 생성 실패 (재시도 가능) |

### 4-2. 어드민 검수 상태 (InspectionStatus)

```
PENDING
  ├──(AI 자동 검토 통과)──► AI_PASSED
  ├──(AI 자동 검토 이상)──► AI_FLAGGED
  └──(워커 오류)──────────► WORKER_ERROR

AI_PASSED / AI_FLAGGED
  ├──(어드민 승인)──► ADMIN_APPROVED  ← 갤러리 노출
  └──(어드민 거부)──► ADMIN_REJECTED
```

> **갤러리 노출 조건:** LookbookStatus = COMPLETED + InspectionStatus = ADMIN_APPROVED

---

## 5. API 설계

### 5-1. Auth

| Method | Path | 설명 | 권한 |
| :--- | :--- | :--- | :--- |
| `GET` | `/oauth2/authorization/{provider}` | 소셜 로그인 시작 (kakao, google) | 없음 |
| `POST` | `/w/v1/auth/refresh` | Access Token 재발급 | 없음 |
| `GET` | `/w/v1/users/me` | 내 정보 조회 (id, email, role) | USER |
| `DELETE` | `/w/v1/users/me` | 회원 탈퇴 | USER |

### 5-2. 갤러리 (유저)

| Method | Path | 설명 | 권한 |
| :--- | :--- | :--- | :--- |
| `GET` | `/w/v1/lookbooks` | APPROVED 룩북 목록 (커서 페이지네이션) | 없음 |
| `GET` | `/w/v1/lookbooks/{id}` | 룩북 상세 (이미지 + AI 스코어 + 연관 상품) | 없음 |

**갤러리 피드 Response 예시**
```json
{
  "data": [
    {
      "id": 1,
      "imageUrl": "https://s3.../lookbook/1.jpg",
      "aiScore": 87,
      "styleType": "CASUAL",
      "season": "SPRING",
      "createdAt": "2026-03-25T00:00:00Z"
    }
  ],
  "nextCursor": "eyJpZCI6MTB9",
  "hasNext": true
}
```

**룩북 상세 Response 예시**
```json
{
  "id": 1,
  "imageUrl": "https://s3.../lookbook/1.jpg",
  "aiScore": 87,
  "styleType": "CASUAL",
  "season": "SPRING",
  "products": [
    {
      "id": 10,
      "position": "TOP",
      "name": "린넨 오버핏 셔츠",
      "brandName": "BRAND_A",
      "price": 39000,
      "imageUrl": "https://s3.../product/10.jpg",
      "originUrl": "https://cafe24mall.com/products/10"
    }
  ]
}
```

### 5-3. 어드민

| Method | Path | 설명 | 권한 |
| :--- | :--- | :--- | :--- |
| `GET` | `/adm/v1/lookbooks` | 검수 대기 룩북 목록 (PENDING/AI_PASSED/AI_FLAGGED) | ADMIN |
| `PATCH` | `/adm/v1/lookbooks/{id}/approve` | 룩북 승인 → ADMIN_APPROVED | ADMIN |
| `PATCH` | `/adm/v1/lookbooks/{id}/reject` | 룩북 거부 → ADMIN_REJECTED | ADMIN |
| `POST` | `/adm/v1/lookbooks/generate` | AI 룩북 생성 수동 트리거 | ADMIN |

---

## 6. AI 룩북 생성 파이프라인

### 전체 흐름

```
어드민 POST /adm/v1/lookbooks/generate
        │
        ▼
[Backend] Lookbook 생성 (status: PENDING)
    + ImageInspection 생성 (status: PENDING)
        │
        ▼
[Backend → n8n] AiWorkerClient.trigger(payload)
  payload: { lookbookId, productIds[], styleType, season }
        │
        ▼
[n8n] Gemini API로 이미지 합성
        │
        ├──(성공)──► POST /i/v1/lookbooks/{id}/complete
        │              { imageUrl, aiScore, aiComment }
        │                │
        │                ▼
        │           Lookbook status → COMPLETED
        │           ImageInspection status → AI_PASSED or AI_FLAGGED
        │
        └──(실패)──► POST /i/v1/lookbooks/{id}/fail
                       Lookbook status → FAILED
```

### 재시도 정책

- `PENDING` 상태가 **10분 이상** 지속된 룩북은 스케줄러가 자동 재시도
- 최대 재시도 횟수: **3회** (초과 시 FAILED 처리)

### Internal Callback API

| Method | Path | 설명 | 권한 |
| :--- | :--- | :--- | :--- |
| `POST` | `/i/v1/lookbooks/{id}/complete` | 생성 성공 콜백 (n8n → Backend) | Internal |
| `POST` | `/i/v1/lookbooks/{id}/fail` | 생성 실패 콜백 (n8n → Backend) | Internal |

---

## 7. AI Score 정의

> Fit Score(체형 기반)와 다른 개념. 삭제되지 않음.

**AI Score (0~100)** = AI가 룩북 내 상품들의 **스타일 조화도**를 평가한 점수

- Gemini API가 생성 시 함께 산출
- 갤러리에서 "AI 시너지 뱃지"로 시각화
- 점수 기준: 색상 조화 + 스타일 통일성 + 시즌 적합성

---

## 8. 데이터 전략

| 단계 | 상품 데이터 소스 |
| :--- | :--- |
| **MVP (현재)** | 더미 데이터 (DB에 직접 시드) |
| **Phase 3** | 카페24 Open API (OAuth 2.0) 연동, 스케줄러 동기화 |

---

## 9. 기술 스택

| 구성 요소 | 기술 |
| :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.5 |
| **Frontend** | Next.js (TypeScript), TailwindCSS, Zustand |
| **AI Worker** | n8n Webhook + Google Gemini API |
| **Database** | PostgreSQL |
| **Cache** | Redis (API 캐싱, 분산 락) |
| **Storage** | AWS S3 (상품 이미지, AI 생성 이미지) |
| **Monitoring** | Prometheus, Grafana, Loki |

---

## 10. 개발 단계

### Phase 1 — MVP (현재)
**목표:** 실제 동작하는 최소 서비스 완성

- [ ] ActiveLog 도메인 삭제
- [ ] Profile 제거 (Profile 엔티티, ProfileRepository, UserProfileUpdateCommand, 관련 서비스/컨트롤러 정리)
- [ ] 소셜 로그인 API 완성 (카카오, 구글)
- [ ] 갤러리 피드 API (커서 페이지네이션)
- [ ] 룩북 상세 API
- [ ] 어드민 검수 API (승인/거부)
- [ ] AI 파이프라인 API (트리거 + 콜백)
- [ ] n8n AiWorkerClient 구현
- [ ] 더미 상품 데이터 시드

### Phase 2 — 안정화
**목표:** 성능 및 운영 안정화

- [ ] Redis 캐싱 (갤러리 피드)
- [ ] 룩북 생성 재시도 스케줄러 고도화
- [ ] Swagger 문서 정비

### Phase 3 — 카페24 연동
**목표:** 실제 상품 데이터 기반 운영

- [ ] 카페24 OAuth 환경 셋팅
- [ ] 상품 동기화 스케줄러 (Delta Sync)
- [ ] 원본몰 링크 연동 검증

### Phase 4 — 수익화
**목표:** 어필리에이트 수익 창출

- [ ] 상품 클릭 트래킹
- [ ] PortOne 통합 결제
- [ ] 셀러 SaaS 대시보드

---

## 11. URL 구조 컨벤션

| Prefix | 대상 |
| :--- | :--- |
| `/w/v1/` | 일반 유저 (Web) |
| `/adm/v1/` | 어드민 |
| `/i/v1/` | Internal (서버 간 통신, n8n 콜백 등) |
| `/s/v1/` | 셀러 (Phase 3) |
