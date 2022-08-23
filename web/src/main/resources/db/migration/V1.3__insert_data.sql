INSERT INTO school (id, address, name, phone_number)
VALUES ('9b5a190f-afee-4b80-befe-827e32626bac', 'Tashkent', '1', '12354678'),
       ('fdd11fd8-d7a5-4b4c-857b-65a03235c99b', 'Navoi', '8', '987654321'),
       ('96368194-c131-414e-ae78-e066d7ff6d8b', 'Navoi', '16', '67854321');

INSERT INTO course (id, name, school_id)
VALUES ('ef7a32a4-52dd-4073-b839-2e30ae65048e', 'Maths', '9b5a190f-afee-4b80-befe-827e32626bac'),
       ('9bb5aa4a-4d81-456f-8c07-73578b0e105f', 'English', 'fdd11fd8-d7a5-4b4c-857b-65a03235c99b'),
       ('ac52155d-978f-4af0-b7fc-b0bd04bad283', 'General Russian', 'fdd11fd8-d7a5-4b4c-857b-65a03235c99b'),
       ('e9a69af7-7d7d-4a74-8434-9fea8605ea98', 'History', 'fdd11fd8-d7a5-4b4c-857b-65a03235c99b'),
       ('f78cb5e2-797a-4f86-806e-657c75271c55', 'Sociology', 'fdd11fd8-d7a5-4b4c-857b-65a03235c99b'),
       ('d7ff6356-1a77-4fbe-a2d2-9f0b7fa547bc', 'Music', 'fdd11fd8-d7a5-4b4c-857b-65a03235c99b'),
       ('7ef49be9-f32c-4809-8f0f-d3ae3ad69868', 'Physics', '96368194-c131-414e-ae78-e066d7ff6d8b');

INSERT INTO groups (id, level, name, school_id)
VALUES ('e94d0a42-411a-408a-a4d0-1de0009299ae', 3, '3-20', 'fdd11fd8-d7a5-4b4c-857b-65a03235c99b'),
       ('6260d6f1-b9b6-4512-9a3e-e9fbbe293f53', 5, '5-18', 'fdd11fd8-d7a5-4b4c-857b-65a03235c99b'),
       ('b1d51af8-4d76-40f2-b052-5e0dacfbb3f4', 4, '4-19', '96368194-c131-414e-ae78-e066d7ff6d8b'),
       ('c1afedd9-64fe-47b0-8bdf-a44109e20f55', 6, '6-11', '96368194-c131-414e-ae78-e066d7ff6d8b'),
       ('a06132da-01ee-47bd-8cea-8390b92a6976', 5, '7-15', '96368194-c131-414e-ae78-e066d7ff6d8b');

INSERT INTO student (id, full_name, group_id, level)
VALUES ('419f32d2-b585-4fde-a3f4-baeba4526160', 'Student 1', 'e94d0a42-411a-408a-a4d0-1de0009299ae', 3),
       ('d2f166c3-b32e-4860-a706-d700a70a380f', 'Student 2', 'e94d0a42-411a-408a-a4d0-1de0009299ae', 3),
       ('636d808c-7e7a-4b79-a468-4ac35a849955', 'Student 3', 'e94d0a42-411a-408a-a4d0-1de0009299ae', 3),
       ('b5ced431-4443-4b4a-ba54-435aa4b02f8a', 'Student 4', 'e94d0a42-411a-408a-a4d0-1de0009299ae', 3),
       ('f65a2cfe-4cc6-4006-9141-8ece12b992ab', 'Student 5', '6260d6f1-b9b6-4512-9a3e-e9fbbe293f53', 5),
       ('929f42f9-6f45-44dd-ac39-603a6e9e0ab3', 'Student 6', '6260d6f1-b9b6-4512-9a3e-e9fbbe293f53', 5),
       ('1f74c77a-3448-46cc-9322-6ac2ec546679', 'Student 7', '6260d6f1-b9b6-4512-9a3e-e9fbbe293f53', 5);

INSERT INTO student_courses (student_id, courses_id)
VALUES ('419f32d2-b585-4fde-a3f4-baeba4526160', '9bb5aa4a-4d81-456f-8c07-73578b0e105f'),
       ('d2f166c3-b32e-4860-a706-d700a70a380f', 'f78cb5e2-797a-4f86-806e-657c75271c55'),
       ('d2f166c3-b32e-4860-a706-d700a70a380f', '9bb5aa4a-4d81-456f-8c07-73578b0e105f'),
       ('636d808c-7e7a-4b79-a468-4ac35a849955', '9bb5aa4a-4d81-456f-8c07-73578b0e105f'),
       ('b5ced431-4443-4b4a-ba54-435aa4b02f8a', '9bb5aa4a-4d81-456f-8c07-73578b0e105f'),
       ('b5ced431-4443-4b4a-ba54-435aa4b02f8a', 'ac52155d-978f-4af0-b7fc-b0bd04bad283'),
       ('f65a2cfe-4cc6-4006-9141-8ece12b992ab', 'e9a69af7-7d7d-4a74-8434-9fea8605ea98'),
       ('f65a2cfe-4cc6-4006-9141-8ece12b992ab', 'f78cb5e2-797a-4f86-806e-657c75271c55'),
       ('929f42f9-6f45-44dd-ac39-603a6e9e0ab3', 'f78cb5e2-797a-4f86-806e-657c75271c55'),
       ('1f74c77a-3448-46cc-9322-6ac2ec546679', 'd7ff6356-1a77-4fbe-a2d2-9f0b7fa547bc'),
       ('1f74c77a-3448-46cc-9322-6ac2ec546679', '9bb5aa4a-4d81-456f-8c07-73578b0e105f');



