CREATE TABLE profiles (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    avatar_url VARCHAR,
    type VARCHAR(50) NOT NULL
);