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
                           fee_type VARCHAR(50) NOT NULL,
                           when_to_charge VARCHAR(50) NOT NULL,
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
                         disbursement_type VARCHAR(50) NOT NULL,
                         disbursement_interval_in_days INT NULL,
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
                              fee_amount DECIMAL(19, 2) NULL,
                              fee_rate INT NULL,
                              active int NOT NULL DEFAULT '1',
                              PRIMARY KEY (product_fee_id),
                              FOREIGN KEY (product_id) REFERENCES products(product_id),
                              FOREIGN KEY (fee_type_id) REFERENCES fee_types(fee_type_id)
);

DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
                           customer_id BIGINT AUTO_INCREMENT,
                           first_name VARCHAR(255) NOT NULL,
                           last_name VARCHAR(255) NOT NULL,
                           email VARCHAR(255) NOT NULL UNIQUE,
                           address VARCHAR(500) NOT NULL,
                           active INT NOT NULL DEFAULT '1',
                           date_created TIMESTAMP NOT NULL DEFAULT NOW(),
                           created_by INT DEFAULT NULL,
                           date_modified TIMESTAMP DEFAULT NULL,
                           modified_by INT DEFAULT NULL,
                           PRIMARY KEY (customer_id)
);

DROP TABLE IF EXISTS profiles;

CREATE TABLE profiles (
                          profile_id BIGINT AUTO_INCREMENT,
                          customer_id BIGINT NOT NULL,
                          msisdn VARCHAR(20) NOT NULL,
                          pin_status INT NOT NULL,
                          pin_hash VARCHAR(255) NOT NULL,
                          credit_score DECIMAL(5, 2) NOT NULL,
                          active INT NOT NULL DEFAULT '1',
                          date_created TIMESTAMP NOT NULL DEFAULT NOW(),
                          created_by INT DEFAULT NULL,
                          date_modified TIMESTAMP DEFAULT NULL,
                          modified_by INT DEFAULT NULL,
                          PRIMARY KEY (profile_id),
                          FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

DROP TABLE IF EXISTS loan_offers;

CREATE TABLE loan_offers (
                             loan_offer_id BIGINT AUTO_INCREMENT,
                             profile_id BIGINT NOT NULL,
                             product_id BIGINT NOT NULL,
                             loan_limit DECIMAL(19, 2) NOT NULL,
                             active INT NOT NULL DEFAULT '1',
                             date_created TIMESTAMP NOT NULL DEFAULT NOW(),
                             created_by INT DEFAULT NULL,
                             date_modified TIMESTAMP DEFAULT NULL,
                             modified_by INT DEFAULT NULL,
                             PRIMARY KEY (loan_offer_id),
                             FOREIGN KEY (profile_id) REFERENCES profiles(profile_id),
                             FOREIGN KEY (product_id) REFERENCES products(product_id)
);

DROP TABLE IF EXISTS loans;

CREATE TABLE loans (
                       loan_id BIGINT AUTO_INCREMENT,
                       loan_offer_id BIGINT NOT NULL,
                       principal DECIMAL(19, 4) NOT NULL,
                       net_disbursed_amount DECIMAL(19, 4),
                       disbursement_date DATETIME,
                       grace_period_in_days INT,
                       due_date DATETIME,
                       loan_state VARCHAR(50),
                       active TINYINT DEFAULT 1,
                       date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
                       created_by INT,
                       date_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       modified_by INT,
                       PRIMARY KEY (loan_id),
                       FOREIGN KEY (loan_offer_id) REFERENCES loan_offers(loan_offer_id)
);


DROP TABLE IF EXISTS disbursements;

CREATE TABLE disbursements (
                       disbursement_id BIGINT AUTO_INCREMENT,
                       loan_id BIGINT NOT NULL,
                       amount DECIMAL(19, 4) NOT NULL,
                       active TINYINT DEFAULT 1,
                       date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
                       created_by INT,
                       date_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       modified_by INT,
                       PRIMARY KEY (disbursement_id),
                       FOREIGN KEY (loan_id) REFERENCES loans(loan_id)
);