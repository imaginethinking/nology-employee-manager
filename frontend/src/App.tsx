import { useState } from 'react';

import EmployeeDetails from './components/EmployeeDetails/EmployeeDetails';
import EmployeeList from './components/EmployeeList/EmployeeList';
import type { Employee } from './types/employee';

function App() {
    const [selectedEmployee, setSelectedEmployee] = useState<Employee | null>(null);

    return (
        <main className="mx-auto max-w-5xl px-4 py-8">
            <h1 className="mb-6 text-3xl font-bold">Employee Manager</h1>

            {selectedEmployee ? (
                <EmployeeDetails
                    employee={selectedEmployee}
                    onEmployeeChange={setSelectedEmployee}
                    onBack={() => setSelectedEmployee(null)}
                />
            ) : (
                <EmployeeList onSelectEmployee={setSelectedEmployee} />
            )}
        </main>
    );
}

export default App;
