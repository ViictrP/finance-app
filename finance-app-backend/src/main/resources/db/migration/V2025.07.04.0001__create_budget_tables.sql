CREATE TABLE finance_app.budget (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    month VARCHAR(7) NOT NULL,
    total_amount NUMERIC(19, 2) NOT NULL,
    CONSTRAINT uq_user_month UNIQUE (user_id, month),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES finance_app.finance_app_user(id)
);

CREATE TABLE finance_app.budget_category (
    id BIGSERIAL PRIMARY KEY,
    budget_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    budgeted_amount NUMERIC(19, 2) NOT NULL,
    CONSTRAINT fk_budget FOREIGN KEY (budget_id) REFERENCES finance_app.budget(id) ON DELETE CASCADE
);