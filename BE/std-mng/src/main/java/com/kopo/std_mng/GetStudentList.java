package com.kopo.std_mng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class GetStudentList {

	// DB연동
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PostMapping("/getStudentsList")
	public ResponseEntity<Map<String, Object>> getSearchDropdowns(@RequestBody Map<String, Object> params) {
		Map<String, Object> responseData = new HashMap<>();

		try {
			
			String grade = (String) params.get("grade");
	        String semester = (String) params.get("semester");
	        String examType = (String) params.get("examType");
	        String classNumber = (String) params.get("classNumber");
	        String schoolGrade = (String) params.get("schoolGrade");
			
	        StringBuilder studentsListSql = new StringBuilder("SELECT * FROM student_score_table WHERE 1=1");
	        List<Object> queryParams = new ArrayList<>();

	        
//	        if (grade != null && grade != "0" && !grade.isEmpty()) {
//	            studentsListSql.append(" AND grade_code = ?");
//	            queryParams.add(grade);
//	        }
	        if (semester != null && semester != "0" && !semester.isEmpty()) {
	            studentsListSql.append(" AND semester_code = ?");
	            queryParams.add(semester);
	        }
	        if (examType != null && examType != "0" && !examType.isEmpty()) {
	            studentsListSql.append(" AND exam_code = ?");
	            queryParams.add(examType);
	        }
	        if (classNumber != null && classNumber != "0" && !classNumber.isEmpty()) {
	            studentsListSql.append(" AND class_code = ?");
	            queryParams.add(classNumber);
	        }
	        if (schoolGrade != null && schoolGrade != "0" && !schoolGrade.isEmpty()) {
	            studentsListSql.append(" AND rating_code = ?");
	            queryParams.add(schoolGrade);
	        }

	        // 3. 쿼리 실행 (SQL문과 주입할 파라미터 배열을 함께 넘김)
	        List<Map<String, Object>> lists = jdbcTemplate.queryForList(
	            studentsListSql.toString(), 
	            queryParams.toArray()
	        );


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
