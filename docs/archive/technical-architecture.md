# ALLBLUE 기술 아키텍처 (Technical Architecture)

## 1. 시스템 구성 (System Stack)
| 구성 요소 | 기술 스택 (Tech Stack) | 주요 역할 |
| :--- | :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.5 | 비즈니스 로직, REST API, 결제/주문 처리 |
| **Frontend** | Next.js (TypeScript) | 웹 기반 갤러리 뷰, 커스텀 룩북 생성 UI |
| **Crawler** | Python (Playwright, curl_cffi) | 무신사 등 쇼핑몰 데이터 수집 (Delta Crawling) |
| **AI Worker** | n8n Webhook + Google Gemini API | 비동기 이미지 합성 및 룩북 생성 로직 |
| **Database** | PostgreSQL | 상품, 룩북, 사용자, 주문 데이터 관리 |
| **Cache** | Redis | API 응답 캐싱, 세션 관리, 분산 락 |
| **Storage** | AWS S3 | 상품 및 합성된 룩북 이미지 저장 |
| **Monitoring** | Prometheus, Grafana, Loki | 메트릭 수집 및 장애 모니터링 |

## 2. 주요 기술 전략
### 2-1. 데이터 수집 (Crawl to Backend)
- **Batch API 패턴:** 크롤러는 `POST /batches`를 통해 수집 작업을 생성하고, `Normalization`을 거친 데이터를 백엔드로 전송.
- **Delta Crawling:** 신규 상품 및 변경된 가격 정보만 선별하여 동기화.

### 2-2. AI 룩북 생성 파이프라인
- **비동기 처리 (Async):** 사용자가 룩북 생성을 요청하면 `PENDING` 상태로 저장 후 AI Worker가 생성 완료 시 `COMPLETED`로 전환.
- **Gemini API 연동:** 다양한 이미지 입력을 바탕으로 자연스러운 코디 매칭 이미지(모델 착용샷) 합성.

### 2-3. 통합 결제 시스템 (Phase 3+)
- **Saga 패턴:** 카페24 등 외부 플랫폼과 분산 트랜잭션을 처리하기 위해 상태 기계 기반의 주문 처리 로직 구현.
- **PortOne 연동:** 다양한 결제 수단 지원.
