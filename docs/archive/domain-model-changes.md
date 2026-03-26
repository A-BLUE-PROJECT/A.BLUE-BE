# ALLBLUE 도메인 모델 전환 설계 (Domain Model Changes)

## 1. 개요
기존 **DEKK** 프로젝트의 `Card` 중심 모델에서 **ALLBLUE**의 `Lookbook` 및 `Product` 중심 모델로 전환합니다.

## 2. 주요 도메인 매핑 테이블
| DEKK (기존) | ALLBLUE (신규) | 전환 사유 및 변경점 |
| :--- | :--- | :--- |
| **Card** | **Lookbook** | AI가 생성한 완성된 코디 조합 단위로 변경. |
| **CardProduct** | **LookbookItem** | 룩북에 포함된 개별 상품 참조 엔티티로 변경. |
| **Product (부속)** | **Product (독립)** | 상품을 독립적인 핵심 엔티티로 승격하여 관리. |
| **CardImage** | **LookbookImage** | AI로 합성된 완성 이미지를 저장. |
| **(없음)** | **Seller** | 카페24 상점 등 입점 쇼핑몰 정보 관리. |
| **(없음)** | **FitProfile** | 사용자 체형 정보 관리 및 핏 점수 계산용. |
| **(없음)** | **Order / Payment** | 통합 결제를 위한 주문 및 결제 도메인 추가. |

## 3. 엔티티 상세 변경 (Entity Refactoring)

### 3-1. Lookbook (룩북)
- `LookbookId` (PK)
- `StyleType` (Casual, Formal, etc.)
- `Season` (Spring, Summer, etc.)
- `LookbookImage` (합성된 이미지 URL)
- `LookbookStatus` (PENDING, COMPLETED, FAILED)
- `CreatedAt`, `UpdatedAt`

### 3-2. Product (상품)
- `ProductId` (PK)
- `SellerId` (FK)
- `ExternalProductId` (원본 쇼핑몰 상품 ID)
- `Category` (TOP, BOTTOM, SHOES, etc.)
- `BrandName`
- `Price`, `SalePrice`
- `ProductImageUrl` (원본 이미지)
- `OriginUrl` (쇼핑몰 상세 페이지 링크)
- `StockStatus` (IN_STOCK, OUT_OF_STOCK)

### 3-3. LookbookItem (룩북 아이템)
- `LookbookItemId` (PK)
- `LookbookId` (FK)
- `ProductId` (FK)
- `Position` (상의/하의/신발 등 매칭 위치)
