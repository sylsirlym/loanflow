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

DROP TABLE IF EXISTS fee_types;

CREATE TABLE fee_types (
                           fee_type_id INT AUTO_INCREMENT,
                           fee_type_name VARCHAR(50) NOT NULL,
                           active INT NOT NULL DEFAULT '1',
                           date_created TIMESTAMP NOT NULL DEFAULT NOW(),
                           created_by INT DEFAULT NULL,
                           date_modified TIMESTAMP DEFAULT NULL,
                           modified_by INT DEFAULT NULL,
                           PRIMARY KEY (fee_type_id)
);

DROP TABLE IF EXISTS products;

CREATE TABLE products (
                         product_id BIGINT AUTO_INCREMENT,
                         name VARCHAR(255) NOT NULL,
                         tenure_duration INT NOT NULL,
                         tenure_duration_type_id INT NOT NULL,
                         days_after_due_for_fee_application INT NOT NULL,
                         active int NOT NULL DEFAULT '1',
                         date_created TIMESTAMP NOT NULL DEFAULT NOW(),
                         created_by INT DEFAULT NULL,
                         date_modified TIMESTAMP DEFAULT NULL,
                         modified_by INT DEFAULT NULL,
                         PRIMARY KEY (product_id)
);



DROP TABLE IF EXISTS product_fees;

CREATE TABLE product_fees (
                              product_fee_id BIGINT AUTO_INCREMENT,
                              product_id BIGINT NOT NULL,
                              fee_type_id INT NOT NULL,
                              fee_amount DECIMAL(19, 2) NOT NULL,
                              fee_currency VARCHAR(5) NOT NULL,
                              active int NOT NULL DEFAULT '1',
                              PRIMARY KEY (product_fee_id),
                              FOREIGN KEY (product_id) REFERENCES products(product_id),
                              FOREIGN KEY (fee_type_id) REFERENCES fee_types(fee_type_id)
);
