# PLAN: 컴포넌트 라이브러리 및 UI 구조

[cite_start]ALLBLUE(올블루) 프론트엔드는 압도적인 비주얼의 'AI 패션 유니버스'를 구현하기 위해 Shadcn/UI와 Framer Motion을 기반으로 한 **안티그래비티(Anti-gravity)** 디자인 시스템을 채택합니다. [cite: 38]

## 1. 디자인 시스템 원칙

* **Anti-gravity (무중력)**: 모든 UI 요소는 물리적인 중력에서 벗어난 듯한 부유감을 제공하며, 레이어 간의 깊이감(Depth)을 극대화합니다.
* [cite_start]**Minimal & High-Contrast**: 고해상도 AI 룩북 이미지가 돋보이도록 무채색 배경과 강렬한 타이포그래피 대비를 활용합니다. [cite: 58]
* **Runway Magazine Layout**: 정형화된 그리드를 탈피하여 잡지 화보와 같은 불규칙한 그리드(Asymmetric Grid)와 대형 타이포그래피 마스킹을 사용합니다.
* [cite_start]**Mobile-First Interaction**: 스와이프를 통한 탐색 경험을 최우선으로 설계하되, 데스크톱에서도 부드러운 드래그 인터랙션을 보장합니다. [cite: 21]

## 2. 주요 컴포넌트 분류

### 2.1. 패션 오케스트레이션 컴포넌트 (Domain Specific)

* [cite_start]**Lookbook Anti-gravity Card**: AI가 합성한 고화질 코디 이미지를 배경으로 하며, 스크롤 시 이미지 내 요소들이 서로 다른 속도로 움직이는 패럴랙스 효과를 적용합니다. [cite: 24, 50]
* **Floating Product Chip**: 룩북 내 상품 위치에 고정되지 않고 미세하게 떠다니는 정보 태그입니다. [cite_start]클릭 시 카페24 실시간 상품 정보와 딥링크를 노출합니다. [cite: 26, 53]
* [cite_start]**Fit Score Orbit Badge**: 유저의 `FitProfile`에 근거한 적합도를 보여주는 배지로, 모델 주위를 공전하는 듯한 애니메이션을 통해 시각적 재미를 줍니다. [cite: 13, 79]
* [cite_start]**Swipe Runway Container**: 틴더 스타일의 인터랙션을 확장하여, 카드가 날아갈 때 가벼운 무중력 물리 엔진이 적용된 애니메이션을 제공합니다. [cite: 21]

### 2.2. 운영자 전용 UI (Admin/Curation)

* [cite_start]**Lookbook Inspector**: Gemini API로 생성된 `PENDING` 상태의 룩북들을 한눈에 확인하고 품질을 검수하는 대시보드입니다. [cite: 48, 49]
* [cite_start]**Approval Trigger**: 퀄리티가 확보된 룩북을 클릭 한 번으로 `APPROVED` 상태로 변경하여 메인 갤러리에 즉시 송출하는 버튼입니다. [cite: 50]
* [cite_start]**Tagging Interface**: AI가 생성한 조합 내 상품들을 카페24 상품 데이터와 매칭하거나 위치를 보정하는 관리 도구입니다. [cite: 45, 58]

### 2.3. 공통 UI (Base)

* **Typography System**: 브랜드 아이덴티티를 드러내는 대형 헤드라인 폰트와 가독성 높은 본문용 폰트 체계입니다.
* **Glassmorphism Modals**: 배경이 비치는 반투명한 레이어를 사용하여 모든 팝업이 공중에 떠 있는 듯한 느낌을 유지합니다.
* **Skeleton Post-processing**: 데이터 로딩 시 단순한 박스가 아닌, 룩북의 실루엣이 부드럽게 나타나는 애니메이션 플레이스홀더를 사용합니다.

## 3. 기술 스택 요약

* [cite_start]**Framework**: Next.js 15 (App Router) [cite: 38]
* [cite_start]**Styling**: Tailwind CSS & Lucide React (Icons) [cite: 38]
* **Components**: Shadcn/UI (Radix UI 기반)
* [cite_start]**Animation**: Framer Motion (무중력 및 패럴랙스 효과 구현 핵심) [cite: 38]