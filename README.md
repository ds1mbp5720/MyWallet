# SMS Ledger (SMS 가계부)

SMS 메시지를 자동으로 파싱하여 수입과 지출을 관리하는 스마트 가계부 어플리케이션입니다.

## 🚀 주요 기능

- **SMS 자동 파싱**: 수신된 SMS 메시지를 설정된 규칙에 따라 금액, 상점명, 유형(수입/지출)으로 자동 변환합니다.
- **카테고리 관리**: 사용자 정의 카테고리를 생성, 수정, 삭제할 수 있으며, 삭제 시 기존 내역을 안전하게 재분류합니다.
- **수입/지출 추적**: 모든 내역을 수입과 지출로 구분하여 관리하며, 직관적인 컬러 코딩(수입: 파랑, 지출: 빨강)을 제공합니다.
- **월별 요약 및 통계**: 월별 총 수입, 총 지출, 합계를 한눈에 확인하고 카테고리별 소비 패턴을 분석할 수 있습니다.
- **실시간 필터링**: 카테고리별로 내역을 필터링하여 특정 분야의 소비를 쉽게 추적할 수 있습니다.

## 🏗️ 프로젝트 아키텍처

본 프로젝트는 **Clean Architecture**와 **MVI (Model-View-Intent)** 패턴을 기반으로 설계되어 유지보수성과 확장성이 뛰어납니다.

### Android 모듈 구조
- **`:app`**: 어플리케이션의 엔트리 포인트 및 의존성 주입(DI), SMS 수신 서비스(`SmsReceiver`)를 포함합니다.
- **`:domain`**: 비즈니스 로직의 핵심인 모델(`model`), 저장소 인터페이스(`repository`), 유스케이스(`usecase`)를 포함하며 외부 라이브러리에 의존하지 않습니다.
- **`:data`**: 데이터 소스와의 상호작용을 담당합니다. Room DB를 이용한 로컬 저장소(`local`)와 저장소 구현체(`repository`)를 포함합니다.
- **`:feature:ledger`**: 가계부의 주요 UI와 상태 관리를 담당합니다. Jetpack Compose 기반의 `LedgerScreen`과 `LedgerViewModel`을 포함합니다.

### Web Preview (React)
- **Vite + React + Tailwind CSS**: 어플리케이션의 주요 기능을 웹에서 미리 보고 관리할 수 있는 인터페이스를 제공합니다.

## 📂 디렉토리 구조

```text
/
├── app/                # Android 앱 모듈 (SmsReceiver, App Entry)
├── data/               # 데이터 레이어 (Room DB, Repository Implementation)
├── domain/             # 도메인 레이어 (Models, UseCases, Repository Interface)
├── feature/            # 기능 레이어 (Ledger UI, ViewModel)
├── src/                # React 프론트엔드 소스 (Web Preview)
├── build.gradle.kts    # 프로젝트 빌드 설정
└── package.json        # 프론트엔드 의존성 및 스크립트
```

## 🛠️ 기술 스택

- **Android**: Kotlin, Jetpack Compose, Room, Coroutines, Flow
- **Frontend**: React, TypeScript, Vite, Tailwind CSS, Lucide React
- **Architecture**: Clean Architecture, MVI, Multi-module

## 📝 설치 및 실행

1. **Android**: Android Studio에서 프로젝트를 열고 `:app` 모듈을 실행합니다.
2. **Web Preview**: `npm install` 후 `npm run dev`를 실행하여 브라우저에서 확인합니다.

---
© 2026 SMS Ledger Project. All rights reserved.
