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