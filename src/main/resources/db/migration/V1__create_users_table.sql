-- ----------------------------
-- Table structure for user
-- ----------------------------
CREATE TABLE users (
    id bigint NOT NULL PRIMARY KEY,
    date_created datetime NOT NULL,
    last_updated datetime NOT NULL,
    password varchar(255) NOT NULL,
    username varchar(100) NOT NULL,
    role varchar(45) NOT NULL,
    enabled bool DEFAULT NULL
);
INSERT INTO users VALUES (1, '2024-01-01 10:51:56', '2024-01-01 10:51:56', '$2a$10$pJ3/4bGG/EPUxeZmbwmltewoqjxBj5zI.x7RgVz6ZM/88KaFl/2Ra', 'User','ROLE_USER',1);
INSERT INTO users VALUES (2, '2024-01-01 11:51:56', '2024-01-01 11:51:56', '$2a$10$pJ3/4bGG/EPUxeZmbwmltewoqjxBj5zI.x7RgVz6ZM/88KaFl/2Ra', 'Vasya','ROLE_USER',1);
INSERT INTO users VALUES (3, '2024-01-01 11:52:46', '2024-01-01 11:52:46', '$2a$10$1jBFKkUnm52LnsoinvStS.Kbd5oVE233hICGS0fLM5n5hojIjF/zy', 'Valerii','ROLE_USER',1);
