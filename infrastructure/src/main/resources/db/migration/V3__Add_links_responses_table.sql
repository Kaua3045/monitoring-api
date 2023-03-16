CREATE TABLE links_responses (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    response_message VARCHAR(255) NOT NULL,
    status_code INT NOT NULL,
    verified_date timestamptz NOT NULL,
    url_id VARCHAR(36) NOT NULL,
    CONSTRAINT fk_url_id FOREIGN KEY (url_id) REFERENCES links (id) ON DELETE CASCADE
);