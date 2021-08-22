DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts (
    account_id int unsigned auto_increment PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    created_at datetime NULL,
    updated_at datetime NULL
) CHARACTER SET utf8mb4;
-- CREATE INDEX idx_accounts_last_name ON accounts(last_name);