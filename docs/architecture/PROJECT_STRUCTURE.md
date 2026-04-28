# Project Structure

> 기준: `src/main/java/com/allblue/`
> 아키텍처: 도메인별 레이어드 아키텍처 (Presentation → Application → Domain → Infrastructure)

---

## 전체 트리

```
com.allblue/
├── AllblueApplication.java
│
├── admin/                          # 어드민 관리 도메인
│   ├── application/
│   │   ├── AdminAuthService.java
│   │   ├── AdminInspectionCommandService.java
│   │   ├── AdminInspectionFallbackServiceImpl.java
│   │   ├── AdminInspectionQueryService.java
│   │   └── dto/
│   │       ├── command/
│   │       │   ├── AdminLoginCommand.java
│   │       │   ├── InspectionCallbackCommand.java
│   │       │   └── InspectionStatusUpdateCommand.java
│   │       └── result/
│   │           ├── AdminLoginResult.java
│   │           └── ImageInspectionResult.java
│   ├── domain/
│   │   ├── exception/
│   │   │   ├── AdminBusinessException.java
│   │   │   └── AdminErrorCode.java
│   │   ├── model/
│   │   │   ├── Admin.java
│   │   │   ├── AdminRole.java
│   │   │   ├── ImageInspection.java
│   │   │   └── InspectionStatus.java
│   │   └── repository/
│   │       ├── AdminRepository.java
│   │       └── ImageInspectionRepository.java
│   ├── infrastructure/
│   │   ├── AdminRepositoryImpl.java
│   │   ├── ImageInspectionRepositoryImpl.java
│   │   └── jpa/
│   │       ├── AdminJpaRepository.java
│   │       └── ImageInspectionJpaRepository.java
│   ├── presentation/
│   │   ├── controller/
│   │   │   ├── AdminAuthApi.java
│   │   │   ├── AdminAuthController.java
│   │   │   ├── AdminInspectionApi.java
│   │   │   ├── AdminInspectionController.java
│   │   │   ├── InternalInspectionApi.java
│   │   │   └── InternalInspectionController.java
│   │   ├── request/
│   │   │   ├── AdminLoginRequest.java
│   │   │   ├── InspectionCallbackRequest.java
│   │   │   └── InspectionStatusUpdateRequest.java
│   │   └── response/
│   │       ├── AdminResultCode.java
│   │       └── ImageInspectionResponse.java
│   └── security/
│       ├── AdminJwtAuthenticationFilter.java
│       ├── AdminJwtTokenProvider.java
│       ├── AdminSecurityConfig.java
│       └── AdminUserDetails.java
│
├── auth/                           # 사용자 인증 도메인 (OAuth2 + JWT)
│   ├── application/
│   │   ├── AuthCommandService.java
│   │   ├── CustomOAuth2UserService.java
│   │   ├── command/
│   │   │   └── TokenRefreshCommand.java
│   │   └── dto/result/
│   │       └── TokenRefreshResult.java
│   ├── domain/
│   │   ├── exception/
│   │   │   ├── AuthBusinessException.java
│   │   │   └── AuthErrorCode.java
│   │   ├── model/
│   │   │   └── RefreshToken.java
│   │   └── repository/
│   │       └── RefreshTokenRepository.java
│   ├── infrastructure/
│   │   └── redis/
│   │       └── RefreshTokenRepositoryImpl.java
│   ├── jwt/
│   │   ├── JwtTokenProvider.java
│   │   └── filter/
│   │       └── JwtAuthenticationFilter.java
│   └── presentation/
│       ├── controller/
│       │   ├── AuthApi.java
│       │   └── AuthController.java
│       ├── response/
│       │   └── AuthResultCode.java
│       └── util/
│           └── CookieUtil.java
│
├── category/                       # 카테고리 도메인
│   ├── application/
│   │   ├── CategoryCommandService.java
│   │   ├── CategoryQueryService.java
│   │   ├── command/
│   │   │   ├── CreateCategoryCommand.java
│   │   │   └── UpdateCategoryNameCommand.java
│   │   └── dto/
│   │       └── CategoryListResult.java
│   ├── domain/
│   │   ├── exception/
│   │   │   ├── CategoryBusinessException.java
│   │   │   └── CategoryErrorCode.java
│   │   ├── model/
│   │   │   └── Category.java
│   │   └── repository/
│   │       └── CategoryRepository.java
│   ├── infrastructure/
│   │   ├── CategoryRepositoryImpl.java
│   │   └── jpa/
│   │       └── CategoryJpaRepository.java
│   └── presentation/
│       ├── controller/
│       │   ├── CategoryCommandApi.java
│       │   ├── CategoryCommandController.java
│       │   ├── CategoryQueryApi.java
│       │   └── CategoryQueryController.java
│       ├── request/
│       │   ├── CreateCategoryRequest.java
│       │   └── UpdateCategoryNameRequest.java
│       └── response/
│           ├── CategoryListResponse.java
│           ├── CategoryResultCode.java
│           └── CreateCategoryResponse.java
│
├── common/                         # 공통 유틸리티
│   ├── config/
│   │   ├── AsyncConfig.java
│   │   ├── DataInitializer.java        # 더미 데이터 시드 (local/dev)
│   │   ├── OpenApiConfig.java
│   │   ├── RedisConfig.java
│   │   ├── RestTemplateConfig.java
│   │   ├── SchedulingConfig.java
│   │   └── WebMvcConfig.java           # 로컬 이미지 정적 리소스 서빙
│   ├── entity/
│   │   └── BaseTimeEntity.java
│   ├── error/
│   │   ├── BusinessException.java
│   │   ├── ErrorCode.java
│   │   ├── ErrorResponse.java
│   │   ├── GlobalErrorCode.java
│   │   └── GlobalExceptionHandler.java
│   ├── filter/
│   │   └── MdcLoggingFilter.java
│   ├── lock/
│   │   ├── AopForTransaction.java
│   │   ├── CustomSpringELParser.java
│   │   ├── DistributedLock.java
│   │   └── DistributedLockAop.java
│   ├── response/
│   │   ├── ApiResponse.java
│   │   ├── PageResponse.java
│   │   └── ResultCode.java
│   ├── scheduler/
│   │   └── CrawlScheduler.java
│   ├── swagger/
│   │   ├── ApiErrorExceptions.java
│   │   └── ApiErrorExceptionsCustomizer.java
│   └── worker/
│       ├── InspectionFallbackHandler.java
│       ├── InspectionWorkerClient.java
│       ├── InspectionWorkerClientImpl.java
│       └── dto/
│           └── CardImageInspectionPayload.java
│
├── global/
│   └── config/
│       └── HibernateFilterAspect.java
│
├── lookbook/                       # 룩북 도메인
│   ├── application/
│   │   ├── AiWorkerClient.java         # AI 워커 인터페이스
│   │   ├── LookbookAiPipelineService.java
│   │   ├── LookbookCommandService.java
│   │   ├── LookbookQueryService.java
│   │   └── dto/
│   │       ├── AiWorkerPayload.java
│   │       ├── command/
│   │       │   ├── LookbookCompleteCommand.java
│   │       │   └── LookbookGenerateCommand.java
│   │       ├── query/
│   │       │   └── LookbookSearchQuery.java
│   │       └── result/
│   │           ├── LookbookDetailResult.java
│   │           └── LookbookResult.java
│   ├── domain/
│   │   ├── exception/
│   │   │   ├── LookbookBusinessException.java
│   │   │   └── LookbookErrorCode.java
│   │   ├── model/
│   │   │   ├── Lookbook.java
│   │   │   ├── LookbookImage.java
│   │   │   ├── LookbookItem.java
│   │   │   └── enums/
│   │   │       ├── LookbookStatus.java
│   │   │       ├── Position.java
│   │   │       ├── Season.java
│   │   │       ├── StyleType.java
│   │   │       └── TargetGender.java
│   │   └── repository/
│   │       ├── LookbookImageRepository.java
│   │       ├── LookbookItemRepository.java
│   │       └── LookbookRepository.java
│   ├── infrastructure/
│   │   ├── LookbookImageRepositoryImpl.java
│   │   ├── LookbookItemRepositoryImpl.java
│   │   ├── LookbookRepositoryImpl.java
│   │   ├── client/
│   │   │   └── AiWorkerClientImpl.java  # n8n Webhook HTTP 구현체
│   │   └── jpa/
│   │       ├── LookbookImageJpaRepository.java
│   │       ├── LookbookItemJpaRepository.java
│   │       └── LookbookJpaRepository.java
│   └── presentation/
│       ├── controller/
│       │   ├── AdminLookbookApi.java
│       │   ├── AdminLookbookController.java    # /adm/v1/lookbooks
│       │   ├── InternalLookbookApi.java
│       │   ├── InternalLookbookController.java # /i/v1/lookbooks
│       │   ├── LookbookApi.java
│       │   └── LookbookController.java         # /w/v1/lookbooks
│       ├── request/
│       │   ├── LookbookCompleteRequest.java
│       │   └── LookbookGenerateRequest.java
│       └── response/
│           ├── LookbookDetailResponse.java
│           ├── LookbookResponse.java
│           └── LookbookResultCode.java
│
├── product/                        # 상품 도메인
│   ├── application/
│   │   ├── ProductCommandService.java
│   │   ├── ProductQueryService.java
│   │   └── dto/
│   │       ├── command/
│   │       │   └── ProductBatchCreateCommand.java
│   │       └── result/
│   │           └── ProductResult.java
│   ├── domain/
│   │   ├── exception/
│   │   │   ├── ProductBusinessException.java
│   │   │   └── ProductErrorCode.java
│   │   ├── model/
│   │   │   ├── Product.java
│   │   │   └── enums/
│   │   │       ├── MappedCategory.java
│   │   │       └── StockStatus.java
│   │   └── repository/
│   │       └── ProductRepository.java
│   ├── infrastructure/
│   │   ├── ProductRepositoryImpl.java
│   │   └── jpa/
│   │       └── ProductJpaRepository.java
│   └── presentation/
│       ├── controller/
│       │   ├── AdminProductApi.java
│       │   ├── AdminProductController.java     # /adm/v1/products
│       │   ├── InternalProductApi.java
│       │   └── InternalProductController.java  # /i/v1/products
│       ├── request/
│       │   ├── ProductBatchCreateRequest.java
│       │   └── ProductHiddenRequest.java
│       └── response/
│           ├── ProductResponse.java
│           └── ProductResultCode.java
│
├── security/                       # B2C 보안 설정
│   ├── config/
│   │   └── SecurityConfig.java
│   └── oauth2/
│       ├── CustomUserDetails.java
│       ├── OAuth2UserInfoFactory.java
│       ├── dto/
│       │   ├── ErrorQueryParam.java
│       │   ├── GoogleOAuth2UserInfo.java
│       │   ├── KakaoOAuth2UserInfo.java
│       │   └── OAuth2UserInfo.java
│       ├── exception/
│       │   ├── CustomOAuth2Exception.java
│       │   └── OAuth2ErrorCode.java
│       └── handler/
│           ├── OAuth2ErrorMapper.java
│           ├── OAuth2FailureHandler.java
│           └── OAuth2SuccessHandler.java
│
├── seller/                         # 셀러(카페24 연동) 도메인
│   ├── application/
│   │   ├── SellerOAuthService.java
│   │   └── dto/result/
│   │       └── SellerResult.java
│   ├── domain/
│   │   ├── exception/
│   │   │   ├── SellerBusinessException.java
│   │   │   └── SellerErrorCode.java
│   │   ├── model/
│   │   │   └── Seller.java
│   │   └── repository/
│   │       └── SellerRepository.java
│   ├── infrastructure/
│   │   ├── SellerRepositoryImpl.java
│   │   ├── cafe24/
│   │   │   ├── Cafe24ApiClient.java
│   │   │   ├── Cafe24RestClientConfig.java
│   │   │   └── dto/
│   │   │       └── Cafe24TokenResponse.java
│   │   └── jpa/
│   │       └── SellerJpaRepository.java
│   └── presentation/
│       ├── controller/
│       │   ├── SellerOAuthApi.java
│       │   └── SellerOAuthController.java
│       └── response/
│           ├── SellerResponse.java
│           └── SellerResultCode.java
│
└── user/                           # 사용자 도메인
    ├── application/
    │   ├── UserCommandService.java
    │   ├── UserQueryService.java
    │   ├── command/
    │   │   ├── UserCreateCommand.java
    │   │   └── UserProfileUpdateCommand.java
    │   └── dto/result/
    │       └── UserInfoResult.java
    ├── domain/
    │   ├── exception/
    │   │   ├── UserBusinessException.java
    │   │   └── UserErrorCode.java
    │   ├── model/
    │   │   ├── Profile.java
    │   │   ├── User.java
    │   │   └── enums/
    │   │       ├── Gender.java
    │   │       ├── Provider.java
    │   │       ├── Role.java
    │   │       └── UserStatus.java
    │   └── repository/
    │       ├── ProfileRepository.java
    │       └── UserRepository.java
    ├── infrastructure/
    │   ├── ProfileRepositoryImpl.java
    │   ├── UserRepositoryImpl.java
    │   └── jpa/
    │       ├── ProfileJpaRepository.java
    │       └── UserJpaRepository.java
    └── presentation/
        ├── controller/
        │   ├── UserCommandApi.java
        │   ├── UserCommandController.java
        │   ├── UserQueryApi.java
        │   └── UserQueryController.java
        ├── request/
        │   └── UserProfileUpdateRequest.java
        └── response/
            ├── UserInfoResponse.java
            └── UserResultCode.java
```

