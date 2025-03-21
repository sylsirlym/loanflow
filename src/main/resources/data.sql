INSERT INTO tenure_duration_types(`tenure_duration_type`)VALUES('Day');
INSERT INTO tenure_duration_types(`tenure_duration_type`)VALUES('Month');
INSERT INTO tenure_duration_types(`tenure_duration_type`)VALUES('Year');

INSERT INTO fee_types (fee_type_name,fee_type,when_to_charge) VALUES
                                          ('SERVICE_FEE','PERCENTAGE_ON_AMOUNT','BEFORE'),
                                          ('SERVICE_FEE','FIXED_AMOUNT','BEFORE'),
                                          ('DAILY_FEE','PERCENTAGE_ON_BALANCE','DURING'),
                                          ('DAILY_FEE','FIXED_AMOUNT','DURING'),
                                          ('LATE_FEE','PERCENTAGE_ON_BALANCE','DURING'),
                                          ('LATE_FEE','FIXED_AMOUNT','DURING');