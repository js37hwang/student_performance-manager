package com.kopo.std_mng_new;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

	// 필요한 service class 가져오기
	@Autowired
	private FileTitleParserService tParser;
	@Autowired
	private FileSaveService stdSave;
	@Autowired
	private StudentRankService stdRankInsert;

	@Transactional
	public Map<String, Object> csvUploadProcess(String fileName, MultipartFile file)
	{
		Map<String, Object> res = new HashMap<>();

		try {
			// 파일명 파싱
			FileTitleInfoDTO titleInfo = tParser.returnTitleInfo(fileName);

			// 디비에 데이터 적재
			stdSave.saveCsvData(file, titleInfo);

			// 학년/ 반 등수 계산하여 추가 저장
			stdRankInsert.updateScoreRank(titleInfo);

			res.put("result", "success");

		} catch (Exception e) {
			e.printStackTrace();
			res.put("result", "fail");
			throw new RuntimeException("fail");
		}
		return res;
	}
}
