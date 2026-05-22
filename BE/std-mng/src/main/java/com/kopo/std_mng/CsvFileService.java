package com.kopo.std_mng;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvFileService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void saveCsvData(MultipartFile file, String fileName) throws Exception {
		
		// 🌟 1. 파일 이름 파싱 로직 추가
		// 확장자 제거 (예: "26_1_1_MID_01.csv" -> "26_1_1_MID_01")
		if (fileName.contains(".")) {
			fileName = fileName.substring(0, fileName.lastIndexOf("."));
		}
		
		// 언더바(_) 기준으로 쪼개기
		String[] tokens = fileName.split("_");
		
		// 파일명 파싱 값 변수에 할당 (예시 규칙: 학년_학기_시험_반)
		// 만약 파일명 구성 순서가 다르다면 아래 index(0, 1, 2, 3)를 변경해 주세요.
		String fileGrade = tokens[1].trim();       // 예: "1" (또는 "1학년")
		String fileSemester = tokens[2].trim();    // 예: "1" (또는 "1학기")
		String fileExamType = tokens[3].trim();    // 예: "MID" (또는 "중간")
		String fileClassCode = tokens[4].trim();   // 예: "01" (또는 "1반")

		// UTF-8: 한글 깨짐 방지
		try (BufferedReader fileReader = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));

				// CSVParser: 첫 줄을 헤더(컬럼명)로 인식
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

			// 🌟 student_score_table에 grade_code 컬럼도 포함하여 인서트 쿼리문 준비
			String sql = "INSERT INTO student_score_table ("
					+ "  student_code, student_name, grade_code, semester_name, exam_name, class_code, "
					+ "  KOR_01, MATH_01, ENG_02, create_date, edit_date"
					+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), null)";

			// 벌크 매핑을 위한 리스트 준비
			List<Object[]> batchArgs = new ArrayList<>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			// 🌟 2. CSV 로우(Line)를 하나씩 돌면서 가공하기
			for (CSVRecord record : csvRecords) {
				
				// 파일 내부에서는 공통 정보(학년, 반 등)를 뺐으므로 학번, 이름, 성적만 매핑해서 꺼냅니다.
				String studentCode = record.get("student_code");
				String studentName = record.get("student_name");
				String kor = record.get("KOR_01");
				String math = record.get("MATH_01");
				String eng = record.get("ENG_02");

				// 💡 [마스터 테이블 코드 변환 및 데이터 가공 예시]
				// 만약 파일명에서 파싱한 값이 코드("1", "MID")가 아니라 한글("1학년", "중간")이고,
				// DB 마스터 테이블 기준에 맞춰 코드로 변환해 쑤셔 넣어야 한다면 여기서 가공 처리를 해줍니다.
				String targetSemester = fileSemester;
				if ("1학기".equals(fileSemester)) {
					targetSemester = "1"; // 마스터 테이블 코드 스펙에 맞추기 예시
				}

				// 🌟 3. 파일 이름에서 가져온 데이터와 CSV 내부 데이터를 한 배열에 순서대로 조립
				Object[] values = new Object[] { 
					studentCode,    // CSV 파일에서 추출
					studentName,    // CSV 파일에서 추출
					fileGrade,      // ✨ 파일 이름에서 추출한 학년
					targetSemester, // ✨ 파일 이름에서 추출한 학기 (가공 적용)
					fileExamType,   // ✨ 파일 이름에서 추출한 시험종류
					fileClassCode,  // ✨ 파일 이름에서 추출한 반 코드
					kor,            // CSV 파일에서 추출
					math,           // CSV 파일에서 추출
					eng             // CSV 파일에서 추출
				};

				batchArgs.add(values);
			}

			// 🌟 4. JdbcTemplate의 batchUpdate로 대량 밀어넣기
			jdbcTemplate.batchUpdate(sql, batchArgs);
			System.out.println("====== 파일명 파싱 + CSV 통합 배치 인서트 성공! 건수: " + batchArgs.size() + " ======");
		}
	}
}