INSERT IGNORE INTO grade_table VALUES (1, '1학년'), (2, '2학년'), (3, '3학년');

INSERT IGNORE INTO semester_table VALUES (1, '1학기'), (2, '2학기');

INSERT IGNORE INTO exam_type_table VALUES ('MID', '중간'), ('FIN', '기말');

INSERT IGNORE INTO class_no_table VALUES
(1, '1반'), (2, '2반'), (3, '3반'), (4, '4반'), (5, '5반');

INSERT IGNORE INTO school_grade_table VALUES
(1,'1등급',0.04),
(2,'2등급',0.11),
(3,'3등급',0.23),
(4,'4등급',0.40),
(5,'5등급',0.60),
(6,'6등급',0.77),
(7,'7등급',0.89),
(8,'8등급',0.96),
(9,'9등급',1.0);

INSERT IGNORE INTO subject_table VALUES
('KOR_01','국어'),
('MATH_01','수학'),
('ENG_02','영어');