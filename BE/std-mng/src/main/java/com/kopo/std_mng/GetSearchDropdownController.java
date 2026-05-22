package com.kopo.std_mng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class GetSearchDropdownController {

	// DB연동
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PostMapping("/getDropdown")
	public ResponseEntity<Map<String, Object>> getSearchDropdowns() {
		Map<String, Object> responseData = new HashMap<>();

		try {
			// 1. 학년 코드 리스트 조회
			String gradeSql = "SELECT grade_code AS value, grade_name AS label FROM grade_table";
			List<Map<String, Object>> grades = jdbcTemplate.queryForList(gradeSql);
			responseData.put("grade", grades);

			// 2. 학기/시험 종류 리스트 조회 (중간, 기말 등)
			String examSql = "SELECT exam_code AS value, exam_name AS label FROM exam_type_table";
			List<Map<String, Object>> exams = jdbcTemplate.queryForList(examSql);
			responseData.put("examType", exams);

			// 3. 반 리스트 조회
			String classSql = "SELECT class_code AS value, class_name AS label FROM class_num_table";
			List<Map<String, Object>> classes = jdbcTemplate.queryForList(classSql);
			responseData.put("classNumber", classes);

			// 4. 내신 등급 기준 리스트 조회
			String scoreGradeSql = "SELECT school_grade_code AS value, school_grade_name AS label FROM school_grade_table";
			List<Map<String, Object>> scoreGrades = jdbcTemplate.queryForList(scoreGradeSql);
			responseData.put("schoolGrade", scoreGrades);

			// 5. 과목 리스트 조회
			String subjectSql = "SELECT subject_code AS value, subject_name AS label FROM subject_table";
			List<Map<String, Object>> subjects = jdbcTemplate.queryForList(subjectSql);
			responseData.put("subjectList", subjects);

			// 6. 학기 리스트 조회
			String semesterSql = "SELECT semester_code AS value, semester_name AS label FROM semester_table";
			List<Map<String, Object>> semesters = jdbcTemplate.queryForList(semesterSql);
			responseData.put("semester", semesters);

			// 성공 시 200 OK 상태코드와 함께 맵 전달
			return new ResponseEntity<>(responseData, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();

			// 에러 발생 시 프론트에 에러 메시지 전달
			Map<String, Object> errorMap = new HashMap<>();
			errorMap.put("error", "드롭다운 데이터를 불러오는 중 오류가 발생했습니다.");
			errorMap.put("message", e.getMessage());

			return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
