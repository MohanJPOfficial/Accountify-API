ALTER TABLE journal
    ADD COLUMN journal_name VARCHAR(255) NOT NULL DEFAULT '' AFTER journal_id;
