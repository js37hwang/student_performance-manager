package com.kopo.std_mng_new;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class StudentRankService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> updateScoreRank(FileTitleInfoDTO rankRangeInfo) {
        Map<String, Object> res = new HashMap<>();

        try {
            Integer year      = rankRangeInfo.getSchoolYear();
            Integer grade     = rankRangeInfo.getGradeLevel();
            Integer classNo   = rankRangeInfo.getClassNo();
            String  examType  = rankRangeInfo.getExamType();

            // 1. 해당 학년 전체 학생 조회 (del_flag = false 조건)
            String selectSql = "SELECT idx, class_no, total_score " +
                               "FROM student_score_management " +
                               "WHERE school_year = ? AND grade_level = ? AND exam_type = ? AND class_no = ? AND del_flag = false " +
                               "ORDER BY total_score DESC";

            RowMapper<Map<String, Object>> rowMapper = (rs, rowNum) -> {
                Map<String, Object> row = new HashMap<>();
                row.put("idx", rs.getInt("idx"));
                row.put("classNo", rs.getInt("class_no"));
                row.put("totalScore", rs.getInt("total_score"));
                return row;
            };

            List<Map<String, Object>> students = jdbcTemplate.query(
                selectSql, rowMapper, year, grade, examType, classNo
            );

            // 2. 학년 등수 계산 (이미 total_score DESC 정렬 상태)
            List<Object[]> batchArgs = new ArrayList<>();

            int gradeRank = 0;
            int prevScore = -1;
            int sameScoreCount = 0;

            for (Map<String, Object> student : students) {
                int score = (int) student.get("totalScore");

                // 동점자 처리: 같은 점수면 같은 등수
                if (score != prevScore) {
                    gradeRank += (1 + sameScoreCount);
                    sameScoreCount = 0;
                } else {
                    sameScoreCount++;
                }

                student.put("gradeRank", gradeRank);
                prevScore = score;
            }

            // 3. 반별 등수 계산
            // classNo 기준으로 그룹핑
            Map<Integer, List<Map<String, Object>>> classGroups = new HashMap<>();
            for (Map<String, Object> student : students) {
                int cNo = (int) student.get("classNo");
                classGroups.computeIfAbsent(cNo, k -> new ArrayList<>()).add(student);
            }

            // 각 반별로 등수 계산 (이미 total_score DESC 정렬 상태라 재정렬 불필요)
            for (List<Map<String, Object>> classStudents : classGroups.values()) {
                // 혹시 모르니 반별로도 정렬
                classStudents.sort((a, b) ->
                    Integer.compare((int) b.get("totalScore"), (int) a.get("totalScore"))
                );

                int classRank = 0;
                int prevClassScore = -1;
                int sameCnt = 0;

                for (Map<String, Object> student : classStudents) {
                    int score = (int) student.get("totalScore");

                    if (score != prevClassScore) {
                        classRank += (1 + sameCnt);
                        sameCnt = 0;
                    } else {
                        sameCnt++;
                    }

                    student.put("classRank", classRank);
                    prevClassScore = score;
                }
            }

            // 4. batchUpdate로 등수 UPDATE
            String updateSql = "UPDATE student_score_management " +
                               "SET grade_rank = ?, class_rank = ? " +
                               "WHERE idx = ?";

            for (Map<String, Object> student : students) {
                batchArgs.add(new Object[]{
                    student.get("gradeRank"),
                    student.get("classRank"),
                    student.get("idx")
                });
            }

            int[] updateCounts = jdbcTemplate.batchUpdate(updateSql, batchArgs);

            res.put("result", "success");
            res.put("updatedCount", updateCounts.length);

        } catch (Exception e) {
            e.printStackTrace();
            res.put("message", e.getMessage());
            res.put("result", "fail");
        }

        return res;
    }
}