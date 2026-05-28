package com.kopo.std_mng_new;

public class FileTitleInfoDTO {

	// 연도
	private Integer schoolYear;

	// 학년 (예: 1, 2, 3)
	private Integer gradeLevel;
	
	// 학기...ㅎ
	private Integer semester;

	// 반 (예: 1, 2, 3)
	private Integer classNo;

	// 시험 구분 (MID / FIN)
	private String examType;

	public FileTitleInfoDTO() {
	}

	public FileTitleInfoDTO(Integer gradeLevel, Integer classNo, String examType, Integer schoolYear) {
		this.gradeLevel = gradeLevel;
		this.classNo = classNo;
		this.examType = examType;
		this.schoolYear = schoolYear;
	}

	public Integer getGradeLevel() {
		return gradeLevel;
	}

	public void setGradeLevel(Integer gradeLevel) {
		this.gradeLevel = gradeLevel;
	}
	
	public Integer getSemester() {
		return semester;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}


	public Integer getClassNo() {
		return classNo;
	}

	public void setClassNo(Integer classNo) {
		this.classNo = classNo;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public Integer getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(Integer schoolYear) {
		this.schoolYear = schoolYear;
	}

	@Override
	public String toString() {
		return "FileTitleInfoDTO{" + "gradeLevel=" + gradeLevel + ", classNo=" + classNo + ", examType='" + examType
				+ '\'' + ", schoolYear=" + schoolYear + '}';
	}
}