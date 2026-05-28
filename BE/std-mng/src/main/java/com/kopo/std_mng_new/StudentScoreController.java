package com.kopo.std_mng_new;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class StudentScoreController {

	@Autowired
	private FileUploadService fUpload;
	@Autowired
	private StudentListService std;
	@Autowired
	private StudentEditProcess stdEdit;
	@Autowired
	private StudentDelProcess stdDel;

	// csv file upload
	@PostMapping("/fileUpload")
	public ResponseEntity<Map<String, Object>> csvFileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("fileName") String fileName) {

		Map<String, Object> res = new HashMap<>();

		try {
			// 빈파일인지 검사
			if (file.isEmpty()) {
				res.put("result", "fail");
				res.put("error", "파일이 비어있습니다.");
				return ResponseEntity.badRequest().body(res);
			}

			res = fUpload.csvUploadProcess(fileName, file);

			return ResponseEntity.ok(res);

		} catch (Exception e) {
			e.printStackTrace();
			res.put("result", "fail");
			res.put("error", "CSV 데이터 저장 실패");
			res.put("message", e.getMessage());

			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// return student list
	@PostMapping("/getStdList")
	public ResponseEntity<Map<String, Object>> returnStudentList(@RequestBody FileTitleInfoDTO searchKeyword) {
		Map<String, Object> res = new HashMap<>();

		try {
			res = std.getStdList(searchKeyword);

			return ResponseEntity.ok(res);

		} catch (Exception e) {
			e.printStackTrace();
			res.put("result", "fail");
			res.put("error", "학생 성적 리스트를 가져오지 못했습니다.");
			res.put("message", e.getMessage());

			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// edit student info
	@PostMapping("/editStd")
	public ResponseEntity<Map<String, Object>> editStudentScore(@RequestBody Map<String, Object> body) {
		Map<String, Object> res = new HashMap<>();

		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> stdInfoMap = (Map<String, Object>) body.get("stdInfo");

			@SuppressWarnings("unchecked")
			Map<String, Object> scoreListMap = (Map<String, Object>) body.get("scoreList");

			if (stdInfoMap == null || scoreListMap == null) {
				throw new IllegalArgumentException("요청한 데이터(stdInfo 또는 scoreList)가 비어있습니다.");
			}

			Integer idx = ((Number) stdInfoMap.get("idx")).intValue();
			String studentId = (String) stdInfoMap.get("studentId");

			StudentKeyDTO stdInfo = new StudentKeyDTO(idx, studentId);

			Map<String, Object> scoreList = new HashMap<>();
			scoreList.put("korean_score", scoreListMap.get("korean_score"));
			scoreList.put("math_score", scoreListMap.get("math_score"));
			scoreList.put("english_score", scoreListMap.get("english_score"));

			res = stdEdit.studentEditProcess(stdInfo, scoreList);

			return ResponseEntity.ok(res);

		} catch (Exception e) {
			e.printStackTrace();
			res.put("result", "fail");
			res.put("error", "학생 점수를 수정하지 못했습니다.");
			res.put("message", e.getMessage());

			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// delete student
	@PostMapping("/delStd")
	public ResponseEntity<Map<String, Object>> delStudent(@RequestBody StudentKeyDTO stdInfo, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<>();
		try {
			
			String clientIp = request.getHeader("X-Forwarded-For");
			
	        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
	            clientIp = request.getHeader("Proxy-Client-IP");
	        }
	        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
	            clientIp = request.getRemoteAddr(); // 로컬 테스트 시 "0:0:0:0:0:0:0:1" 또는 "127.0.0.1"
	        }
	        
	        
			// 특정 사용자 정보 행 삭제
			res = stdDel.studentDelProcess(stdInfo, clientIp);

			return ResponseEntity.ok(res);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("result", "fail");
			res.put("error", "학생 데이터를 삭제하지 못했습니다.");
			res.put("message", e.getMessage());

			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
