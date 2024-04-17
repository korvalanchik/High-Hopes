-- --------------------------------
-- Table structure for short_links
-- --------------------------------
CREATE TABLE short_links (
     id BIGINT PRIMARY KEY,
     user_id BIGINT NOT NULL,
     original_url VARCHAR(255) NOT NULL,
     short_url VARCHAR(255) UNIQUE NOT NULL,
     creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
     expiry_date TIMESTAMP WITH TIME ZONE,
     status BOOLEAN NOT NULL,
     clicks INT NOT NULL,
     FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO short_links (id, user_id, original_url, short_url, creation_date, expiry_date, status, clicks)
VALUES
(1, 1, 'https://docs.google.com/document/d/1xGYwCQuMeSyU4cHf4EvGdzRn0T9VKyVQ0Wgl3hcrMp0/edit', 'abc123', '2024-04-13 12:00:00', NULL, true, 0),
(2, 2, 'https://docs.google.com/document/d/1CmvPkcy-fo49BqlTwnC_iYBhj5KSmItM2V9Ps6lLtnI/edit', 'def456', '2024-04-13 12:00:00', NULL, false, 0),
(3, 3, 'https://docs.google.com/document/d/1CmvPkcy-fo49BqlTwnC_iYBhj5KSmItM2V9Ps6lLtnI/edit', 'ghi789', '2024-04-13 12:00:00', NULL, true, 0);