package com.kopo.std_mng_new;

import org.springframework.stereotype.Service;

@Service
public class FileTitleParserService {

	public FileTitleInfoDTO returnTitleInfo(String fileName) {
		FileTitleInfoDTO dto = new FileTitleInfoDTO();

		try {
			// 시험연도_학년_학기_중간or기말flag_반
			// {schoolYear : "시험 연도", gradeLevel : "1학년", classNo : "1반", examType : "중간", semester : "1학기" }
			
			// 확장자 제거 
			int dotIdx = fileName.lastIndexOf(".");
			String onlyFileName = (dotIdx != -1) ? fileName.substring(0, dotIdx) : fileName;
			
			String delimiter = "_";
			String[] tokens = onlyFileName.split(delimiter);

			dto.setSchoolYear(Integer.parseInt(tokens[0]));
			dto.setGradeLevel(Integer.parseInt(tokens[1]));
			dto.setSemester(Integer.parseInt(tokens[2]));
			dto.setExamType(tokens[3]);
			dto.setClassNo(Integer.parseInt(tokens[4]));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}
}
