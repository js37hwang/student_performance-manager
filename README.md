# 📋 학생 성적 관리 시스템
### Student Performance Management System

> 고등학교 교사를 위한 웹 기반 성적 관리 시스템  
> Vanilla JavaScript + Spring MVC + MySQL + Docker

<br>

## 📁 프로젝트 구조

```
student_performance-manager/
│
├── BE/
│   └── std-mng/
│       ├── src/
│       │   └── main/
│       │       ├── java/com/kopo/std_mng_new/
│       │       │   ├── FileSaveService.java           # CSV 데이터 DB 저장
│       │       │   ├── FileTitleInfoDTO.java          # 파일명 파싱 결과 DTO
│       │       │   ├── FileTitleParserService.java    # 파일명 파싱 서비스
│       │       │   ├── FileUploadService.java         # CSV 업로드
│       │       │   ├── StudentDelLogService.java      # 삭제 감사 로그 기록
│       │       │   ├── StudentDelProcess.java         # 삭제 트랜잭션 처리
│       │       │   ├── StudentDelService.java         # 소프트 딜리트 쿼리
│       │       │   ├── StudentDTO.java                # 학생 성적 데이터 DTO
│       │       │   ├── StudentEditProcess.java        # 수정 트랜잭션 처리
│       │       │   ├── StudentEditService.java        # 성적 수정 쿼리
│       │       │   ├── StudentKeyDTO.java             # 수정/삭제 키 DTO
│       │       │   ├── StudentListService.java        # 학생 목록 조회
│       │       │   ├── StudentRankService.java        # 등수 재계산
│       │       │   └── StudentScoreController.java    # REST API 엔드포인트
│       │       ├── resources/
│       │       │   ├── application.properties
│       │       │   ├── schema.sql
│       │       │   └── data.sql
│       │       └── webapp/
│       │           └── WEB-INF/
│       │               └── spring/
│       │                   └── servlet-context.xml
│       ├── Dockerfile
│       └── pom.xml
│
├── FE/
│   ├── script/
│   │   ├── editStudent.js    # 성적 수정/ 학생 삭제
│   │   ├── getDropdown.js    # 드롭다운 옵션 조회 (현재 미사용)
│   │   ├── getStudents.js    # 학생 목록 조회
│   │   └── uploadCsv.js      # CSV 업로드
│   ├── app.js
│   ├── index.html
│   ├── nginx.conf
│   ├── reset.css
│   ├── style.css
│   └── Dockerfile
│
├── dataset/
│   ├── 26_1_1_MID_01.csv
│   ├── 26_1_1_MID_02.csv
│   ├── 26_2_2_FIN_02.csv
│   ├── 26_3_1_FIN_01.csv
│   └── 26_3_2_FIN_01.csv
│
├── .gitignore
└── docker-compose.yml
```

<br>

## 🛠 기술 스택

| 구분 | 기술 |
|------|------|
| Frontend | HTML5, CSS3, Vanilla JavaScript (ES6+) |
| Backend | Java 11, Spring MVC (Legacy), Apache Tomcat 9 |
| Build | Maven |
| Database | MySQL 8.0 |
| Web Server | Nginx (리버스 프록시) |
| Infra | Docker, Docker Compose, AWS EC2 (t3.small) |

<br>

## ✅ 주요 기능

### 📤 성적 등록 (CREATE)
- CSV 파일 업로드를 통한 다수 학생 성적 일괄 등록
- **파일명 파싱** 으로 학년도/학년/반/학기/시험 종류 자동 추출
  - 파일명 규칙: `년도_학년_학기_시험구분_반번호.csv`
  - 예시: `26_1_1_MID_01.csv` → 2026년도, 1학년, 1학기, 중간고사, 1반
- 등록 완료 후 **전교 등수 및 반 등수 자동 계산**

### 🔍 성적 조회 (READ)
- 학년 / 반 / 학기 / 시험 종류 **4가지 조건 다중 검색**
- 각 항목별 전체 선택 가능
- 결과는 **학년 → 반 → 학기 → 학번** 순으로 정렬 반환

### ✏️ 성적 수정 (UPDATE)
- 학생 1명 선택 후 국어 / 수학 / 영어 점수 수정
- 입력 범위: 0 ~ 100 (소수점 허용)
- 수정 후 **전교 및 반 등수 자동 재계산**

### 🗑️ 성적 삭제 (DELETE)
- **소프트 딜리트** 방식 (`del_flag = TRUE`, 실제 삭제 없음)
- 삭제 시 `delete_student_table`에 삭제 로그 기록 (클라이언트 IP 포함)
- 삭제 후 **등수 재계산**

<br>

## 🗄 데이터베이스 설계

총 **8개 테이블**로 구성됩니다.

### 마스터 테이블 (6개)

| 테이블명 | 설명 |
|----------|------|
| `grade_table` | 학년 코드 (1~3학년) |
| `semester_table` | 학기 코드 (1~2학기) |
| `exam_type_table` | 시험 종류 (MID / FIN) |
| `class_no_table` | 반 코드 (1~5반) |
| `school_grade_table` | 내신 등급 및 비율 (1~9등급)- 미사용 |
| `subject_table` | 과목 코드 (국어 / 수학 / 영어) |

### 메인 및 로그 테이블 (2개)

| 테이블명 | 설명 |
|----------|------|
| `student_score_management` | 학생 성적 원장 (CRUD 대상) |
| `delete_student_table` | 삭제 감사 로그 |

> **소프트 딜리트 전략**: `del_flag = TRUE`로 논리 삭제 처리, 조회 시 `WHERE del_flag = FALSE` 조건 적용

<br>

## 🌐 API 명세

