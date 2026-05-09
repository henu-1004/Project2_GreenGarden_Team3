# 🌿 GreenGarden

BNK 부산은행 금융DT 아카데미 3조 미니 프로젝트로 진행한 **Spring Boot 기반 이커머스 웹 애플리케이션**입니다. 기존 Kmarket 프로젝트 기획을 바탕으로 사용자 쇼핑몰, 마이페이지, 고객센터, 관리자 시스템을 통합 구현했습니다.

> 기존 README의 Figma/Google Sheet 링크와 프로젝트 요구사항을 참고해 현재 저장소 구조와 실행 방식에 맞춰 정리했습니다.

---

## 📌 프로젝트 개요

GreenGarden은 일반 사용자와 관리자/판매자 관점의 기능을 함께 제공하는 쇼핑몰 서비스입니다.

- **일반 사용자**: 상품 조회, 검색, 장바구니, 주문/결제 흐름, 마이페이지, 고객센터 이용
- **회원 기능**: 일반 회원/판매자 회원 가입, 로그인, 자동 로그인, Google OAuth2 로그인, 아이디/비밀번호 찾기
- **관리자**: 상품·회원·주문·배송·쿠폰·공지/FAQ/Q&A·채용·약관 관리
- **운영 기능**: 이미지 업로드, 이메일 인증, 포인트/쿠폰/리뷰/교환/반품 관리

---

## 👥 팀원

| 역할 | 이름 |
| --- | --- |
| 팀장 | 서현우 |
| 팀원 | 박효빈, 이수연, 이종봉, 조지영, 한탁원 |

---

## 🔗 기획/협업 자료

- Kmarket Figma: <https://www.figma.com/design/amLy5z6BomEXjUgbAdnB8F/>
- 3조 Figma Board: <https://www.figma.com/board/6Jo7F300ovWIcDz6QiGuo4/3%EC%A1%B0_2%EC%B0%A8%EB%AF%B8%EB%8B%88%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8?node-id=0-1&t=rJgBD6yy6pGQASaU-1>
- 3조 Google Sheet: <https://docs.google.com/spreadsheets/d/19zL2yzpaBrGRmPTm4cGDgpbHTyGyVo3kn_RG8Y1trXk>

---

## 🛠 기술 스택

### Backend

- Java 17
- Spring Boot 3.5.6
- Spring MVC
- Spring Security 6
- Spring Data JPA
- MyBatis 3.0.5
- Lombok
- ModelMapper

### Frontend

- Thymeleaf
- HTML5 / CSS3 / JavaScript
- 서버 사이드 렌더링 기반 템플릿

### Database / Infra

- Oracle Database JDBC Driver
- MySQL Connector 포함
- HikariCP
- GitHub Actions CI/CD
- AWS EC2 배포 파이프라인

---

## ✨ 주요 기능

### 1. 사용자 쇼핑몰

- 메인 화면 및 카테고리 기반 상품 노출
- 상품 목록/상세/검색
- 장바구니 담기 및 장바구니 목록 조회
- 주문서 작성, 주문 처리, 주문 완료 페이지
- 판매자 정보 조회

### 2. 회원/인증

- 일반 회원 가입
- 판매자 회원 가입
- 로그인/로그아웃
- 자동 로그인(Remember-me)
- Google OAuth2 로그인
- 이메일 인증 코드 발송
- 아이디 찾기 및 비밀번호 재설정

### 3. 마이페이지

- 주문 내역 조회
- 주문 상세 조회
- 포인트 내역 조회
- 보유 쿠폰 조회
- 리뷰 관리
- 문의 내역 관리
- 개인정보 수정 및 회원 탈퇴
- 구매 확정, 주문 취소, 교환/반품 신청

### 4. 고객센터

- 공지사항 목록/상세
- FAQ 목록/상세
- Q&A 목록/상세/작성

### 5. 관리자 시스템

- 관리자 대시보드
- 기본 설정/배너/카테고리/약관/버전 관리
- 상점 목록 및 매출 현황
- 회원 목록, 회원 정보 수정, 포인트 관리
- 상품 목록, 상품 등록, 상품 삭제
- 주문 현황, 주문 상세, 배송 등록/조회
- 쿠폰 등록, 쿠폰 목록, 발급 현황, 발급 중지
- 공지사항/FAQ/Q&A CRUD 및 일괄 삭제
- 채용 공고 관리

---

## 🧭 주요 URL

> 기본 서버 설정 기준: `http://localhost:8080/greengarden`

### 사용자

