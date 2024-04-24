-- --------------------------------
-- Table structure for short_links
-- --------------------------------
CREATE TABLE IF NOT EXISTS short_links (
     id BIGINT PRIMARY KEY,
     user_id BIGINT NOT NULL,
     original_url VARCHAR(255) NOT NULL,
     short_url VARCHAR(255) UNIQUE NOT NULL,
     creation_date TIMESTAMP NOT NULL,
     expiry_date TIMESTAMP,
     status BOOLEAN NOT NULL,
     clicks INT NOT NULL,
     FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Inserting sample data into short_links table
INSERT INTO short_links (id, user_id, original_url, short_url, creation_date, expiry_date, status, clicks)
VALUES
    (1, 2, 'https://app.slack.com/client/T060L949SJY/C06TVJGDCPK', 'http://localhost:8080/sl/abc123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '7' DAY, TRUE, 4),
    (2, 1, 'https://app.slack.com/client/T060L949SJY/C060L94BZAQ', 'http://localhost:8080/sl/def456', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '7' DAY, TRUE, 6),
    (3, 1, 'https://app.slack.com/client/T060L949SJY/C06F4SA1B55', 'http://localhost:8080/sl/qwe457', TIMESTAMP '2024-03-20 12:34:56', TIMESTAMP '2024-03-27 12:34:56', TRUE, 6);
