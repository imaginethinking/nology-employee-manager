CREATE TABLE contracts(
    id BINARY(16) NOT NULL PRIMARY KEY,
    employee_id BINARY(16) NOT NULL,
    contract_type VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    employment_basis VARCHAR(255) NOT NULL,
    hours_per_week DECIMAL(4, 1) NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_contracts_employee FOREIGN KEY (employee_id) REFERENCES employees(id)
)
