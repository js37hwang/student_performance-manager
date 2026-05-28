package com.kopo.std_mng_new;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentEditProcess {

	@Autowired
	private StudentEditService stdEdit;
	@Autowired
	private StudentRankService stdRankInsert;

	@Transactional
	public Map<String, Object> studentEditProcess(StudentKeyDTO stdInfo, Map<String, Object> scoreList) {

		Map<String, Object> res = new HashMap<>();

		try {

			// 1. 점수 수정
			Map<String, Object> editResult = stdEdit.editStdData(stdInfo, scoreList);

			// 2. 결과 체크
			if ("success".equals(editResult.get("result"))) {
				
				// 지금 수정한 행을 걍 쿼리로 가지고와서,... 필요한 정보만 뽑아서 넣어주기..

				// 3. 등수 재계산
				stdRankInsert.updateScoreRank( (FileTitleInfoDTO) editResult.get("rankKyes") );

				res.put("result", "success");

			} else {
				res.put("result", "fail");
			}

		} catch (Exception e) {
			e.printStackTrace();
			res.put("result", "fail");
			res.put("message", e.getMessage());

			throw new RuntimeException("fail", e);
		}

		return res;
	}
}