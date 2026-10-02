ALTER TABLE contracts
DROP FOREIGN KEY fk_contracts_employee;

ALTER TABLE contracts
ADD CONSTRAINT fk_contracts_employee
FOREIGN KEY (employee_id)
REFERENCES employees(id)
ON DELETE CASCADE;