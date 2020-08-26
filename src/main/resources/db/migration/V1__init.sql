-- -----------------------------------------------------
-- Table `topic_db`.`topic`
-- -----------------------------------------------------
CREATE TABLE topic (
  id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  name VARCHAR(255) NULL DEFAULT NULL
);

-- -----------------------------------------------------
-- Table `topic_db`.`student`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS student (
  id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  birthday DATE NULL DEFAULT NULL,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE course (
  id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  name VARCHAR(255) NULL DEFAULT NULL,
  start_date DATE NULL DEFAULT NULL,
  topic_id BIGINT(20),
  CONSTRAINT fk_course_topic FOREIGN KEY (topic_id)
  REFERENCES topic(id)
);





-- -----------------------------------------------------
-- Table `topic_db`.`course_student`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS course_student (
  course_id BIGINT(20) NOT NULL references course,
  student_id BIGINT(20) NOT NULL references student,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);





