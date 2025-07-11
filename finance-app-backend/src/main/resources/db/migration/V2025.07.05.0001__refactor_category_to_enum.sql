-- Rename column in transactions table
ALTER TABLE finance_app.transaction RENAME COLUMN category TO category_old;
ALTER TABLE finance_app.transaction ADD COLUMN category VARCHAR(255);
UPDATE finance_app.transaction SET category = category_old;
ALTER TABLE finance_app.transaction DROP COLUMN category_old;

-- Rename column in budget_categories table
ALTER TABLE finance_app.budget_categories RENAME COLUMN name TO category_old;
ALTER TABLE finance_app.budget_categories ADD COLUMN category VARCHAR(255);
UPDATE finance_app.budget_categories SET category = category_old;
ALTER TABLE finance_app.budget_categories DROP COLUMN category_old;

-- Rename column in budget_template_categories table
ALTER TABLE finance_app.budget_template_categories RENAME COLUMN name TO category_old;
ALTER TABLE finance_app.budget_template_categories ADD COLUMN category VARCHAR(255);
UPDATE finance_app.budget_template_categories SET category = category_old;
ALTER TABLE finance_app.budget_template_categories DROP COLUMN category_old;