| 기능 | URL |
| --- | --- |
| 메인 | `/`, `/index` |
| 로그인 | `/member/login` |
| 회원가입 선택 | `/member/join` |
| 일반 회원가입 | `/member/register` |
| 판매자 회원가입 | `/member/registerSeller` |
| 아이디 찾기 | `/find/userId` |
| 비밀번호 찾기 | `/find/password` |
| 상품 목록 | `/product/list` |
| 상품 상세 | `/product/view` |
| 장바구니 | `/product/cart` |
| 주문 | `/product/order` |
| 주문 완료 | `/product/complete` |
| 상품 검색 | `/product/search` |
| 고객센터 | `/cs` |
| 공지사항 | `/cs/notice/list` |
| FAQ | `/cs/faq/list` |
| Q&A | `/cs/qna/list` |
| 마이페이지 홈 | `/my/home` |
| 마이페이지 주문 | `/my/order` |
| 마이페이지 포인트 | `/my/point` |
| 마이페이지 쿠폰 | `/my/coupon` |
| 마이페이지 리뷰 | `/my/review` |
| 마이페이지 문의 | `/my/qna` |
| 마이페이지 설정 | `/my/info` |
| 회사소개 | `/company/index` |
| 서비스 정책 | `/policy`, `/policy/privacy` |

### 관리자

| 기능 | URL |
| --- | --- |
| 관리자 홈 | `/admin/` |
| 기본 설정 | `/admin/config/basic` |
| 배너 관리 | `/admin/config/banner` |
| 카테고리 관리 | `/admin/config/category` |
| 약관 관리 | `/admin/config/policy` |
| 버전 관리 | `/admin/config/version` |
| 상점 목록 | `/admin/shop/list` |
| 매출 현황 | `/admin/shop/sales` |
| 회원 목록 | `/admin/member/list` |
| 상품 목록 | `/admin/product/list` |
| 상품 등록 | `/admin/product/register` |
| 주문 목록 | `/admin/order/list` |
| 배송 현황 | `/admin/order/delivery` |
| 쿠폰 목록 | `/admin/coupon/list` |
| 쿠폰 발급 현황 | `/admin/coupon/issued` |
| 공지사항 관리 | `/admin/cs/notice/list` |
| FAQ 관리 | `/admin/cs/faq/list` |
| Q&A 관리 | `/admin/cs/qna/list` |
| 채용 관리 | `/admin/cs/recruit/list` |

---

## 📁 디렉터리 구조

```text
GreenGarden/
├── .github/workflows/          # GitHub Actions CI/CD 설정
├── gradle/                     # Gradle Wrapper 파일
├── src/
│   ├── main/
│   │   ├── java/kr/co/greengarden/
│   │   │   ├── config/         # MVC 설정, 공통 AppInfo, 필터
│   │   │   ├── controller/     # 사용자/관리자 화면 및 API 컨트롤러
│   │   │   ├── dto/            # 요청/응답/화면 전달 DTO
│   │   │   ├── entity/         # JPA 엔티티
│   │   │   ├── handler/        # 이미지 업로드 처리
│   │   │   ├── intercepter/    # 공통 인터셉터
│   │   │   ├── mapper/         # MyBatis Mapper 인터페이스
│   │   │   ├── repository/     # Spring Data JPA Repository
│   │   │   ├── security/       # Spring Security 설정 및 인증 사용자
│   │   │   ├── service/        # 비즈니스 로직
│   │   │   └── util/           # 페이지네이션 유틸
│   │   └── resources/
│   │       ├── mappers/        # MyBatis XML Mapper
│   │       ├── static/         # CSS, JS, 이미지 등 정적 리소스
│   │       ├── templates/      # Thymeleaf 템플릿
│   │       └── application.yml # 애플리케이션 설정
│   └── test/                   # 테스트 코드
├── uploads/                    # 업로드 파일 저장 디렉터리
├── build.gradle                # Gradle 빌드 설정
├── settings.gradle             # 프로젝트 이름 설정
└── README.md
```

---

## ⚙️ 실행 환경

- JDK 17 이상
- Gradle Wrapper 사용 가능 환경
- Oracle Database 접근 가능 환경
- SMTP 메일 계정 및 Google OAuth2 클라이언트 설정

> `application.yml`에는 데이터베이스, 메일, OAuth2 클라이언트, 업로드 경로 등 실행 환경 정보가 포함되어 있습니다. 운영/공유 환경에서는 민감 정보를 환경 변수 또는 별도 프로필로 분리하는 것을 권장합니다.

---

## 🚀 로컬 실행 방법

### 1. 저장소 클론

```bash
git clone <repository-url>
cd GreenGarden
```

