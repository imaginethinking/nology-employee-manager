CREATE TABLE employees(
    id BINARY(16) NOT NULL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255),
    last_name VARCHAR(255) NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6)
)
