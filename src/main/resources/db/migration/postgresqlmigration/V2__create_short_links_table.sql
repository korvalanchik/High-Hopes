-- --------------------------------
-- Table structure for short_links
-- --------------------------------
CREATE TABLE short_links (
     id BIGSERIAL PRIMARY KEY,
     user_id BIGINT NOT NULL,
     original_url VARCHAR(255) NOT NULL,
     short_url VARCHAR(255) UNIQUE NOT NULL,
     creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
     expiry_date TIMESTAMP WITH TIME ZONE,
     status BOOLEAN NOT NULL,
     clicks INT NOT NULL,
     FOREIGN KEY (user_id) REFERENCES users(id)
);
INSERT INTO short_links (user_id, original_url, short_url, creation_date, expiry_date, status, clicks)
VALUES
    (2, 'https://app.slack.com/client/T060L949SJY/C06TVJGDCPK', 'http://short.link/abc123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1' MONTH, TRUE, 4),
    (1, 'https://app.slack.com/client/T060L949SJY/C060L94BZAQ', 'http://short.link/def456', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1' MONTH, TRUE, 6);