CREATE TABLE IF NOT EXISTS customer (
    id            BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name     VARCHAR(11) NOT NULL,
    middle_initial VARCHAR(1),
    last_name      VARCHAR(20) NOT NULL,
    address       VARCHAR(45) NOT NULL,
    city          VARCHAR(16) NOT NULL,
    state         CHAR(2)     NOT NULL,
    zip       CHAR(5)
);