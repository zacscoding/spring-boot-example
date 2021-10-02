DELIMITER //

CREATE PROCEDURE customer_list(IN cityOption CHAR(16))
    BEGIN
        SELECT * FROM CUSTOMER
        WHERE city = cityOption;
END //

DELIMITER ;