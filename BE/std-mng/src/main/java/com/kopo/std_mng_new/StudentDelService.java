package com.kopo.std_mng_new;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class StudentDelService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<String, Object> delStdData(StudentKeyDTO stdInfo) {
		Map<String, Object> res = new HashMap<>();

		try {
			// id+ 학번 일치 시 삭제 -> delFlag true/false 변경
			// 삭제 데이터 로그용 테이블도 필요하지 않나?
			// 삭제일시 삭제된 데이터 id 학번 삭제한사람 ip?

			String sql = "UPDATE student_score_management SET " + 
			"del_flag = TRUE, " + 
			"updated_at = NOW() "+
			"WHERE idx = ? " + 
			"AND student_id = ? ";

			jdbcTemplate.update(sql, stdInfo.getIdx(), stdInfo.getStudentId());

			sql = "select * from student_score_management where idx = ? AND student_id = ? AND del_flag = TRUE";

			Map<String, Object> deletedRow = jdbcTemplate.queryForMap(sql, stdInfo.getIdx(), stdInfo.getStudentId());

			
			FileTitleInfoDTO rankKeys = new FileTitleInfoDTO(
			    (Integer) deletedRow.get("grade_level"),
			    (Integer) deletedRow.get("class_no"),
			    (String)  deletedRow.get("exam_type"),
			    (Integer) deletedRow.get("school_year")
			);

			res.put("result", "success");
			res.put("rankKyes", rankKeys);
			res.put("deletedRow", deletedRow); 

		} catch (Exception e) {
			e.printStackTrace();
			res.put("message", e.getMessage());
			res.put("result", "fail");
		}

		return res;
	}
}
