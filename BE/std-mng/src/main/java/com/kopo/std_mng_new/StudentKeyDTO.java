package com.kopo.std_mng_new;

public class StudentKeyDTO {
	
	private Integer idx; // idx
	private String studentId; // 학번
	
	public StudentKeyDTO() {
    }
	
	public StudentKeyDTO(Integer idx, String studentId) {
		this.idx = idx;
		this.studentId = studentId;
	}
	
	public Integer getIdx() {
		return idx;
	}

	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
}
