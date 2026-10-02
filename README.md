# Employee Manager

Employee Manager is a full-stack application for managing employees and their employment contract history.

## Features

- Create, view, update, and delete employees
- Store employee contact details and addresses
- Create and update employee contracts
- View an employee's full contract history
- View an employee's current active contract
- Prevent overlapping contracts
- Paginate employee and contract lists
- Search employees by name
- Filter employees by active contract status
- Database schema managed with Flyway migrations
- React frontend with simple responsive styling

## Contract Rules

A contract is active when:

```text
startDate <= today
AND
(endDate is null OR endDate >= today)
```

An employee cannot have overlapping contracts.

If a contract has no end date, it is treated as ongoing.

## Tech Stack

### Backend

- Java
- Spring Boot
- Spring Data JPA
- MySQL
- Flyway
- Maven

### Frontend

- React
- TypeScript
- Vite
- React Hook Form
- Zod
- Tailwind CSS

## Environment Variables

Create a `.env` file in the `backend` folder:

```properties
DB_HOST=localhost
DB_PORT=3306
DB_NAME=employee_manager
DB_USER=root
DB_PASSWORD=your_password
SPRING_PROFILE=dev
JWT_SECRET=your_secret
```

Create a `.env` file in the `frontend` folder:

```properties
VITE_API_URL=http://localhost:8080/api/v1
```

The test profile uses a separate MySQL database based on the main database name:

```text
employee_manager_test
```

## Running Locally

### Backend

Make sure MySQL is running.

From the `backend` folder:

```bash
mvn spring-boot:run
```

The backend runs on:

```text
http://localhost:8080
```

### Frontend

From the `frontend` folder:

```bash
npm install
npm run dev
```

The frontend runs on:

```text
http://localhost:5173
```

## Tests

From the `backend` folder:

```bash
mvn test
```

The tests use a separate MySQL test database and run against the same Flyway migrations as the main application.
