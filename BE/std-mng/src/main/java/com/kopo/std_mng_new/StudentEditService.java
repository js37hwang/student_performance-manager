package com.kopo.std_mng_new;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class StudentEditService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> editStdData(StudentKeyDTO stdInfo, Map<String, Object> scoreList) {

        Map<String, Object> res = new HashMap<>();

        try {
        	System.out.println(stdInfo.getIdx()+ stdInfo.getStudentId());
            // 1. 점수 파싱 및 계산
            double korean = 0;
            double english = 0;
            double math = 0;

            if (scoreList.get("korean_score") instanceof Number) {
                korean = ((Number) scoreList.get("korean_score")).doubleValue();
            }
            if (scoreList.get("english_score") instanceof Number) {
                english = ((Number) scoreList.get("english_score")).doubleValue();
            }
            if (scoreList.get("math_score") instanceof Number) {
                math = ((Number) scoreList.get("math_score")).doubleValue();
            }

            double total = korean + english + math;
            double avg = total / 3.0;

            // 2. UPDATE 실행 (JdbcTemplate 활용)
            String updateSql = 
                "UPDATE student_score_management SET " +
                "korean_score = ?, " +
                "english_score = ?, " +
                "math_score = ?, " +
                "total_score = ?, " +
                "average_score = ?, " +
                "updated_at = ? " +
                "WHERE del_flag = FALSE " +
                "AND idx = ? " +
                "AND student_id = ?";

            jdbcTemplate.update(updateSql, 
                korean, english, math, total, avg, LocalDateTime.now(), 
                stdInfo.getIdx(), stdInfo.getStudentId()
            );

            // 3. SELECT 실행 (JdbcTemplate.queryForObject 활용)
            // 특정 학생(idx, student_id)의 정보만 가져오도록 WHERE 절을 보완했습니다.
            String selectSql = 
                "SELECT grade_level, class_no, exam_type, school_year " +
                "FROM student_score_management " +
                "WHERE 1 = 1 " +
                "AND del_flag = FALSE " +
                "AND idx = ? " +
                "AND student_id = ?";

            // RowMapper를 이용해 조회 결과를 DTO로 바로 매핑합니다.
            FileTitleInfoDTO rankKeys = jdbcTemplate.queryForObject(selectSql, (rs, rowNum) -> {
                return new FileTitleInfoDTO(
                    rs.getInt("grade_level"),
                    rs.getInt("class_no"),
                    rs.getString("exam_type"),
                    rs.getInt("school_year")
                );
            }, stdInfo.getIdx(), stdInfo.getStudentId());
            
            // 4. 결과 반환
            res.put("result", "success");
            res.put("rankKyes", rankKeys);

        } catch (Exception e) {
            e.printStackTrace();
            res.put("message", e.getMessage());
            res.put("result", "fail");
        }

        return res;
    }
}