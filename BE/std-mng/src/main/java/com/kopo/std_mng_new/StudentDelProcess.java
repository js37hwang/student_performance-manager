package com.kopo.std_mng_new;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentDelProcess {

	@Autowired
	private StudentDelService stdDel;
	@Autowired
	private StudentRankService stdRankInsert;
	@Autowired
	private StudentDelLogService delLog;

	@Transactional
	public Map<String, Object> studentDelProcess(StudentKeyDTO stdInfo, String clientIp) {
		Map<String, Object> res = new HashMap<>();

		try {

			// 사용자 삭제
			Map<String, Object> delResult = stdDel.delStdData(stdInfo);

			if ("success".equals(delResult.get("result"))) {

				// 지금 수정한 행을 걍 쿼리로 가지고와서,... 필요한 정보만 뽑아서 넣어주기..
				// 삭제 로그
				delLog.writeDeleteLog((Map<String, Object>) delResult.get("deletedRow"),  clientIp);

				// 3. 등수 재계산
				stdRankInsert.updateScoreRank((FileTitleInfoDTO) delResult.get("rankKyes"));

				res.put("result", "success");

			} else {
				res.put("result", "fail");
			}

		} catch (Exception e) {

			e.printStackTrace();
			res.put("result", "fail");
			res.put("message", e.getMessage());
			throw new RuntimeException("fail");
		}
		return res;
	}

}
