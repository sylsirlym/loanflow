DROP TABLE IF EXISTS tenure_duration_types;

CREATE TABLE tenure_duration_types (
                         tenure_duration_type_id INT NOT NULL AUTO_INCREMENT,
                         tenure_duration_type VARCHAR(45) NOT NULL,
                         active INT NOT NULL DEFAULT '1',
                         date_created DATE DEFAULT CURRENT_DATE,
                         created_by INT DEFAULT NULL,
                         date_modified DATE DEFAULT CURRENT_DATE ON UPDATE CURRENT_DATE,
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
                           date_created DATE DEFAULT CURRENT_DATE,
                           created_by INT DEFAULT NULL,
                           date_modified DATE DEFAULT CURRENT_DATE ON UPDATE CURRENT_DATE,
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
                         billing_cycle VARCHAR(50) NOT NULL,
                         active int NOT NULL DEFAULT '1',
                         date_created DATE DEFAULT CURRENT_DATE,
                         created_by INT DEFAULT NULL,
                         date_modified DATE DEFAULT CURRENT_DATE ON UPDATE CURRENT_DATE,
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
                           date_created DATE DEFAULT CURRENT_DATE,
                           created_by INT DEFAULT NULL,
                           date_modified DATE DEFAULT CURRENT_DATE ON UPDATE CURRENT_DATE,
                           modified_by INT DEFAULT NULL,
                           PRIMARY KEY (customer_id)
);

DROP TABLE IF EXISTS profiles;

CREATE TABLE profiles (
                          profile_id BIGINT AUTO_INCREMENT,
                          customer_id BIGINT NOT NULL,
                          msisdn VARCHAR(20) NOT NULL,
                          pin_status INT NOT NULL,
                          pin_hash VARCHAR(255),
                          device_id VARCHAR(500),
                          preferred_notification_channel VARCHAR(100),
                          credit_score DECIMAL(5, 2) NOT NULL,
                          active INT NOT NULL DEFAULT '1',
                          date_created DATE DEFAULT CURRENT_DATE,
                          created_by INT DEFAULT NULL,
                          date_modified DATE DEFAULT CURRENT_DATE ON UPDATE CURRENT_DATE,
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
                             date_created DATE DEFAULT CURRENT_DATE,
                             created_by INT DEFAULT NULL,
                             date_modified DATE DEFAULT CURRENT_DATE ON UPDATE CURRENT_DATE,
                             modified_by INT DEFAULT NULL,
                             PRIMARY KEY (loan_offer_id),
                             FOREIGN KEY (profile_id) REFERENCES profiles(profile_id),
                             FOREIGN KEY (product_id) REFERENCES products(product_id)
);

DROP TABLE IF EXISTS loans;

CREATE TABLE loans (
                       loan_id BIGINT AUTO_INCREMENT,
                       loan_offer_id BIGINT NOT NULL,
                       principal DECIMAL(19, 2) NOT NULL,
                       net_disbursed_amount DECIMAL(19, 2),
                       disbursement_date DATETIME,
                       grace_period_in_days INT,
                       cooling_off_period_in_days INT,
                       due_date DATETIME,
                       loan_state VARCHAR(50),
                       is_fully_disbursed BOOLEAN DEFAULT FALSE,
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
                       amount DECIMAL(19, 2) NOT NULL,
                       scheduled_date DATE NOT NULL,
                       actual_disbursement_date DATE NULL,
                       is_disbursed BOOLEAN DEFAULT FALSE,
                       transaction_reference VARCHAR(200),
                       disbursement_status VARCHAR(20) DEFAULT 'PENDING',
                       active TINYINT DEFAULT 1,
                       date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
                       created_by INT,
                       date_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       modified_by INT,
                       PRIMARY KEY (disbursement_id),
                       FOREIGN KEY (loan_id) REFERENCES loans(loan_id)
);

DROP TABLE IF EXISTS repayment_schedules;

CREATE TABLE repayment_schedules (
                                     repayment_schedule_id BIGINT AUTO_INCREMENT,
                                     loan_id BIGINT NOT NULL,
                                     due_date DATE NOT NULL,
                                     installment_amount DECIMAL(19, 2) NOT NULL,
                                     paid BOOLEAN NOT NULL DEFAULT FALSE,
                                     active TINYINT DEFAULT 1,
                                     date_created DATE DEFAULT CURRENT_DATE,
                                     created_by INT,
                                     date_modified DATE DEFAULT CURRENT_DATE ON UPDATE CURRENT_DATE,
                                     modified_by INT,
                                     PRIMARY KEY (repayment_schedule_id),
                                    FOREIGN KEY (loan_id) REFERENCES loans(loan_id)
);

DROP TABLE IF EXISTS notifications;

CREATE TABLE notifications (
                               notification_id BIGINT AUTO_INCREMENT,
                               profile_id BIGINT NOT NULL,
                               message VARCHAR(500) NOT NULL,
                               channel VARCHAR(50) NOT NULL, -- SMS, EMAIL, PUSH
                               date_sent DATETIME DEFAULT NULL,
                               delivered BOOLEAN DEFAULT FALSE,
                               event_type VARCHAR(100) NOT NULL, -- LOAN_CREATED, DUE_REMINDER, etc.
                               active TINYINT DEFAULT 1,
                               date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
                               created_by INT,
                               date_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               modified_by INT,
                               PRIMARY KEY (notification_id),
                               FOREIGN KEY (profile_id) REFERENCES profiles(profile_id)
);

DROP TABLE IF EXISTS notification_templates;

CREATE TABLE notification_templates (
                                        notification_template_id BIGINT AUTO_INCREMENT,
                                        event_type VARCHAR(255) NOT NULL,
                                        channel VARCHAR(50) NOT NULL,
                                        template_text VARCHAR(1000) NOT NULL,
                                        active TINYINT(1) DEFAULT 1,
                                        date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
                                        created_by INT,
                                        date_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        modified_by INT,
                                        PRIMARY KEY (notification_template_id)
);

