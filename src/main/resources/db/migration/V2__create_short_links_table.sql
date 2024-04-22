-- --------------------------------
-- Table structure for short_links
-- --------------------------------
CREATE TABLE short_links (
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

INSERT INTO short_links (id, user_id, original_url, short_url, creation_date, expiry_date, status, clicks)
VALUES
    (1, 2, 'https://app.slack.com/client/T060L949SJY/C06TVJGDCPK', 'http://localhost:8080/api/shortLinks/resolve/short.link/abcd1234', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1' MONTH, TRUE, 4),
    (2, 1, 'https://app.slack.com/client/T060L949SJY/C060L94BZAQ', 'http://short.link/def456', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1' MONTH, TRUE, 6);
