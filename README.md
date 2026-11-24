# 💻 DEVORA

> Connect. Share. Grow.
>
> 개발자의 질문이 지식이 되고, 지식이 성장으로 이어지는 공간.

## 📌 프로젝트 소개

DEVORA는 개발자들이 개발 과정에서 발생하는 기술적 문제, 오류 해결 방법 및 개발 지식을 공유할 수 있는 웹 커뮤니티 플랫폼입니다.

Spring Boot 기반의 MVC 아키텍처를 활용하여 회원 관리, 인증 및 인가, 게시판 CRUD, 입력값 검증, 관리자 기능을 구현하는 것을 목표로 합니다.

본 프로젝트는 한국방송통신대학교 대학원 정보과학과 컴포넌트프로그래밍 11강 및 12강 실습 내용을 기반으로 진행합니다.

## 🎯 프로젝트 목표

- Spring Boot 기반 웹 애플리케이션 개발
- Spring MVC 아키텍처의 이해 및 적용
- Spring Data JPA를 활용한 데이터베이스 관리
- Entity와 DTO의 역할 분리
- Spring Security 기반 인증 및 인가 구현
- 게시판 CRUD 및 사용자 권한 검증
- Validation과 BindingResult를 활용한 입력값 검증
- Thymeleaf 기반 서버 사이드 렌더링 구현
- GitHub를 활용한 협업 및 버전 관리

## 🛠️ 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.10 |
| Backend | Spring MVC |
| ORM | Spring Data JPA, Hibernate |
| Security | Spring Security |
| Database | MySQL |
| Frontend | HTML5, CSS3, JavaScript |
| Template Engine | Thymeleaf |
| Validation | Jakarta Bean Validation |
| Build Tool | Gradle |
| IDE | IntelliJ IDEA |
| Version Control | Git, GitHub |

## ✨ 주요 기능

### 일반 사용자 (ROLE_USER)

- [ ] 회원가입
- [ ] 아이디 및 이메일 중복 검사
- [ ] 로그인 및 로그아웃
- [ ] 비밀번호 변경
- [ ] 마이페이지
- [ ] 게시글 목록 조회
- [ ] 게시글 상세 조회
- [ ] 게시글 작성
- [ ] 본인 게시글 수정
- [ ] 본인 게시글 삭제
- [ ] 입력값 유효성 검증

### 관리자 (ROLE_ADMIN)

- [ ] 관리자 전용 페이지
- [ ] 전체 회원 목록 조회
- [ ] 회원 정보 수정
- [ ] 회원 삭제
- [ ] 관리자 접근 권한 검증

### 공통 기능

- [ ] 공통 Header 및 Footer
- [ ] 공통 레이아웃 및 스타일
- [ ] 사용자 입력 오류 메시지
- [ ] 인증 및 인가 처리
- [ ] 예외 처리

### 추가 구현 예정 기능

- [ ] 게시글 카테고리
- [ ] 게시글 검색
- [ ] 페이지네이션

## 🔐 사용자 권한

| 기능 | 비회원 | 일반 회원 | 관리자 |
|---|:---:|:---:|:---:|
| 회원가입 | O | X | X |
| 로그인 | O | X | X |
| 게시글 조회 | O | O | O |
| 게시글 작성 | X | O | O |
| 본인 게시글 수정 | X | O | O |
| 본인 게시글 삭제 | X | O | O |
| 타인 게시글 수정 | X | X | X |
| 타인 게시글 삭제 | X | X | X |
| 비밀번호 변경 | X | O | O |
| 관리자 페이지 접근 | X | X | O |
| 회원 목록 조회 | X | X | O |
| 회원 정보 수정 | X | X | O |
| 회원 삭제 | X | X | O |

## 🏗️ 시스템 아키텍처

Browser (HTML / CSS / JavaScript)
↓
Spring Security
↓
Controller (Spring MVC)
↓
Service (Business Logic)
↓
Repository (Spring Data JPA)
↓
MySQL Database

