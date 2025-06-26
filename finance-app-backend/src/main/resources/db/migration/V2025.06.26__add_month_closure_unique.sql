ALTER TABLE finance_app.month_closure
ADD CONSTRAINT uq_month_closure_month_year_user UNIQUE (month, year, user_id);