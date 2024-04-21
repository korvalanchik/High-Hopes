-- ----------------------------
-- Table structure for user
-- ----------------------------
CREATE TABLE users (
    id bigint NOT NULL PRIMARY KEY,
    date_created datetime NOT NULL,
    last_updated datetime NOT NULL,
    password varchar(255) NOT NULL,
    username varchar(100) NOT NULL
);
INSERT INTO users VALUES (1, '2024-01-01 10:51:56', '2024-01-01 10:51:56', '$2a$10$OSYyd.176WpWCeIh4Vy2Le9Rw49F1TdasGJbXI2KLzSIvkDh5PUpq', 'User');
INSERT INTO users VALUES (2, '2024-01-01 11:51:56', '2024-01-01 11:51:56', '$2a$10$VlCGdMC01TUgNTM47J76T.fpr/L1/WAGimKhoRIKgUwKR1Zon4GE2', 'Vasya' );
INSERT INTO users VALUES (3, '2024-01-01 11:52:46', '2024-01-01 11:52:46', '$2a$10$1jBFKkUnm52LnsoinvStS.Kbd5oVE233hICGS0fLM5n5hojIjF/zy', 'Valerii' );
