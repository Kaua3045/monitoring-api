ALTER TABLE links ADD next_execute_date timestamptz;
CREATE INDEX index_next_execute_date ON links (next_execute_date)