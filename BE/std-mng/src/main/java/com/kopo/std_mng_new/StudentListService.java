package com.kopo.std_mng_new;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class StudentListService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getStdList(FileTitleInfoDTO searchKeyword) {

        Map<String, Object> res = new HashMap<>();
        StringBuilder sql = new StringBuilder();

        try {
            Integer grade = searchKeyword.getGradeLevel();
            Integer classNo = searchKeyword.getClassNo();
            String examType = searchKeyword.getExamType();
            Integer semester = searchKeyword.getSemester();

            // 1. 기본 쿼리 및 파라미터 리스트 준비
            sql.append("SELECT * FROM student_score_management WHERE 1=1 AND del_flag = false");
            List<Object> params = new ArrayList<>();

            // 2. 동적 조건절 생성 (Integer 조건들)
            Map<String, Object> conditionMap = new LinkedHashMap<>();
            conditionMap.put("grade_level", grade);
            conditionMap.put("class_no", classNo);
            conditionMap.put("semester", semester);

            for (Map.Entry<String, Object> entry : conditionMap.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof Integer && (Integer) value != 0) {
                    sql.append(" AND ").append(entry.getKey()).append(" = ? ");
                    params.add(value);
                }
            }

            // 3. 동적 조건절 생성 (String 조건)
            if (examType != null && !"0".equals(examType) && !examType.isEmpty()) {
                sql.append(" AND exam_type = ? ");
                params.add(examType);
            }
            
            // 정렬 추가
            sql.append(" ORDER BY grade_level, class_no, semester, student_id ");

            // 4. JdbcTemplate.query를 사용하여 조회 및 매핑
            // 파라미터 리스트를 배열(params.toArray())로 변환하여 넘겨줍니다.
            List<StudentDTO> list = jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
                StudentDTO dto = new StudentDTO();

                dto.setIdx(rs.getInt("idx"));
                dto.setSchoolYear(rs.getInt("school_year"));
                dto.setSemester(rs.getInt("semester"));
                dto.setGradeLevel(rs.getInt("grade_level"));
                dto.setClassNo(rs.getInt("class_no"));
                dto.setStudentId(rs.getString("student_id"));
                dto.setStudentName(rs.getString("student_name"));
                dto.setExamType(rs.getString("exam_type"));
                dto.setKoreanScore(rs.getInt("korean_score"));
                dto.setEnglishScore(rs.getInt("english_score"));
                dto.setMathScore(rs.getInt("math_score"));
                dto.setTotalScore(rs.getInt("total_score"));
                dto.setAverageScore(rs.getDouble("average_score"));
                dto.setGradeRank(rs.getInt("grade_rank"));
                dto.setClassRank(rs.getInt("class_rank"));

                // LocalDateTime 변환 (rs.getTimestamp가 null이면 null 반환)
                dto.setCreatedAt(rs.getTimestamp("created_at") != null ? 
                        rs.getTimestamp("created_at").toLocalDateTime() : null);
                dto.setUpdatedAt(rs.getTimestamp("updated_at") != null ? 
                        rs.getTimestamp("updated_at").toLocalDateTime() : null);

                return dto;
            }, params.toArray());

            // 5. 결과 설정
            res.put("data", list);
            res.put("result", "success");

        } catch (Exception e) {
            e.printStackTrace();
            res.put("message", e.getMessage());
            res.put("result", "fail");
        }

        return res;
    }
}