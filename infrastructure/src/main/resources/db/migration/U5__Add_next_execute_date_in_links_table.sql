ALTER TABLE links DROP COLUMN next_execute_date;
DROP INDEX index_next_execute_date on links;