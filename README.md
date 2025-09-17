피그마 : https://www.figma.com/design/Tr63anCStUp6O9QM1zsnSG/BNK_2%EC%B0%A8_%EC%BC%80%EC%9D%B4%EB%A7%88%EC%BC%93-%EC%87%BC%ED%95%91%EB%AA%B0-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8?node-id=0-1&p=f&t=OkidTpodIPcO87BU-0

엑셀 : https://docs.google.com/spreadsheets/d/19zL2yzpaBrGRmPTm4cGDgpbHTyGyVo3kn_RG8Y1trXk/edit?gid=0#gid=0
# Project2_Kmarket_Team3
# 🛒 Kmarket 프로젝트  
BNK 부산은행 금융DT 아카데미 개발자 양성과정 팀 프로젝트  
전자상거래 웹 시스템 구축 프로젝트  

---

## 📖 프로젝트 개요
**Kmarket 프로젝트**는 온라인 쇼핑몰 기능과 관리자 페이지를 통합적으로 구현한 웹 어플리케이션입니다.  

- 일반 사용자: 상품 조회, 장바구니, 주문, 마이페이지 기능 제공  
- 관리자: 상품, 회원, 주문, 쿠폰, 고객센터, 회사 정보 관리  

---

## 👥 팀원
- 팀장: 서현우
- 팀원: 박효빈, 이수연, 이종봉, 조지영, 한탁원

---

## 🎯 주요 요구사항
### 화면 설계
- 직관적이고 일관된 UI 제공  
- 색상/폰트 규칙 준수 및 반응형 지원  

### 데이터베이스
- ERD 작성 (상품, 회원, 주문, 쿠폰, Q&A 등 주요 엔티티)  
- 제약조건, 정규화, 성능 고려한 테이블 설계  
- 샘플 데이터로 초기 더미 데이터 입력  

### 기능 구현
- 회원가입 / 로그인  
- 상품목록 / 상품보기 / 장바구니 / 주문하기  
- 공지사항, FAQ, Q&A CRUD  
- 마이페이지 (주문내역, 포인트, 쿠폰, 리뷰)  
- 관리자 페이지 (상품, 회원, 주문, 쿠폰, 고객센터, 정책 관리)  

---

## 🏗️ 시스템 구성
### 1. 사용자 사이트 (User Website)
- 메인: 메인화면  
- 상품: 상품목록, 상품보기, 장바구니, 주문하기, 주문완료, 검색  
- 회원: 로그인, 회원가입, 아이디/비밀번호 찾기  
- 고객센터: 공지사항, FAQ, Q&A  
- 마이페이지: 주문내역, 포인트, 쿠폰, 리뷰, 문의내역, 나의설정  
- 회사소개: 회사 정보, 기업문화, 소식과 이야기, 채용, 미디어  
- 서비스정책: 구매/판매자 약관, 위치정보, 개인정보  

### 2. 관리자 사이트 (Admin System)
- 환경설정: 기본설정, 배너, 약관, 카테고리, 버전 관리  
- 상점관리: 상품목록, 매출현황  
- 회원관리: 회원목록, 포인트 관리  
- 상품관리: 상품현황, 상품등록  
- 주문관리: 주문현황, 배송현황  
- 쿠폰관리: 쿠폰목록, 발급현황  
- 고객센터: 공지사항, FAQ, Q&A 관리  
- 채용관리: 채용 공고 관리  

---

## ⚙️ 기술 스택
- **Frontend**: HTML5, CSS3, JavaScript, JSP  
- **Backend**: Java (Servlet/JSP), Spring Framework  
- **Database**: Oracle / MySQL  
- **Infra & Tools**: AWS, Jenkins, GitHub Actions (CI/CD)  
- **협업 툴**: Figma (UI/UX), GitHub (형상관리), Slack (커뮤니케이션), Notion (작업 관리)  

---

## 📂 디렉터리 구조
```plaintext
/kmarket
 ├── index.jsp                  # 메인화면
 │
 ├── product/                   # 상품 관련
 │    ├── list.jsp              # 상품목록
 │    ├── view.jsp              # 상품보기
 │    ├── cart.jsp              # 장바구니
 │    ├── order.jsp             # 주문하기
 │    ├── complete.jsp          # 주문완료
 │    └── search.jsp            # 상품검색
 │
 ├── member/                    # 회원 관련
 │    ├── login.jsp             # 로그인
 │    ├── join.jsp              # 회원가입 구분
 │    ├── signup.jsp            # 약관동의
 │    ├── register.jsp          # 일반회원가입
 │    ├── registerSeller.jsp    # 판매자회원가입
 │    └── find/                 # 아이디/비밀번호 찾기
 │         ├── userId.jsp
 │         ├── resultId.jsp
 │         ├── password.jsp
 │         └── changePassword.jsp
 │
 ├── cs/                        # 고객센터
 │    ├── index.jsp             # 고객센터 메인
 │    ├── notice/ (list/view)
 │    ├── faq/ (list/view)
 │    └── qna/ (list/view/write)
 │
 ├── my/                        # 마이페이지
 │    ├── home.jsp              # 메인
 │    ├── order.jsp             # 주문내역
 │    ├── point.jsp             # 포인트
 │    ├── coupon.jsp            # 쿠폰
 │    ├── review.jsp            # 리뷰
 │    ├── qna.jsp               # 문의내역
 │    └── info.jsp              # 나의 설정
 │
 ├── policy/                    # 서비스 정책
 │    ├── buyer.jsp             # 구매회원 약관
 │    ├── seller.jsp            # 판매회원 약관
 │    ├── finance.jsp           # 전자금융거래 약관
 │    ├── location.jsp          # 위치정보 이용 약관
 │    └── privacy.jsp           # 개인정보처리방침
 │
 ├── company/                   # 회사소개
 │    ├── index.jsp             # 회사소개 메인
 │    ├── culture.jsp           # 기업문화
 │    ├── story.jsp             # 소식과 이야기
 │    ├── recruit.jsp           # 채용
 │    └── media.jsp             # 미디어
 │
 └── admin/                     # 관리자
      ├── index.jsp             # 관리자 메인
      ├── config/ (basic/banner/policy/category/version)
      ├── shop/ (list/sales)
      ├── member/ (list/point)
      ├── product/ (list/register)
      ├── order/ (list/delivery)
      ├── coupon/ (list/issued)
      ├── cs/ (notice/faq/qna)
      └── recruit/ (list)
```

## 🚀 실행 방법
1. 저장소 클론  
   ```bash
   git clone https://github.com/your-team/kmarket.git
   ```
2. IDE(Eclipse/IntelliJ)에서 Import → Dynamic Web Project
3. DB 연결 설정 (/resources/db.properties)
4. Tomcat 실행 후 접속
```http://localhost:8080/kmarket```

---
## 📌 향후 계획
- 반응형 웹 적용 (모바일 대응)
- 관리자 페이지 기능 고도화
- REST API 기반 확장