---

## 레이어 역할 정리

| 레이어 | 패키지 | 역할 |
|---|---|---|
| Presentation | `presentation/controller/` | HTTP 요청/응답 처리. 비즈니스 로직 없음 |
| Presentation | `presentation/request/` | 요청 DTO (`@Valid` 검증, `toCommand()` 포함) |
| Presentation | `presentation/response/` | 응답 DTO (`from()` 포함), ResultCode Enum |
| Application | `application/*Service.java` | 비즈니스 유스케이스 조합 |
| Application | `application/dto/command/` | 서비스 입력 DTO |
| Application | `application/dto/result/` | 서비스 출력 DTO |
| Application | `application/dto/query/` | 조회 조건 DTO |
| Domain | `domain/model/` | 엔티티, 값 객체, 비즈니스 메서드 |
| Domain | `domain/model/enums/` | 도메인 Enum |
| Domain | `domain/repository/` | 레포지토리 인터페이스 |
| Domain | `domain/exception/` | 도메인 예외, 에러코드 |
| Infrastructure | `infrastructure/*RepositoryImpl.java` | 레포지토리 구현체 |
| Infrastructure | `infrastructure/jpa/` | Spring Data JPA 인터페이스 |
| Infrastructure | `infrastructure/client/` | 외부 HTTP 클라이언트 구현체 |

