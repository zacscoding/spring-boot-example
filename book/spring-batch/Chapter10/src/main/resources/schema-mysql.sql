DROP TABLE IF EXISTS customer_account;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS customer;

CREATE TABLE IF NOT EXISTS customer  (
    customer_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY ,
    first_name VARCHAR(45) NOT NULL ,
    middle_name VARCHAR(45),
    last_name VARCHAR(45) NOT NULL,
    address1 VARCHAR(255) NOT NULL,
    address2 VARCHAR(255),
    city VARCHAR(50) NOT NULL,
    state VARCHAR(20) NOT NULL,
    postal_code CHAR(5) NOT NULL,
    ssn VARCHAR(11) NOT NULL,
    email_address VARCHAR(255),
    home_phone VARCHAR(12),
    cell_phone VARCHAR(12),
    work_phone VARCHAR(12),
    notification_pref CHAR(1) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ;

CREATE TABLE IF NOT EXISTS account  (
    account_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY ,
    balance FLOAT NOT NULL ,
    last_statement_date TIMESTAMP NOT NULL
) ;

CREATE TABLE IF NOT EXISTS transaction  (
    transaction_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY ,
    account_account_id BIGINT NOT NULL ,
    description VARCHAR(255) NOT NULL,
    credit FLOAT,
    debit FLOAT,
    timestamp TIMESTAMP
) ;

CREATE TABLE IF NOT EXISTS customer_account  (
    customer_customer_id BIGINT NOT NULL,
    account_account_id BIGINT NOT NULL
) ;