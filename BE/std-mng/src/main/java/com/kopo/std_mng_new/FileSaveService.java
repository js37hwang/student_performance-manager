package com.kopo.std_mng_new;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSaveService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<String, Object> saveCsvData(MultipartFile file, FileTitleInfoDTO titleInfo) {
		Map<String, Object> res = new HashMap<>();

		try {
			// csv 파일 읽기
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));

			List<Object[]> batchArgs = new ArrayList<>();

			String line;
			boolean isFirstLine = true;

			while ((line = reader.readLine()) != null) {
				// 헤더 행 스킵
				if (isFirstLine) {
					isFirstLine = false;
					continue;
				}

				// 빈 줄 스킵
				if (line.trim().isEmpty())
					continue;

				// 2. 쉼표 기준으로 컬럼 파싱
				String[] columns = line.split(",");

				// CSV 컬럼 순서: 학번, 이름, 국어, 수학, 영어
				String studentId = columns[0].trim();
				String name = columns[1].trim();
				double korean = Double.parseDouble(columns[2].trim());
				double english = Double.parseDouble(columns[3].trim());
				double math = Double.parseDouble(columns[4].trim());

				Integer grade = titleInfo.getGradeLevel();
				Integer year = titleInfo.getSchoolYear();
				Integer classNo = titleInfo.getClassNo();
				String examType = titleInfo.getExamType();
				Integer semester = titleInfo.getSemester();

				// 3. 배치 저장용 파라미터 배열 구성
				// id(자동), 년도, 학년, 학번, 이름, 시험타입, 국어, 영어, 수학, 등록일(자동), 수정일(null), 삭제여부(bool)
				batchArgs.add(new Object[] { year, grade, classNo, studentId, name, semester, examType, korean, english, math,

						korean + english + math, // total_score
						(korean + english + math) / 3.0, // average_score

						null, // grade_rank
						null, // class_rank

						LocalDateTime.now(), null,

						false });
			}

			reader.close();

			// insert하기
			String sql = "INSERT INTO student_score_management ("
					+ "school_year, grade_level, class_no, student_id, student_name, "
					+ "semester, exam_type, korean_score, english_score, math_score, "
					+ "total_score, average_score, grade_rank, class_rank, created_at, updated_at, del_flag"
					+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			int[] updateCounts = jdbcTemplate.batchUpdate(sql, batchArgs);

			res.put("savedCount", updateCounts.length);
			res.put("result", "success");

		} catch (Exception e) {
			e.printStackTrace();
			res.put("message", e.getMessage());
			res.put("result", "fail");
		}

		return res;
	}

}
