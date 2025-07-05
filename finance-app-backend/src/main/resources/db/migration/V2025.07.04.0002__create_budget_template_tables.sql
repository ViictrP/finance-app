CREATE TABLE finance_app.budget_template (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    total_amount NUMERIC(19, 2) NOT NULL,
    CONSTRAINT fk_user_template FOREIGN KEY (user_id) REFERENCES finance_app.finance_app_user(id)
);

CREATE TABLE finance_app.budget_template_category (
    id BIGSERIAL PRIMARY KEY,
    template_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    budgeted_amount NUMERIC(19, 2) NOT NULL,
    CONSTRAINT fk_template FOREIGN KEY (template_id) REFERENCES finance_app.budget_template(id) ON DELETE CASCADE
);
