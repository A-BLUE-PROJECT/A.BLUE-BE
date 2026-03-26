# PLAN: 개발 단계 및 로드맵

[cite_start]ALLBLUE(올블루) 프론트엔드 구축을 위한 4단계 마일스톤입니다. [cite: 9] [cite_start]이 로드맵은 기존 DEKK의 카드 중심 구조를 탈피하여 카페24 API 연동 및 AI 룩북 중심의 오케스트레이션 플랫폼으로 전환하는 과정을 담고 있습니다. [cite: 8, 57]

---

## Phase 1: Foundation (1주)
- [ ] **프로젝트 스캐폴딩**: Next.js 15 (App Router), TypeScript, Tailwind CSS 초기 설정
- [ ] **디자인 시스템 기초**: Shadcn/UI 설치 및 **안티그래비티(무중력)** 테마 커스터마이징 (레이어 깊이감 및 부유 효과 정의)
- [ ] [cite_start]**API 클라이언트 인프라**: Axios 설정 및 카페24 OAuth 2.0 연동 에러 핸들러 구현 [cite: 54]
- [ ] **공통 레이아웃**: 매거진 스타일의 글로벌 Header, 사이드바, 및 대형 타이포그래피 기반 레이아웃 구축

## Phase 2: Core Feature - Gallery & Admin (2-3주)
- [ ] [cite_start]**메인 룩북 갤러리 피드**: AI가 생성한 크로스 셀러 코디 조합을 탐색하는 무한 스크롤 피드 구현 [cite: 8, 20]
- [ ] [cite_start]**안티그래비티 인터랙션**: Framer Motion을 활용하여 룩북 카드가 중력을 거스르는 듯한 부유 효과 및 스와이프 애니메이션 적용 [cite: 21]
- [ ] [cite_start]**운영자 전용 관리 화면 (Admin)**: Gemini API로 생성된 `PENDING` 상태의 룩북 이미지를 검수하고 갤러리에 노출할 `APPROVED` 상태로 전환하는 내부 승인 시스템 구축 [cite: 47, 51]
- [ ] [cite_start]**상품 퀵뷰(Quick View)**: 룩북 내 개별 상품 클릭 시 카페24 API를 통해 실시간 재고 및 상품 정보를 노출하고 원본몰 딥링크 연결 [cite: 26, 55]

## Phase 3: Personalization & Auth (4주)
- [ ] **소셜 로그인**: NextAuth.js 기반의 소셜 로그인 및 회원가입 연동 (JWT 인증 체계)
- [ ] [cite_start]**유저 핏 프로필(FitProfile)**: 키, 몸무게, 어깨너비 등 유저 체형 데이터 입력 및 관리 페이지 구현 [cite: 58, 72]
- [ ] [cite_start]**룩북 상세 및 핏 스코어**: 등록된 `FitProfile`과 상품 실측 데이터를 비교하여 개인별 **Fit Score(매칭률)** 시각화 배지 개발 [cite: 13, 79]

## Phase 4: Visual Polish & Performance (5주+)
- [ ] **매거진 레이아웃 고도화**: 런웨이 컨셉의 대형 타이포그래피 마스킹 및 불규칙한 그리드(Asymmetric Grid) 레이아웃 완성
- [ ] [cite_start]**성능 최적화**: 고해상도 AI 합성 이미지의 WebP 변환 렌더링 최적화 및 LCP(Largest Contentful Paint) 개선 [cite: 41]
- [ ] [cite_start]**최종 QA**: 카페24 실시간 상품 동기화 및 결제 이동 경로(Saga 패턴 등) 최종 테스트 [cite: 53, 78]
- [ ] [cite_start]**상용 환경 배포**: AWS S3 및 CloudFront 기반의 고성능 이미지 서빙 및 자동 배포 파이프라인 가동 [cite: 38]