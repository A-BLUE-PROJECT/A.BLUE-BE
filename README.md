# 🌊 ALLBLUE — AI Fashion Orchestration Platform

> **파편화된 쇼핑을 단 하나의 룩으로 엮다.**
>
> 여러 쇼핑몰의 단품 데이터를 수집하여 AI가 크로스 셀러 코디 조합(룩북)을 자동 생성하고,
> 유저에게 통합된 쇼핑 경험을 제공하는 패션 오케스트레이션 플랫폼의 **백엔드 서버**입니다.

<br>

## 📌 프로젝트 핵심 요약

| 항목 | 내용 |
|------|------|
| **서비스 포지션** | 크로스 셀러 AI 룩북 오케스트레이터 (단순 애그리게이터 ✕) |
| **핵심 차별점** | A셀러 상의 + B셀러 하의 + C셀러 신발 → AI가 하나의 룩북으로 합성 |
| **개발 인원** | 1인 (기획 · 설계 · 백엔드 · 인프라) |
| **개발 기간** | 2026.03 ~ (진행 중) |


## 🔥 기술적 도전 과제 & 해결

### 1. 크롤링 데이터 수신 — Batch API 3-Step 패턴

> **문제**: 크롤러가 수백 건의 상품 데이터를 한 번에 보내면 타임아웃 및 데이터 유실 위험

**해결**: 배치 생성 → 청크 단위 전송 → 완료 통보의 3단계 프로토콜 설계

```
1. POST /api/batches              → batchId 발급 (상태: COLLECTING)
2. POST /api/batches/{id}/raw-data → 20건씩 청크 반복 전송
3. POST /api/batches/{id}/complete → 전송 완료 통보 (상태: COLLECTED)
4. 백엔드 내부: Normalization → Product 엔티티 적재
```

- **멱등성 보장**: 동일 `originId` 중복 적재 방지 (UPSERT)
- **데이터 유실 방지**: 크롤러 측 상태 갱신은 `complete` 성공 후에만 수행

---

### 2. AI 룩북 생성 — 비동기 파이프라인 + 부하 제어

> **문제**: Gemini API 호출은 평균 2초 소요 + Rate Limit 존재 → 동기 처리 시 UX 붕괴 + 비용 폭탄

**해결**: Redis Streams 기반 메시지 큐 + 상태 머신 + 우선순위 분리

```
[사용자 요청] → LookbookGenerateService
                    │
                    ▼ (상태: REQUESTED → QUEUED)
            ┌───────────────┐
            │ Redis Streams  │  ← Rate Limiting (분당 N건)
            │ Priority Queue │  ← HIGH: 유저 커스텀 / LOW: 자동 갤러리
            └───────┬───────┘
                    ▼
            ┌───────────────┐
            │  n8n Worker    │  → Gemini API 호출 (상태: GENERATING)
            └───────┬───────┘
                    ▼
            LookbookCallbackService  (상태: COMPLETED / FAILED)
                    │
                    ▼ (FAILED 시 DLQ 이동, 최대 3회 재시도)
            ┌───────────────┐
            │ Dead Letter Q  │
            └───────────────┘
```

- **`@Async` 콜백**: n8n 워커 완료 시 Webhook으로 비동기 수신, 블로킹 없음
- **우선순위 큐**: 유저가 직접 요청한 커스텀 룩북은 HIGH, 배치 자동 생성은 LOW
- **Dead Letter Queue**: 3회 실패 시 DLQ 격리, 관리자 수동 확인

---

### 3. 통합 결제 — 하이브리드 전략 + Saga 패턴

> **문제**: 무신사/네이버는 외부 주문 API 미제공 → 전체 통합 결제 불가능

**해결**: 셀러 유형에 따라 결제 방식을 이분화하는 하이브리드 전략

```
┌─ 딥링크 결제 (무신사/네이버 등) ─────────────────────────────┐
│  룩북 내 상품 클릭 → 원본 쇼핑몰 리다이렉트 → 어필리에이트 수수료  │
└────────────────────────────────────────────────────────────┘

┌─ 통합 결제 (카페24 셀러) ─────────────────────────────────────┐
│  PortOne 결제 → Saga Orchestrator → 셀러별 주문 분산 라우팅      │
│                                                              │
│  [결제 성공] → Seller A 주문 → Seller B 주문 → Seller C 주문    │
│  [부분 실패] → 성공 건 보상 트랜잭션(취소) → 유저 환불           │
└────────────────────────────────────────────────────────────┘
```

