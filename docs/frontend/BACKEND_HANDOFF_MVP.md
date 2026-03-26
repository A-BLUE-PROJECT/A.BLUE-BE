# 📘 ALLBLUE Platform: MVP Backend Handoff Document

> **작성 목적**: 본 문서는 프론트엔드 파트에서 기획 및 설계, 개발을 완료한 **ALLBLUE 패션 플랫폼(MVP)**의 전반적인 구조와 백엔드 개발팀이 처음부터 참고하여 개발을 시작할 수 있도록 작성된 종합 인수인계 문서입니다.

---

## 1. 프로젝트 개요 (Overview)
ALLBLUE는 매거진 화보 스타일의 프론트엔드와 무중력(Anti-gravity) 테마의 인터랙션을 결합한 차세대 **AI 패션 룩북 오케스트레이션 플랫폼**입니다.

### 🌟 현재 프론트엔드 상태 (Frontend Status)
*   **기술 스택**: Next.js 14 (App Router), TailwindCSS, Zustand (로컬 상태 관리용)
*   **완성도 (100%)**: 기획된 UI/UX, B2C 메인 갤러리 피드, 퀵뷰 모달, B2B 어드민(제너레이터, 큐레이터, 상품 리스트) 등 모든 화면의 퍼블리싱과 Mock 데이터 상태 관리(더미 데이터 연동)가 완벽하게 동작하는 상태입니다.
*   **요청 사항**: 이제 백엔드 파트에서 이 문서에 명세된 **REST API와 DB 스키마**를 설계/구현해주시면, 프론트엔드의 더미(Mock) 로직을 실제 API 통신(`axios/fetch`)으로 교체하여 실서비스를 런칭할 수 있습니다.

---

## 2. 필수 데이터 도메인 구조 (Core Entities)
백엔드 DB 설계 시 반드시 포함되어야 할 핵심 엔티티(Entity) 3가지입니다.

### (1) User (유저)
*   일반 유저(B2C)와 관리자(B2B)를 철저히 분리해야 합니다. (`Role: NORMAL | ADMIN`)
*   Next.js 미들웨어(`middleware.ts`, `proxy.ts`)에서 어드민 접근(`/hq/*`)을 차단할 수 있도록 로그인 시 쿠키나 JWT 토큰에 Role 정보를 담아 내려주어야 합니다.

### (2) Product (쇼핑몰 상품)
*   카페24 등에서 동기화해오는 옷 정보입니다.
*   **필수 필드**: `id`, `name`, `thumbnailUrl`, `price`, `stock`, `category` (OUTER, TOP, BOTTOM, ACC), `status` (SYNCED, HIDDEN)

### (3) Lookbook (룩북: 핵심 AI 결과물)
*   이미지, 프롬프트, 연결된 상품 목록이 저장되는 핵심 정보입니다.
*   **필수 필드**: `id`, `imageUrl`, `prompt`, `fitScore`(0~100 모델 핏 점수), `status` (PENDING, APPROVED, REJECTED), `mappedProducts` (Product와의 1:N 혹은 N:N 관계)

---

## 3. 프론트엔드 화면 구성 및 필요 API 명세 (API Requirements)

프론트엔드 구조에 맞춰서 백엔드 시스템이 제공해야 할 API(End-points) 목록입니다.

### 👥 파트 A: B2C 사용자 페이지 (User Site)
일반 사용자가 방문하여 패션 화보를 구경하는 영역입니다.

1. **메인 갤러리 피드 로드**
    *   `GET /api/public/lookbooks`
    *   **설명**: 관리자가 최종 승인한(`status: APPROVED`) 최신 룩북 목록 반환. (페이지네이션 필요)
    *   **프론트엔드 동작**: 안티그래비티 폭포수(Masonry) 레이아웃으로 이미지와 타이포그래피 노출.
2. **단일 룩북 및 매핑 상품 퀵뷰(Quick View)**
    *   `GET /api/public/lookbooks/{lookbook_id}`
    *   **설명**: 룩북 클릭 시 해당 이미지의 상세 정보와, 이 룩북을 구성할 때 쓰인 실제 상품(Product) 리스트 및 핏스코어(fitScore) 반환.
