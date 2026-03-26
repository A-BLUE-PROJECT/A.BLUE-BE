# ALLBLUE 프론트엔드 진행 상황 및 백엔드 연동 요구사항 (전달용)

## 1. 프론트엔드 완료 사항 (UI/UX 및 기능)
현재 프론트엔드 측 UI 컴포넌트 및 기본 라우팅 구현이 대부분 완료되었으며, 주요 데이터는 임시 더미(Mock) 데이터를 사용하여 화면을 구성하고 있습니다.

### 1-1. 코어 레이아웃 & 네비게이션
- **GNB (Global Navigation Bar)**: 매거진 스타일(좌측 로고, 우측 로그인/메뉴 배치) 및 스크롤 시 Glassmorphism 안티그래비티 효과 적용 완료.
- **인증 UI**: 우측 헤더 'LOG IN' 클릭 시 페이지 이동 없이 부드럽게 나타나는 반투명 소셜 로그인 모달 구현 (상태 관리: Zustand).
- **어드민 라우트 가드 (Route Guard)**: Next.js Middleware를 활용하여 `/admin` 하위 경로 접근 시 비로그인/권한 없는 유저를 메인 페이지(`/`)로 리다이렉션 처리 완료.

### 1-2. 메인 페이지 (Landing & Gallery)
- **비주얼 인터랙션**: 매거진 스타일의 하이콘트라스트, Anti-gravity(무중력) 갤러리 테마 스크롤 애니메이션 및 타이포그래피 마스킹 적용 완료.
- **갤러리 피드**: 비대칭(Asymmetric) 그리드 레이아웃을 통한 A.BLUE 룩북 갤러리 구현.
- **AI 시너지 뱃지**: 룩북 스코어링을 시각적으로 나타내는 궤도(Orbit) 애니메이션 뱃지 적용.

### 1-3. 상세 보기 (Quick View)
- 갤러리 아이템 클릭 시 화면 전환 없이 룩북 상세 정보를 볼 수 있는 **Quick View 모달** 퍼블리싱 완료.
- 내부 구성 요소: 룩북 메인 이미지, AI 핏 스코어, 연관 카페24 상품 리스트(의류/액세서리 등) UI 구현 완료.

### 1-4. 어드민 (Admin Curation Dashboard)
- **큐레이션 라우트 분리**: 유저 갤러리와 어드민 큐레이션 페이지 라우트 명확히 분리.
- **대시보드 UI**: AI가 생성한 룩북 목록(PENDING 상태)을 확인하고, 갤러리 노출 여부(APPROVED / REJECTED)를 검수하는 관리자 전용 대시보드 퍼블리싱 완료.

---

## 2. API 연동 필요 목록 (To-Do for Backend)
프론트엔드에서 현재 더미 데이터로 처리하고 있는 부분들로, 백엔드 API 연동이 필요한 핵심 기능 목록입니다.

### 2-1. 사용자 및 인증 (Auth)
- **[GET / POST] 소셜 로그인 API** (카카오, 구글, 네이버 등 지원 플랫폼 확정 필요)
- **[POST] JWT 토큰 갱신 (Refresh Token) API**
- **[GET] 유저 정보 조회 API** (권한 확인 - ADMIN / USER 등급 판별)

### 2-2. 갤러리 및 룩북 (Gallery & Lookbook)
- **[GET] 메인 갤러리 피드 API**:
    - 승인된(APPROVED) 룩북 리스트 조회 (페이지네이션 또는 무한 스크롤(커서/오프셋) 지원 필요)
- **[GET] 룩북 상세 조회 API** (Quick View 모달용):
    - *Response 요구 항목 예시*: 룩북 메인 이미지 URL, 타이틀, AI 스코어, 사용된 카페24 연동 상품 아이디 목록 등.

### 2-3. 어드민 큐레이션 (Admin)
- **[GET] 대기 중인 룩북 리스트 API**: 상태가 PENDING인 AI 생성 룩북 목록 조회
- **[PATCH / PUT] 룩북 상태 변경 API**: 선택한 룩북의 상태를 PENDING에서 APPROVED 또는 REJECTED로 변경
- **[POST] AI 룩북 생성 요청 API**: (필요 시) 어드민에서 특정 조건으로 룩북 생성을 수동 트리거

---

## 3. 프론트엔드 데이터 구조 설계 (참고용 Mock Interface)
현재 프론트엔드 컴포넌트에서 사용 중인 데이터 타입 인터페이스입니다. 백엔드 API 응답 설계 시 참고 부탁드립니다.

```typescript
// 룩북 데이터 모델 (Lookbook)
interface Lookbook {
  id: string;            // 룩북 고유 ID
  title: string;         // 룩북 타이틀
  imageUrl: string;      // 생성된 룩북 메인 이미지 URL
  aiScore: number;       // 0~100 (AI 시너지 뱃지 표기용)
  status: 'PENDING' | 'APPROVED' | 'REJECTED'; // 검수 상태
  createdAt: string;     // 생성일자 (ISO 8601 등)
  relatedProducts: Product[]; // 룩북에 포함된 카페24 상품 목록
}

// 카페24 연동 상품 모델 (Product)
interface Product {
  productId: string;     // 카페24 상품 ID
  name: string;          // 상품명
  price: number;         // 상품 가격
  thumbnailUrl: string;  // 상품 썸네일 이미지 URL
  detailUrl: string;     // 카페24 상품 상세 페이지 링크 URL
}

// 유저 정보 모델 (User)
interface User {
  id: string;
  name: string;
  profileImageUrl?: string;
  role: 'USER' | 'ADMIN';
}
```

---
**비고**:
- 위 명세는 프론트엔드 UI를 기준으로 초안 작성되었습니다.
- 백엔드 데이터베이스 설계나 성능 최적화(예: GraphQL, 응답 내려주기 방식 등) 과정에서 구조 변경이 필요하다면 언제든 조율 가능합니다.
- API 문서(Swagger, Postman, 또는 Notion 등)가 준비되거나 서버가 배포되면 엔드포인트 연동을 시작할 예정입니다.
