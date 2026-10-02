import { useEffect, useState } from 'react';
import './App.css';
import Employee from './components/Employee/Employee';
import EmployeeDetails from './components/EmployeeDetails/EmployeeDetails';
import EmployeeFilters from './components/EmployeeFilters/EmployeeFilters';
import NewEmployeeForm from './components/NewEmployeeForm/NewEmployeeForm';
import type { EmployeeFormData } from './components/NewEmployeeForm/schema';
import Pagination from './components/Pagination/Pagination';
import {
  createEmployee,
  getAllEmployees,
} from './services/employees';
import type { ActiveFilter, Employee as EmployeeType } from './types/employee';
import type { PageResponse } from './types/pagination';

function getInitialPage() {
  const page = Number(new URLSearchParams(window.location.search).get('page'));
  return page > 0 ? page : 1;
}

function getInitialSize() {
  const size = Number(new URLSearchParams(window.location.search).get('size'));
  return [5, 10, 20].includes(size) ? size : 10;
}

function getInitialActive(): ActiveFilter {
  const active = new URLSearchParams(window.location.search).get('active');

  if (active === 'true' || active === 'false') {
    return active;
  }

  return 'all';
}

function App() {
  const params = new URLSearchParams(window.location.search);

  const [employeePageData, setEmployeePageData] =
    useState<PageResponse<EmployeeType> | null>(null);
  const [page, setPage] = useState(getInitialPage);
  const [size, setSize] = useState(getInitialSize);
  const [search, setSearch] = useState(params.get('search') ?? '');
  const [searchInput, setSearchInput] = useState(params.get('search') ?? '');
  const [active, setActive] = useState<ActiveFilter>(getInitialActive);
  const [selectedEmployee, setSelectedEmployee] =
    useState<EmployeeType | null>(null);
  const [refreshCount, setRefreshCount] = useState(0);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const query = new URLSearchParams();

    query.set('page', String(page));
    query.set('size', String(size));

    if (search) {
      query.set('search', search);
    }

    if (active !== 'all') {
      query.set('active', active);
    }

    window.history.replaceState(
      null,
      '',
      `${window.location.pathname}?${query.toString()}`,
    );

    setError(null);

    getAllEmployees({ page, size, search, active })
      .then(setEmployeePageData)
      .catch(() => setError('Could not load employees'));
  }, [page, size, search, active, refreshCount]);

  const handleCreateEmployee = async (data: EmployeeFormData) => {
    await createEmployee(data);
    setPage(1);
    setRefreshCount((count) => count + 1);
  };

  const handleApplyFilters = () => {
    setSearch(searchInput.trim());
    setPage(1);
    setSelectedEmployee(null);
  };

  const handleClearFilters = () => {
    setSearchInput('');
    setSearch('');
    setActive('all');
    setSize(10);
    setPage(1);
    setSelectedEmployee(null);
  };

  return (
    <main className="app">
      <header className="page-header">
        <h1>Employee Manager</h1>
        <p>Browse employees and view their contract history.</p>
      </header>

      <NewEmployeeForm onSubmit={handleCreateEmployee} />

      <section>
        <h2>Employees</h2>

        <EmployeeFilters
          search={searchInput}
          active={active}
          size={size}
          onSearchChange={setSearchInput}
          onActiveChange={(value) => {
            setActive(value);
            setPage(1);
            setSelectedEmployee(null);
          }}
          onSizeChange={(value) => {
            setSize(value);
            setPage(1);
            setSelectedEmployee(null);
          }}
          onSubmit={handleApplyFilters}
          onClear={handleClearFilters}
        />

        {error && <p className="error-message">{error}</p>}

        {!error && employeePageData?.data.length === 0 && (
          <p>No employees found.</p>
        )}

        <div className="employee-list">
          {employeePageData?.data.map((employee) => (
            <Employee
              key={employee.id}
              employee={employee}
              onView={setSelectedEmployee}
            />
          ))}
        </div>

        {employeePageData && (
          <Pagination
            currentPage={employeePageData.currentPage}
            totalPages={employeePageData.totalPages}
            onPageChange={(newPage) => {
              setPage(newPage);
              setSelectedEmployee(null);
            }}
          />
        )}
      </section>

      {selectedEmployee && (
        <EmployeeDetails
          employee={selectedEmployee}
          onClose={() => setSelectedEmployee(null)}
        />
      )}
    </main>
  );
}

export default App;
