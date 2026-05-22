package com.kopo.std_mng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UploadFile {

	// DB연동
	@Autowired
	private CsvFileService csvService;

	@PostMapping("/uploadCsv")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("fileName") String fileName) {
		try {
			// FE: formdata -> BE: MultipartFile
			if(file.isEmpty()) {
				return new ResponseEntity<>("파일이 비어있습니다.", HttpStatus.BAD_REQUEST);
			}
			
			csvService.saveCsvData(file, fileName);
            return new ResponseEntity<>("CSV 데이터 저장 완료", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
            return new ResponseEntity<>("업로드 중 오류 발생: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