| Method | Endpoint | 설명 |
|--------|----------|------|
| `POST` | `/api/fileUpload` | CSV 성적 파일 업로드 |
| `GET` | `/api/getDropdown` | 검색 드롭다운 옵션 조회- 미사용 |
| `POST` | `/api/getStdList` | 학생 성적 목록 조회 |
| `POST` | `/api/editStd` | 학생 성적 수정 |
| `POST` | `/api/delStudent` | 학생 성적 삭제 |

<br>

## 🐳 배포 구성

### 컨테이너 구성

```
docker-compose.yml
├── mysql      (MySQL 8.0 / Port 3306)
├── backend    (Tomcat 9 + Spring WAR / Port 8080)
└── frontend   (Nginx + 정적파일 / Port 80)
```

### Nginx 리버스 프록시

프론트엔드에서 `/api/` 요청은 Nginx가 백엔드로 전달합니다.  
이를 통해 IP 변경 시 코드 수정 없이 동작하며 CORS 문제도 해결됩니다.

```nginx
location /api/ {
    proxy_pass http://backend:8080;
}
```

### 실행 방법

```bash
# 1. 레포 클론
git clone https://github.com/js37hwang/student_performance-manager.git
cd student_performance-manager

# 2. 도커 컴포즈 실행 (빌드 포함)
docker compose up --build -d

# 3. 브라우저 접속
# http://localhost  또는  http://EC2-퍼블릭-IP
```

> **주의**: 첫 실행 시 MySQL 초기화 완료 후 백엔드가 자동으로 연결됩니다.  
> 볼륨이 유지되므로 이후 재시작 시 `docker compose up -d`만으로 실행 가능합니다.

### CSV 파일명 규칙

```
{년도}_{학년}_{학기}_{시험구분}_{반번호}.csv

예시)
26_1_1_MID_01.csv  →  2026년, 1학년, 1학기, 중간고사, 1반
26_3_2_FIN_02.csv  →  2026년, 3학년, 2학기, 기말고사, 2반
```

<br>

## 🔧 트러블슈팅 기록

### 1. 한글 깨짐
- **원인**: Nginx에 UTF-8 charset 설정 누락
- **해결**: `nginx.conf`에 `charset utf-8;` 추가

### 2. BASE_URL is not defined
- **원인**: `app.js`가 Node.js express 서버 코드인데 브라우저에서 `<script>`로 로드, `require()`가 브라우저에 없음
- **해결**: `index.html`에서 `app.js` 제거, 인라인으로 `const BASE_URL = "";` 선언

### 3. EC2 재시작 시 IP 변경 문제
- **원인**: EC2 재시작마다 퍼블릭 IP가 바뀌어 API 주소를 매번 수정해야 함
- **해결**: Nginx 리버스 프록시 설정으로 `/api/` 요청을 docker-compose.yml파일에서 선언한 별칭에 해당하는 `backend:8080`으로 전달, `BASE_URL = ""`으로 고정

### 4. Docker 권한 오류
- **원인**: EC2 재시작 후 `/var/run/docker.sock` 권한 초기화
- **해결**: `sudo chmod 666 /var/run/docker.sock`

### 5. MySQL 연결 실패 (핵심)
- **원인 1**: `servlet-context.xml`의 DB URL이 EC2 퍼블릭 IP로 하드코딩되어 있음 (`jdbc:mysql://43.200.183.5:3306/...`)
- **원인 2**: `servlet-context.xml`에 `jdbc:initialize-database` 블록이 별도로 있어 `application.properties`의 `spring.sql.init.mode=never` 설정이 무시됨
- **해결**: URL을 `mysql:3306`으로 변경, `jdbc:initialize-database` 블록 주석 처리

### 6. schema/data 실행 순서 오류
- **원인**: `docker-entrypoint-initdb.d`에 마운트 시 알파벳 순으로 실행되어 `data.sql`이 `schema.sql`보다 먼저 실행됨
- **해결**: `1_schema.sql`, `2_data.sql`로 파일명에 순서 번호 부여

<br>

## 💭 개발 회고

### 배운 점
- **DTO의 필요성**: 없이 개발 시 계층 간 데이터 전달이 복잡해지고 유지보수가 어려워짐. 도입 후 명확한 데이터 흐름 확보
- **Spring MVC 구조**: Controller / Service 분리의 중요성과 `@Transactional` 트랜잭션 관리 방법 습득
- **CORS 전역 설정**: `@CrossOrigin` 대신 `servlet-context.xml`에서 일괄 관리하는 방식이 더 효율적
- **소프트 딜리트 패턴**: `del_flag` + 감사 로그 연계 구현
- **Docker 멀티스테이지 빌드**: Maven 빌드 → WAR 생성 → Tomcat 배포를 단일 Dockerfile로 처리

### 아쉬운 점
- **마스터 테이블 미활용**: `grade_table`, `semester_table` 등 설계 및 데이터까지 넣어두었으나 실제 비즈니스 로직에서 JOIN이나 참조 없이 하드코딩된 값을 사용한 점
- **DB 설계 판단 어려움**: 코드성 데이터와 관계형 데이터의 경계를 설계 단계에서 제대로 정의하지 못함
- **초기 과잉 설계**: 내신 등급까지 계산하려다 테이블 구조가 복잡해져 프로젝트를 처음부터 재시작, 시간 부족으로 목표 기능 미완성

### 개선 방향
- **마스터 테이블 연계**: 드롭다운 데이터를 DB에서 조회하여 반환, FK 기반 유효성 검증 추가
- **과목 동적 구성**: 현재 국어/수학/영어 3과목 하드코딩 → `subject_table` 기반 학년/학기별 과목 동적 처리
- **내신 등급 계산**: `school_grade_table`의 등급 비율을 활용한 자동 등급 계산 기능
- **인증/인가**: 교사 로그인 기능 추가


