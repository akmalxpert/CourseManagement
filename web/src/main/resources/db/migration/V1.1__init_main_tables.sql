CREATE TABLE IF NOT EXISTS school
(
    id           UUID         NOT NULL,
    address      VARCHAR(255) NOT NULL,
    name         VARCHAR(25)  NOT NULL,
    phone_number VARCHAR(13)  NOT NULL,
    postal_code  VARCHAR(7),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS course
(
    id          UUID         NOT NULL,
    code        VARCHAR(15),
    description VARCHAR(255),
    name        VARCHAR(255) NOT NULL,
    school_id   UUID,
    PRIMARY KEY (id),
    FOREIGN KEY (school_id) REFERENCES school (id)
);

CREATE TABLE IF NOT EXISTS groups
(
    id        UUID NOT NULL,
    faculty   VARCHAR(255),
    level     integer,
    name      VARCHAR(255),
    school_id UUID,
    PRIMARY KEY (id),
    FOREIGN KEY (school_id) REFERENCES school (id)
);