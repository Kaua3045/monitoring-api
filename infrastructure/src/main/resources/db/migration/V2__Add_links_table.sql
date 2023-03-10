-- H2 not exists timestamptz
CREATE DOMAIN IF NOT EXISTS timestamptz AS TIMESTAMP;

CREATE TABLE links (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    url VARCHAR(500) NOT NULL,
    execute_date timestamptz NOT NULL,
    link_execution VARCHAR(80) NOT NULL,
    profile_id VARCHAR(36) NOT NULL,
    CONSTRAINT fk_profile_id FOREIGN KEY (profile_id) REFERENCES profiles (id) ON DELETE CASCADE
);