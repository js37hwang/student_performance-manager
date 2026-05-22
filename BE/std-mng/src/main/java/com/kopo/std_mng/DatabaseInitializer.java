package com.kopo.std_mng;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties; 

@WebListener
public class DatabaseInitializer implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("====== [DB] 테이블 생성 ======");

		// 🌟 1. application.properties 파일로부터 DB 설정 정보 읽어오기
		String url = "";
		String user = "";
		String password = "";

		try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
			Properties prop = new Properties();
			
			if (input == null) {
				System.err.println("오류: application.properties 파일을 찾을 수 없습니다.");
				return;
			}

			// 프로퍼티 파일 로드
			prop.load(input);

			// 파일에 적힌 key값으로 value 가져오기
			url = prop.getProperty("spring.datasource.url");
			user = prop.getProperty("spring.datasource.username");
			password = prop.getProperty("spring.datasource.password");

		} catch (Exception ex) {
			System.err.println("properties 파일 로드 중 에러 발생: " + ex.getMessage());
			ex.printStackTrace();
		}

		// 🌟 2. 쿼리 빌드 시작 (기존 코드가 이어서 진행됩니다)
		StringBuilder sql = new StringBuilder();

		// 1. 학년 테이블
		sql.append("CREATE TABLE IF NOT EXISTS grade_table (");
		sql.append("  grade_code VARCHAR(10) PRIMARY KEY,");
		sql.append("  grade_name VARCHAR(50) NOT NULL");
		sql.append("); ");
		sql.append("INSERT IGNORE INTO grade_table VALUES ('1', '1학년'), ('2', '2학년'), ('3', '3학년'); ");

		// 2. 학기 종류 테이블
		sql.append("CREATE TABLE IF NOT EXISTS semester_table (");
		sql.append("  semester_code VARCHAR(10) PRIMARY KEY,");
		sql.append("  semester_name VARCHAR(50) NOT NULL");
		sql.append("); ");
		sql.append("INSERT IGNORE INTO semester_table VALUES ('1', '1학기'), ('2', '2학기'); ");

		// 3. 시험 종류 테이블
		sql.append("CREATE TABLE IF NOT EXISTS exam_type_table (");
		sql.append("  exam_code VARCHAR(10) PRIMARY KEY,");
		sql.append("  exam_name VARCHAR(50) NOT NULL");
		sql.append("); ");
		sql.append("INSERT IGNORE INTO exam_type_table VALUES ('MID', '중간'), ('FIN', '기말'); ");

		// 4. 반 리스트 테이블
		sql.append("CREATE TABLE IF NOT EXISTS class_num_table (");
		sql.append("  class_code VARCHAR(10) PRIMARY KEY,");
		sql.append("  class_name VARCHAR(50) NOT NULL");
		sql.append("); ");
		sql.append("INSERT IGNORE INTO class_num_table VALUES ('01', '1반'), ('02', '2반'), ('03', '3반'), ('04', '4반'), ('05', '5반'); ");

		// 5. 내신 등급 기준 테이블
		sql.append("CREATE TABLE IF NOT EXISTS school_grade_table (");
		sql.append("  school_grade_code VARCHAR(10) PRIMARY KEY,");
		sql.append("  school_grade_name VARCHAR(50) NOT NULL,"); 
		sql.append("  school_grade_ratio VARCHAR(50) NOT NULL");
		sql.append("); ");
		sql.append("INSERT IGNORE INTO school_grade_table VALUES ('1', '1등급', '0.04'), ('2', '2등급', '0.11'), ('3', '3등급', '0.23'), ('4', '4등급', '0.4'), ('5', '5등급', '0.6'), ('6', '6등급', '0.77'), ('7', '7등급', '0.89'), ('8', '8등급', '0.96'), ('9', '9등급', '1.0'); ");

		// 6. 과목 테이블
		sql.append("CREATE TABLE IF NOT EXISTS subject_table (");
		sql.append("  subject_code VARCHAR(10) PRIMARY KEY,");
		sql.append("  subject_name VARCHAR(50) NOT NULL");
		sql.append("); ");
		sql.append("INSERT IGNORE INTO subject_table VALUES ('KOR_01', '국어'), ('MATH_01', '수학'), ('ENG_02', '영어'); ");

		// 7. 학생 성적 저장 테이블 
		sql.append("CREATE TABLE IF NOT EXISTS student_score_table (");
		sql.append("  id INT AUTO_INCREMENT PRIMARY KEY,");
		sql.append("  student_code VARCHAR(10) NOT NULL,");
		sql.append("  student_name VARCHAR(50) NOT NULL,"); 
		sql.append("  semester_name VARCHAR(10) NOT NULL,");
		sql.append("  exam_name VARCHAR(10) NOT NULL,");     
		sql.append("  class_code VARCHAR(10) NOT NULL,");    
		sql.append("  KOR_01 VARCHAR(10) NOT NULL,");
		sql.append("  MATH_01 VARCHAR(10) NOT NULL,");
		sql.append("  ENG_02 VARCHAR(10) NOT NULL,");
		sql.append("  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,"); 
		sql.append("  edit_date DATETIME");
		sql.append("); ");
		
		// 8. 학생 내신 저장 테이블
		sql.append("CREATE TABLE IF NOT EXISTS student_grade_table (");
		sql.append("  id INT AUTO_INCREMENT PRIMARY KEY,");
		sql.append("  student_code VARCHAR(10) NOT NULL,"); 
		sql.append("  total_grade_avg VARCHAR(10) NOT NULL,"); 
		sql.append("  KOR_01_grade_avg VARCHAR(10) NOT NULL,");
		sql.append("  MATH_01_grade_avg VARCHAR(10) NOT NULL,");
		sql.append("  ENG_02_grade_avg VARCHAR(10) NOT NULL");
		sql.append("); ");
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// 🌟 파일에서 읽어온 url, user, password 변수가 자동으로 바인딩됩니다.
			try (Connection conn = DriverManager.getConnection(url, user, password);
					Statement stmt = conn.createStatement()) {

				stmt.execute(sql.toString());
				System.out.println("====== [DB] 초기화 완료 ======");
			}
		} catch (Exception e) {
			System.err.println("DB 초기화 중 에러 발생: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}