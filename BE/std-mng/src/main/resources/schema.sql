CREATE TABLE IF NOT EXISTS grade_table (
    grade_code INT PRIMARY KEY,
    grade_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS semester_table (
    semester_code INT PRIMARY KEY,
    semester_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS exam_type_table (
    exam_code VARCHAR(10) PRIMARY KEY,
    exam_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS class_no_table (
    class_code INT PRIMARY KEY,
    class_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS school_grade_table (
    school_grade_code INT PRIMARY KEY,
    school_grade_name VARCHAR(50) NOT NULL,
    school_grade_ratio DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS subject_table (
    subject_code VARCHAR(10) PRIMARY KEY,
    subject_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS delete_student_table (
    log_idx INT AUTO_INCREMENT PRIMARY KEY,
    student_score_idx INT NOT NULL, --원본 성적 테이블의 idx

    grade_level INT NOT NULL,
    class_no INT NOT NULL,
    student_id VARCHAR(10)  NOT NULL,
    subject_name VARCHAR(50) NOT NULL,

    semester INT NOT NULL,
    exam_type VARCHAR(10) NOT NULL,

    client_ip VARCHAR(45) NOT NULL,
    deleted_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS student_score_management (
    idx INT AUTO_INCREMENT PRIMARY KEY,


    grade_level INT NOT NULL,
    class_no INT NOT NULL,
    student_id VARCHAR(50) NOT NULL,
    student_name VARCHAR(100) NOT NULL,

    semester INT NOT NULL,
    exam_type VARCHAR(10) NOT NULL,


    korean_score DOUBLE DEFAULT 0,
    english_score DOUBLE DEFAULT 0,
    math_score DOUBLE DEFAULT 0,

    total_score DOUBLE DEFAULT 0,
    average_score DOUBLE DEFAULT 0,

    grade_rank INT DEFAULT NULL,
    class_rank INT DEFAULT NULL,

    school_year INT NOT NULL,

    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    del_flag BOOLEAN DEFAULT FALSE,

    UNIQUE KEY uk_student_exam (
        student_id,
        school_year,
        semester,
        exam_type
    )
);