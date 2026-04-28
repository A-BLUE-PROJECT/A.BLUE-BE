# PLAN: 상태 관리 전략

ALLBLUE는 유저 개인화(핏 점수)와 탐색 경험이 중요하므로, 다음과 같은 상태 관리 전략을 사용합니다.

## 1. 전역 상태 (Global State)

### Auth Context
- **도구**: NextAuth.js 또는 자체 Context API
- **관리 항목**: JWT 액세스 토큰, 유저 기본 정보, 로그인 여부.

### Filter/Preference State
- **도구**: Zustand
- **관리 항목**: 현재 선택된 스타일 필터(Casual, Street 등), 성별 필터.

## 2. 서버 상태 (Server State)
- **도구**: TanStack Query (React Query) v5
- **이유**: 캐싱, 무한 스크롤(useInfiniteQuery), 데이터 동기화 최적화.
- **주요 쿼리**:
  - `useLookbooks`: 메인 피드 데이터 (무한 스크롤)
  - `useFitProfile`: 유저 체형 데이터 조회 및 업데이트

## 3. 로컬 상태 (Local State)
- **도구**: `useState`, `useOptimistic` (Next.js 15)
- **용도**: 스와이프 애니메이션 상태, 모달 열림 상태, 폼 입력값.