---

## 컨트롤러 URL prefix 규칙

| prefix | 대상 | 예시 |
|---|---|---|
| `/w/v1/` | B2C 사용자 (로그인 필요) | `GET /w/v1/lookbooks` |
| `/adm/v1/` | 어드민 (Admin JWT 필요) | `POST /adm/v1/lookbooks/generate` |
| `/i/v1/` | 내부 서버 간 통신 (AI Worker → Backend) | `POST /i/v1/lookbooks/{id}/complete` |
| `/s/v1/` | 셀러 OAuth (카페24 연동) | `GET /s/v1/oauth/callback` |

---

## 공통 패턴

### DTO 명명 규칙

| 종류 | 위치 | 예시 |
|---|---|---|
| Command | `application/dto/command/` | `LookbookGenerateCommand` |
| Result | `application/dto/result/` | `LookbookResult`, `LookbookDetailResult` |
| Query | `application/dto/query/` | `LookbookSearchQuery` |
| Request | `presentation/request/` | `LookbookGenerateRequest` |
| Response | `presentation/response/` | `LookbookResponse`, `LookbookDetailResponse` |

### ResultCode 코드 형식

`[S/E][도메인약어][HTTP상태코드][순번 2자리]`

| 도메인 | 약어 | 성공 예 | 에러 예 |
|---|---|---|---|
| Lookbook | LB | `SLB20001` | `ELB40401` |
| Product | PD | `SPD20001` | `EPD40401` |
| Admin | AD | `SAD20001` | `EAD40001` |
| Auth | AT | `SAT20001` | `EAT40101` |
| User | US | `SUS20001` | `EUS40001` |
| Seller | SL | `SSL20001` | `ESL40001` |
| Category | CT | `SCT20001` | `ECT40001` |

---

## 리소스

```
src/main/resources/
├── application.yml     # 전체 공통 설정 (DB, Redis, JWT, n8n, 이미지 경로 등)
└── logback-spring.xml  # 로깅 설정
```

---

## 테스트

```
src/test/java/com/allblue/
├── AllblueApplicationTests.java
└── admin/
    └── application/
        └── AdminAuthServiceTest.java
```
