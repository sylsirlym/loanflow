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


INSERT INTO notification_templates (event_type, channel, template_text, active, created_by, modified_by)
VALUES
    ('LOAN_CREATED', 'SMS', 'Your loan of {amount} has been received.', 1, 1, 1),
    ('LOAN_CREATED', 'EMAIL', 'Your loan of {amount} has been received.', 1, 1, 1),
    ('LOAN_CREATED', 'PUSH', 'Your loan of {amount} has been received.', 1, 1, 1),

    ('DUE_REMINDER', 'SMS', 'Reminder: Your loan payment of {amount} is due on {due_date}.', 1, 1, 1),
    ('DUE_REMINDER', 'EMAIL', 'Reminder: Your loan payment of {amount} is due on {due_date}.', 1, 1, 1),
    ('DUE_REMINDER', 'PUSH', 'Reminder: Your loan payment of {amount} is due on {due_date}.', 1, 1, 1),

    ('OVERDUE', 'SMS', 'Alert: Your {product_name} loan is overdue. Please make payment immediately.', 1, 1, 1),
    ('OVERDUE', 'EMAIL', 'Alert: Your {product_name} loan is overdue. Please make payment immediately.', 1, 1, 1),
    ('OVERDUE', 'PUSH', 'Alert: Your {product_name} loan is overdue. Please make payment immediately.', 1, 1, 1);