3. **사용자 로그인 (소셜 Auth)**
    *   `POST /api/auth/login` (카카오/네이버 등 연동 후 콜백)
    *   **프론트엔드 동작**: 우측 상단의 반투명 블러 로그인 모달에서 카카오톡 아이콘을 클릭합니다. (Session Cookie 발급 필수)

---

### 🛡 파트 B: B2B 관리자 대시보드 (Admin HQ - `/hq`)
관리자가 상품을 동기화하고, AI 이미지를 만들고 검수하는 **보안 구역(SaaS Style)**입니다.

#### 1. 카페24 상품 연동 관리 (Product Management)
> **프론트엔드 경로**: `/hq/products`
*   `GET /api/admin/products` : 전체/카테고리별 상품 목록 및 재고, 숨김 처리 상태 반환.
*   `POST /api/admin/products/sync` : 카페24 등 외부 API를 찔러서 최신 재고/상품을 DB로 동기화(Sync)하는 트리거.
*   `PATCH /api/admin/products/{id}/status` : 관리자가 수동으로 특정 상품을 어드민에서 숨김(`HIDDEN`) 처리할 때 사용.

#### 2. 수동 AI 룩북 캔버스 (Generator Workspace)
> **프론트엔드 경로**: `/hq/generator`
*   **`POST /api/admin/generate` (가장 핵심적인 API)**
    *   **요청(Req)**: `prompt`(분위기 묘사), `ratio`(비율), `referenceImage`(참고용), `productIds`(옷 데이터)
    *   **설명**: 프론트에서 넘어온 텍스트와 레퍼런스를 Python/Gemini 엔진에 전달하여 실제 룩북 이미지를 생성.
    *   **응답(Res)**: 생성 완료된 이미지 URL (로컬 폴더의 `/public/images/` 경로 형태도 무관함).

#### 3. 룩북 큐레이션 및 승인 (Curation Dashboard)
> **프론트엔드 경로**: `/hq/curation`
*   `GET /api/admin/curations?status=PENDING` : 제너레이터에서 방금 만들어져서 승인 대기 중인 룩북 목록.
*   `PATCH /api/admin/curations/{lookbook_id}`
    *   **설명**: 관리자가 화면 우측의 검수 패널에서 `APPROVE`(승인) 혹은 `REJECT`(보류) 버튼을 누를 때 룩북의 상태를 변경. 승인되는 즉시 파트 A(사용자 메인 피드)에 노출됨.

---

## 4. 아키텍처 및 이미지 저장 구조 제안서 (Architecture Note)

만약 AWS(S3, CloudFront) 구축 전, **로컬 폴더 수준의 MVP**만 빠르게 구성하시려면 다음 방식을 추천합니다.

1. **Public 폴더 공유 방식 (가장 간편함)**
2. 
    *   백엔드 스크립트가 AI 룩북 이미지를 생성하면, 그 파일을 이 프론트엔드 프로젝트 내부의 **`public/generated_images/`** 라우트로 곧바로 저장해주세요.
    *   그 후, 백엔드 DB의 `imageUrl` 컬럼에는 도메인 없이 `/generated_images/lookbook_001.webp` 구조로만 넘겨주시면 프론트엔드에서 곧장 렌더링이 가능합니다.
2. **보안 및 라우팅 (Route Guard)**
    *   프론트엔드 레포지토리에 `middleware.ts`(또는 `proxy.ts`)를 작성해 유저의 '/hq' 접근을 차단해두었습니다.
    *   백엔드에서 로그인 시 **`next-auth.session-token`** 명칭을 가진 HttpOnly 쿠키(Cookie)를 내려주시거나, 롤 기반(Role-based) 토큰을 내려주시면 됩니다.

끝으로 백엔드 개발에 돌입하시다 API 명세에 필드 추가가 필요하시거나 프론트엔드 동작을 수정해야 하면 편하게 알려주시기 바랍니다!