- **카페24 API 연동**: OAuth 2.0 → 실시간 재고/사이즈 조회 + 주문 생성
- **Saga/TCC 패턴**: 멀티 셀러 주문의 분산 트랜잭션, 부분 실패 시 보상 로직
- **Redisson 분산 락**: 동시 결제 시 재고 차감 동시성 제어

---

### 4. 동시성 제어 — Redisson 분산 락

> **문제**: 인기 상품에 동시 결제 요청이 몰리면 재고 초과 판매(Overselling) 위험

**해결**: Redis 기반 분산 락으로 재고 차감 원자성 보장

```java
RLock lock = redissonClient.getLock("stock:" + productId + ":" + size);
try {
    if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
        // 재고 확인 → 차감 → 주문 생성 (원자적 실행)
    }
} finally {
    lock.unlock();
}
```

---

### 5. API 응답 최적화 — Redis 캐싱 전략

> **문제**: 룩북 갤러리 피드는 동일한 데이터를 반복 조회 → DB 부하 증가

**해결**: 읽기 빈도 높은 데이터에 Redis 캐싱 적용

| 대상 | TTL | 무효화 조건 |
|------|-----|------------|
| 룩북 갤러리 피드 | 5분 | 신규 룩북 생성 시 |
| 상품 상세 정보 | 1시간 | 가격/재고 변경 시 |
| 인기 룩북 랭킹 | 10분 | Sorted Set 기반 실시간 갱신 |
| Fit Score 계산 결과 | 24시간 | FitProfile 변경 시 |

<br>

## 🗂️ API 명세 (주요 엔드포인트)

### 크롤링 데이터 수신
```
POST   /api/batches                    # 배치 생성
POST   /api/batches/{id}/raw-data      # 데이터 청크 전송
POST   /api/batches/{id}/complete      # 배치 완료 통보
```

### 룩북
```
GET    /api/lookbooks                  # 룩북 갤러리 피드 (페이지네이션 + 캐싱)
GET    /api/lookbooks/{id}             # 룩북 상세 (포함 상품 목록 + Fit Score)
POST   /api/lookbooks/generate         # 커스텀 룩북 생성 요청 (비동기)
POST   /api/lookbooks/callback         # AI 워커 완료 콜백 수신
```

### 상품
```
GET    /api/products                   # 상품 검색 (카테고리, 플랫폼, 가격대 필터)
GET    /api/products/{id}              # 상품 상세
GET    /api/products/{id}/fit-score    # Fit Score 조회
```

### 결제 (Phase 3)
```
POST   /api/orders                     # 통합 결제 요청 (Saga 시작)
GET    /api/orders/{id}                # 주문 상태 조회
POST   /api/orders/{id}/cancel         # 주문 취소 (보상 트랜잭션)
```

## 🗓️ 개발 로드맵

| Phase | 기간 | 내용 | 상태 |
|-------|------|------|------|
| **Phase 1** | 4주 | 단품 크롤링 수신 + AI 룩북 PoC + SNS 검증 | 🔄 진행 중 |
| **Phase 2** | 4주 | 웹 MVP (갤러리 + 커스텀 룩북 + FitProfile) | 📋 예정 |
| **Phase 3** | 4주 | 카페24 연동 + PortOne 통합 결제 + Saga | 📋 예정 |
| **Phase 4** | 지속 | 크롤링 소스 확장 + 수익화 + 모니터링 고도화 | 📋 예정 |

<br>

## 📂 관련 레포지토리

| 레포 | 설명 |
|------|------|
| **ALLBLUE-BE** (이 레포) | Spring Boot 백엔드 API 서버 |
| **ALLBLUE-CRAWLER** | Python 크롤러 (Playwright + Delta Crawling) |
| **ALLBLUE-FE** | Next.js 프론트엔드 (예정) |

<br>

## 🧑‍💻 개발자

**1인 개발** — 기획 · 설계 · 백엔드 · 크롤러 · 인프라 · CI/CD

이전 프로젝트 [DEKK](https://github.com/potenup-dekk)에서의 경험(크롤링 파이프라인, Batch API 설계, 모니터링 구축)을 기반으로,
방향을 전환하여 AI 오케스트레이션 플랫폼으로 독립 개발하고 있습니다.

<br>

---

<p align="center">
  <strong>ALLBLUE</strong> — The Ultimate Fashion Universe 🌊
</p>
