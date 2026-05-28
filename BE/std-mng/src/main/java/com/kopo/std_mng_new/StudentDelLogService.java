package com.kopo.std_mng_new;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class StudentDelLogService {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void writeDeleteLog(Map<String, Object> deletedRow, String clientIp) {
        try {
            String insertLogSql = 
                "INSERT INTO delete_student_table " +
                "(student_score_idx, grade_level, class_no, student_id, subject_name, semester, exam_type, client_ip) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            jdbcTemplate.update(
                insertLogSql,
                deletedRow.get("idx"),
                deletedRow.get("grade_level"),
                deletedRow.get("class_no"),
                deletedRow.get("student_id"),
                "ALL", 
                deletedRow.get("semester"),
                deletedRow.get("exam_type"),
                clientIp 
            );
            System.out.println("삭제 로그 저장 완료! 대상 학번: " + deletedRow.get("student_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}