package com.kopo.std_mng_new;

public class StudentDTO {

	private Integer idx; // idx
	private Integer gradeLevel; // 학년
	private Integer classNo; // 반
	private String studentId; // 학번
	private String studentName; // 이름

	private String examType; // MID 중간 / FIN 기말

	private Integer koreanScore; // 국어
	private Integer englishScore; // 영어
	private Integer mathScore; // 수학

	private Integer totalScore; // 총점
	private Double averageScore; // 평균

	private Integer gradeRank; // 학년 등수
	private Integer classRank; // 반 등수

	private Integer schoolYear; // 연도
	private Integer semester; // 학기

	private java.time.LocalDateTime createdAt; // 등록일
	private java.time.LocalDateTime updatedAt; // 수정일

	public StudentDTO() {
	}

	public Integer getIdx() {
		return idx;
	}

	public void setIdx(Integer idx) {
		this.idx = idx;
	}

	public Integer getGradeLevel() {
		return gradeLevel;
	}

	public void setGradeLevel(Integer gradeLevel) {
		this.gradeLevel = gradeLevel;
	}

	public Integer getClassNo() {
		return classNo;
	}

	public void setClassNo(Integer classNo) {
		this.classNo = classNo;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public Integer getKoreanScore() {
		return koreanScore;
	}

	public void setKoreanScore(Integer koreanScore) {
		this.koreanScore = koreanScore;
	}

	public Integer getEnglishScore() {
		return englishScore;
	}

	public void setEnglishScore(Integer englishScore) {
		this.englishScore = englishScore;
	}

	public Integer getMathScore() {
		return mathScore;
	}

	public void setMathScore(Integer mathScore) {
		this.mathScore = mathScore;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public Double getAverageScore() {
		return averageScore;
	}

	public void setAverageScore(Double averageScore) {
		this.averageScore = averageScore;
	}

	public Integer getGradeRank() {
		return gradeRank;
	}

	public void setGradeRank(Integer gradeRank) {
		this.gradeRank = gradeRank;
	}

	public Integer getClassRank() {
		return classRank;
	}

	public void setClassRank(Integer classRank) {
		this.classRank = classRank;
	}

	public Integer getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(Integer schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Integer getSemester() {
		return semester;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public java.time.LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(java.time.LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public java.time.LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(java.time.LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}