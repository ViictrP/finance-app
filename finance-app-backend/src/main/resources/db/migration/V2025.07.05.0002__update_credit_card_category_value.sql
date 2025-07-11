-- Update the category value in the transaction table to match the enum
UPDATE finance_app.transaction SET category = 'CREDIT_CARD' WHERE category = 'credit-card';
