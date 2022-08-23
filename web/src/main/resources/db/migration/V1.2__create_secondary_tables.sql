CREATE TABLE IF NOT EXISTS student
(
    id        UUID        NOT NULL,
    full_name VARCHAR(25) NOT NULL,
    group_id  UUID,
    level     INTEGER     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (group_id) REFERENCES groups (id)
);

CREATE TABLE IF NOT EXISTS student_courses
(
    student_id UUID NOT NULL,
    courses_id UUID NOT NULL,
    FOREIGN KEY (student_id) REFERENCES student (id),
    FOREIGN KEY (courses_id) REFERENCES course (id)
);

CREATE TABLE IF NOT EXISTS teacher
(
    id                  UUID        NOT NULL,
    email               VARCHAR(255),
    full_name           VARCHAR(25) NOT NULL,
    office_phone_number VARCHAR(255),
    school_id           UUID,
    PRIMARY KEY (id),
    FOREIGN KEY (school_id) REFERENCES school (id)
);

CREATE TABLE IF NOT EXISTS teacher_courses
(
    teacher_id UUID NOT NULL,
    courses_id UUID NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES teacher (id),
    FOREIGN KEY (courses_id) REFERENCES course (id)
);

CREATE TABLE IF NOT EXISTS teacher_positions
(
    teacher_id UUID NOT NULL,
    positions  INTEGER,
    FOREIGN KEY (teacher_id) REFERENCES teacher (id)
);