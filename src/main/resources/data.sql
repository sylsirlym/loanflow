INSERT INTO tenure_duration_types(`tenure_duration_type`)VALUES('Day');
INSERT INTO tenure_duration_types(`tenure_duration_type`)VALUES('Month');
INSERT INTO tenure_duration_types(`tenure_duration_type`)VALUES('Year');

INSERT INTO fee_types (fee_type_name,fee_type) VALUES
                                          ('SERVICE_FEE','PERCENTAGE_ON_AMOUNT'),
                                          ('SERVICE_FEE','FIXED_AMOUNT'),
                                          ('DAILY_FEE','PERCENTAGE_ON_BALANCE'),
                                          ('DAILY_FEE','FIXED_AMOUNT'),
                                          ('LATE_FEE','PERCENTAGE_ON_BALANCE'),
                                          ('LATE_FEE','FIXED_AMOUNT');