### 2. 실행 권한 부여

```bash
chmod +x gradlew
```

### 3. 설정 확인

`src/main/resources/application.yml`에서 다음 항목을 로컬 환경에 맞게 확인합니다.

- `server.port`
- `server.servlet.context-path`
- `spring.datasource.*`
- `spring.mail.*`
- `spring.security.oauth2.client.registration.google.*`
- `app.upload.dir` 또는 기본 `./uploads` 경로

### 4. 테스트 실행

```bash
./gradlew test
```

### 5. 애플리케이션 실행

```bash
./gradlew bootRun
```

실행 후 브라우저에서 아래 주소로 접속합니다.

```text
http://localhost:8080/greengarden
```

---

## 🧪 빌드

```bash
./gradlew clean build
```

빌드 결과물은 다음 경로에 생성됩니다.

```text
build/libs/GreenGarden-1.0.0-RELEASE.jar
```

JAR 직접 실행 예시는 다음과 같습니다.

```bash
java -jar build/libs/GreenGarden-1.0.0-RELEASE.jar
```

---

## 🔐 인증/인가 정책

- 정적 리소스, 메인, 회원 관련 페이지는 비회원 접근을 허용합니다.
- `/my/**` 경로는 로그인한 사용자만 접근할 수 있습니다.
- `/admin/**` 경로는 `ROLE_ADMIN` 권한이 있는 사용자만 접근할 수 있습니다.
- 로그인 성공 시 관리자 권한 사용자는 관리자 홈으로, 일반 사용자는 메인으로 이동합니다.
- 로그아웃 시 세션과 쿠키를 정리하고 로그인 페이지로 이동합니다.

---

## 📦 업로드 파일

업로드 파일은 기본적으로 프로젝트 루트의 `uploads/` 디렉터리에 저장되며, `/uploads/**` URL로 정적 제공됩니다.

현재 저장소에는 다음 용도의 업로드 하위 디렉터리가 포함되어 있습니다.

- `uploads/product/`: 상품 이미지
- `uploads/exchange/`: 교환 신청 첨부 이미지
- `uploads/return/`: 반품 신청 첨부 이미지

---

## 🔄 CI/CD

`main` 브랜치에 push되면 GitHub Actions가 다음 작업을 수행합니다.

1. Ubuntu 환경에서 JDK 17 설정
2. Gradle 빌드 실행
3. 빌드된 JAR 파일을 artifact로 업로드
4. AWS EC2 서버로 JAR 복사
5. 원격 서버의 기존 Java 프로세스 종료 후 새 JAR 실행

배포에는 GitHub Secrets의 EC2 접속 정보가 필요합니다.

- `AWS_EC2_KEY`
- `AWS_EC2_USER`
- `AWS_EC2_HOST`

---

## 🧩 구현 모듈 요약

| 영역 | 주요 패키지/경로 |
| --- | --- |
| 메인/회사/정책 | `controller/MainController`, `controller/company`, `controller/policy` |
| 회원/인증 | `controller/member`, `controller/find`, `security` |
| 상품/주문 | `controller/product`, `service/ProductService`, `service/OrderService`, `service/CartService` |
| 고객센터 | `controller/cs`, `controller/admin/cs`, `service/NoticeService`, `service/FaqService`, `service/InquiryService` |
| 마이페이지 | `controller/my`, `service/MyService`, `service/MyCouponService`, `service/PointLedgerService` |
| 관리자 | `controller/admin`, `templates/admin` |
| 데이터 접근 | `repository`, `mapper`, `resources/mappers` |
| 화면 | `resources/templates` |
| 정적 리소스 | `resources/static` |

---

## 📝 개발 참고사항

- Gradle Wrapper 기준으로 빌드/실행합니다.
- Thymeleaf 템플릿은 `src/main/resources/templates` 하위에 사용자/관리자 기능별로 분리되어 있습니다.
- MyBatis XML Mapper는 `src/main/resources/mappers/**/*.xml` 패턴으로 로드됩니다.
- JPA와 MyBatis를 함께 사용하므로 신규 기능 개발 시 기존 서비스/Repository/Mapper 사용 방식을 먼저 확인하는 것이 좋습니다.
- 현재 테스트는 Spring Boot 컨텍스트 로드 중심으로 구성되어 있어, 데이터베이스 연결 환경이 맞지 않으면 테스트가 실패할 수 있습니다.

---

## 📄 라이선스

교육 과정 팀 프로젝트 용도로 작성된 저장소입니다. 별도 라이선스가 필요한 경우 팀 정책에 따라 추가해 주세요.
