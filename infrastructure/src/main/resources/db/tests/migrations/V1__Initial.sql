CREATE TABLE profiles (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    avatar_url VARCHAR,
    type VARCHAR(50) NOT NULL
);