Spring MVC의 계층형 아키텍처를 기반으로 각 계층의 책임을 분리하여 유지보수성과 확장성을 고려한 구조로 설계합니다.

| 계층 | 역할 |
|---|---|
| Controller | 사용자 요청 처리 및 View 반환 |
| Service | 비즈니스 로직 및 트랜잭션 처리 |
| Repository | JPA 기반 데이터베이스 접근 |
| Entity | 데이터베이스 테이블과 객체 매핑 |
| DTO | 계층 간 데이터 전달 및 입력값 검증 |
| Config | Spring Security 및 애플리케이션 설정 |
| View | Thymeleaf 기반 HTML 렌더링 |

## 🗄️ 데이터베이스 설계

### members

| 컬럼 | 데이터 타입 | 설명 |
|---|---|---|
| id | BIGINT | PK, 회원 고유 번호 |
| username | VARCHAR | 로그인 아이디, UNIQUE |
| password | VARCHAR | 암호화된 비밀번호 |
| name | VARCHAR | 회원 이름 |
| email | VARCHAR | 이메일, UNIQUE |
| role | VARCHAR | 사용자 권한 |
| created_at | DATETIME | 회원가입 일시 |

### posts

| 컬럼 | 데이터 타입 | 설명 |
|---|---|---|
| id | BIGINT | PK, 게시글 고유 번호 |
| member_id | BIGINT | FK, 게시글 작성자 |
| title | VARCHAR | 게시글 제목 |
| content | TEXT | 게시글 내용 |
| created_at | DATETIME | 게시글 작성 일시 |
| updated_at | DATETIME | 게시글 수정 일시 |

### Entity Relationship

Member (1) : Post (N)

한 명의 회원은 여러 개의 게시글을 작성할 수 있으며, 하나의 게시글은 한 명의 회원을 작성자로 참조합니다.

## 📂 프로젝트 구조

- src/main/java
  - config — 보안 및 환경 설정
  - controller — 사용자 요청 처리
  - entity — JPA Entity 및 Enum
  - dto — 데이터 전달 객체
  - repository — 데이터베이스 접근
  - service — 비즈니스 로직
- src/main/resources
  - templates
    - fragments — 공통 레이아웃
    - member — 회원 관련 화면
    - post — 게시판 관련 화면
    - admin — 관리자 관련 화면
    - index.html — 메인 페이지
  - static
    - css — 스타일시트
    - js — JavaScript
    - images — 이미지 리소스
  - application.properties
- src/test — 테스트 코드
- build.gradle
- settings.gradle
- README.md

## 👥 팀 구성 및 역할 분담

### Developer A — 회원 및 관리자 시스템

- Member Entity 및 DTO 구현
- MemberRepository 및 MemberService 구현
- 회원가입 및 회원 입력값 검증
- Spring Security 설정
- 로그인 및 로그아웃
- 비밀번호 변경
- ROLE_USER 및 ROLE_ADMIN 권한 분리
- 관리자 회원 목록 조회
- 관리자 회원 정보 수정 및 삭제
- 회원 및 관리자 관련 Thymeleaf 화면 구현
- 담당 기능 테스트

### Developer B — 게시판 및 공통 UI 시스템

- Post Entity 및 DTO 구현
- PostRepository 및 PostService 구현
- Member와 Post의 JPA 연관관계 설정
- 게시글 목록 및 상세 조회
- 게시글 작성, 수정, 삭제
- 게시글 작성자 권한 검증
- Validation 및 BindingResult 오류 처리
- 메인 페이지 및 공통 UI 구현
- Header, Footer, CSS 구현
- 게시판 관련 Thymeleaf 화면 구현
- 담당 기능 테스트

### 공통 작업

- 프로젝트 초기 환경 설정
- 데이터베이스 스키마 설계
- GitHub 브랜치 관리
- 코드 리뷰 및 Pull Request
- 기능 통합 및 테스트
- 실습 결과 정리

