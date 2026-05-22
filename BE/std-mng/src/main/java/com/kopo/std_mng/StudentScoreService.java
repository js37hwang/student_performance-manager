package com.kopo.std_mng;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StudentScoreService {
	// 만약 DB 코드 테이블을 조회해야 한다면 레포지토리 주입
    // @Autowired private CodeRepository codeRepository;
//    @Autowired private StudentScoreMapper studentScoreMapper; 

    public void processAndSaveScores(MultipartFile file, String fileName) throws Exception {
        // 1. 확장자(.csv) 제거 및 언더바(_) 기준 분리
        String cleanName = fileName.substring(0, fileName.lastIndexOf(".")); // "26_3_1_FIN_01"
        String[] tokens = cleanName.split("_");
        
        if (tokens.length != 5) {
            throw new IllegalArgumentException("올바르지 않은 파일명 형식입니다.");
        }

        // 2. 파일명에서 원본 값 추출
        String yearToken = tokens[0];     // 연도
        String gradeToken = tokens[1];    // 학년
        String semesterToken = tokens[2]; // 학기
        String examToken = tokens[3];     // 중간/기말
        String classToken = tokens[4];    // 반

        // 3. 🌟 기존 마스터 테이블의 Code 체계와 매핑 변환
        // 예시 A: 규칙에 맞게 문자열을 조합하여 고유 코드를 만드는 경우
        String semesterCode = "SEM_" + yearToken + "_" + gradeToken + "_" + semesterToken; 
        // 결과 예시: "SEM_26_3_1" (2026년 3학년 1학기 코드)
        
        String examCode = examToken.equals("MID") ? "EXAM_MID_CODE" : "EXAM_FIN_CODE";
        // 결과 예시: 이전 DB 설계의 exam_name 또는 exam_code 컬럼에 들어갈 값 매핑

        // 예시 B: 만약 DB 마스터 테이블에서 실제 고유 ID(PK)를 찾아와야 하는 경우
        // String semesterCode = codeRepository.findSemesterCode(yearToken, gradeToken, semesterToken);
        // String examCode = codeRepository.findExamCodeByToken(examToken);


        // 4. CSV 내부 데이터 읽기 및 공통 컬럼 세팅
//        List<StudentScoreDto> scoreList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), "EUC-KR"))) {
            String line;
            boolean isHeader = true;
            
            while ((line = br.readLine()) != null) {
                if (isHeader) { // 첫 줄 헤더 건너뛰기
                    isHeader = false;
                    continue;
                }
                
                String[] csvRow = line.split(","); // CSV 한 줄 파싱
                
                // DTO 객체 생성 후 CSV 데이터 채우기
//                StudentScoreDto dto = new StudentScoreDto();
//                dto.setStudentCode(csvRow[0]); // 학번
//                dto.setStudentName(csvRow[1]); // 이름
//                
//                // 성적 데이터 매핑 (국/수/영 등)
//                dto.setKor01(csvRow[2]);
//                dto.setMath01(csvRow[3]);
//                dto.setEng02(csvRow[4]);
//                
//                // 🌟 파일명에서 추출하여 변환한 매핑 코드들을 공통으로 꽂아넣기!
//                dto.setSemesterName(semesterCode); // 파싱된 학기 코드 적용
//                dto.setExamName(examCode);         // 파싱된 시험 종류 코드 적용
//                
//                // 날짜 정보 세팅
//                dto.setCreateDate(LocalDate.now());
//                dto.setEditDate(LocalDate.now());
//                
//                scoreList.add(dto);
            }
        }

        // 5. DB에 대량 저장 (MyBatis <foreach> 또는 JPA saveAll 등 활용)
//        studentScoreMapper.insertStudentScores(scoreList);
    }
}
