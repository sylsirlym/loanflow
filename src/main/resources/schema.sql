DROP TABLE IF EXISTS tenure_duration_types;

CREATE TABLE tenure_duration_types (
                         tenure_duration_type_id INT NOT NULL AUTO_INCREMENT,
                         tenure_duration_type VARCHAR(45) NOT NULL,
                         active INT NOT NULL DEFAULT '1',
                         date_created TIMESTAMP NOT NULL DEFAULT NOW(),
                         created_by INT DEFAULT NULL,
                         date_modified TIMESTAMP DEFAULT NULL,
                         modified_by INT DEFAULT NULL,
                         PRIMARY KEY (tenure_duration_type_id)
);


DROP TABLE IF EXISTS products;

CREATE TABLE products (
                         product_id BIGINT AUTO_INCREMENT,
                         name VARCHAR(255) NOT NULL,
                         tenure_duration INT NOT NULL,
                         tenure_duration_type_id INT NOT NULL,
                         service_fee DECIMAL(19, 2) NOT NULL,
                         daily_fee DECIMAL(19, 2) NOT NULL,
                         late_fee DECIMAL(19, 2) NOT NULL,
                         days_after_due_for_fee_application INT NOT NULL,
                         active int NOT NULL DEFAULT '1',
                         date_created TIMESTAMP NOT NULL DEFAULT NOW(),
                         created_by INT DEFAULT NULL,
                         date_modified TIMESTAMP DEFAULT NULL,
                         modified_by INT DEFAULT NULL,
                         PRIMARY KEY (product_id)
);