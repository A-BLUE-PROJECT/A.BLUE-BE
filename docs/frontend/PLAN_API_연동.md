# PLAN: API 연동 및 데이터 통신

백엔드 서버와의 안정적인 통신을 위한 클라이언트 구조 및 에러 처리 전략입니다.

## 1. API 클라이언트 설정
- **도구**: Axios
- **설정**:
  - `baseURL`: `process.env.NEXT_PUBLIC_API_URL`
  - **Request Interceptor**: 모든 요청에 JWT 토큰 자동 주입.
  - **Response Interceptor**: 401 에러 시 리프레시 토큰 시도 또는 로그아웃 처리.

## 2. 데이터 페칭 패턴
- **Server Components (Next.js)**: SEO가 필요한 상세 페이지나 공지사항 조회.
- **Client Components + React Query**: 갤러리 피드, 유저 커스텀 작업 등 실시간 인터랙션.

## 3. 에러 핸들링
- **ApiResponse Wrapper**: 백엔드 `ResultCode`에 따른 UI 대응 (예: `ELB40401` -> 토스트 메시지 표시).
- **Zod Validation**: API 응답 데이터 타입을 런타임에서 검증하여 타입 안정성 확보.

## 4. 보안
- **CORS**: 허용된 도메인 설정 확인.
- **HttpOnly Cookie**: 리프레시 토큰 관리.