## 🌿 Git Branch Strategy

| 브랜치 | 담당 | 설명 |
|---|---|---|
| main | 공통 | 최종 통합 브랜치 |
| feat/member-admin | Developer A | 회원, 인증, 관리자 기능 |
| feat/board-ui | Developer B | 게시판, Validation, 공통 UI |

각 개발자는 담당 브랜치에서 개발을 진행하며, 기능 구현 및 테스트 완료 후 Pull Request를 통해 main 브랜치에 병합합니다.

## 📝 Commit Convention

| Type | Description |
|---|---|
| feat | 새로운 기능 추가 |
| fix | 버그 수정 |
| refactor | 코드 리팩토링 |
| style | 코드 스타일 및 UI 수정 |
| docs | 문서 수정 |
| test | 테스트 코드 추가 및 수정 |
| chore | 프로젝트 설정 및 기타 작업 |
| design | 화면 디자인 변경 |

### Commit Message Example

- feat: 게시글 작성 기능 구현
- feat: Spring Security 로그인 기능 구현
- fix: 게시글 작성자 권한 검증 오류 수정
- refactor: 게시글 Service 로직 개선
- test: 게시판 CRUD 테스트 추가
- docs: README 프로젝트 설명 수정
- chore: Spring Boot 프로젝트 초기 설정

## 📅 개발 일정

개발 기간: 2026.11.24 ~ 2026.11.29

| 날짜 | 개발 내용 |
|---|---|
| 11.24 | 프로젝트 초기 설정 및 DB 설계 |
| 11.25 | 회원 관리 및 게시판 핵심 기능 구현 |
| 11.26 | 인증·인가 및 게시판 CRUD 구현 |
| 11.27 | 관리자 기능, Validation 및 UI 구현 |
| 11.28 | 코드 통합 및 기능 테스트 |
| 11.29 | 실습 보고서 작성 및 최종 제출 |

## 📚 강의 실습 내용

### 11강 — 웹 애플리케이션 개발 (1)

- 데이터베이스 스키마 구축
- 회원 및 게시글 Model, DTO 설계
- JPA Repository 구현
- Service 계층 구현
- Spring Security 기반 로그인 및 로그아웃
- 회원가입 및 비밀번호 변경

### 12강 — 웹 애플리케이션 개발 (2)

- 게시글 작성(Create)
- 게시글 수정(Update)
- 게시글 삭제(Delete)
- Validation 기반 입력값 검증
- BindingResult 기반 오류 처리
- ROLE_ADMIN 관리자 권한 설정
- 관리자 회원 목록 조회
- 관리자 회원 정보 수정 및 삭제

## 🚀 프로젝트 실행 환경

- Java 17
- MySQL
- Gradle Wrapper
- Port: 8080
- Database: devora

데이터베이스 접속 정보는 환경변수를 통해 관리하며, 실제 비밀번호 및 민감한 정보는 GitHub에 업로드하지 않습니다.

## 🧪 테스트 계획

- 회원가입 및 중복 검사 테스트
- 로그인 및 로그아웃 테스트
- 비밀번호 변경 테스트
- 게시글 CRUD 테스트
- 게시글 작성자 권한 검증 테스트
- Validation 및 BindingResult 테스트
- 관리자 접근 권한 테스트
- 관리자 회원 관리 테스트

## 📖 References

- 한국방송통신대학교 대학원 컴포넌트프로그래밍 11강, 12강
- Spring Boot: https://spring.io/projects/spring-boot
- Spring Data JPA: https://spring.io/projects/spring-data-jpa
- Spring Security: https://spring.io/projects/spring-security
- Thymeleaf: https://www.thymeleaf.org/
- MySQL: https://dev.mysql.com/doc/

---

**DEVORA — Connect. Share. Grow.**

개발자의 경험이 모두의 지식이 되는 공간.
