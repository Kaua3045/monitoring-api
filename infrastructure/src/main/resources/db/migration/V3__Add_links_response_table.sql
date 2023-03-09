CREATE TABLE links_responses (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    urlId VARCHAR(36) NOT NULL,
    response_message VARCHAR(36) NOT NULL,
    status_code INT NOT NULL
);