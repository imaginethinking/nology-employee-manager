package com.nology.employeemanager.contract;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import com.nology.employeemanager.employee.Address;
import com.nology.employeemanager.employee.ContactDetails;
import com.nology.employeemanager.employee.Employee;
import com.nology.employeemanager.employee.EmployeeRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ContractApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ContractRepository contractRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        contractRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    void create_whenRequestIsValid_returnsCreatedContractAndMatchesSchema() {
        Employee employee = employeeRepository.save(
            employee("employee@example.com"));

        LocalDate startDate = LocalDate.now().minusMonths(1);

        given()
            .contentType(ContentType.JSON)
            .body(contractRequest(
                startDate,
                null,
                BigDecimal.valueOf(37.5)))
            .when()
            .post("/api/v1/employees/{employeeId}/contracts", employee.getId())
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .body(matchesJsonSchemaInClasspath("schemas/contract-response.schema.json"))
            .body("contractType", equalTo("CONTRACT"))
            .body("startDate", equalTo(startDate.toString()))
            .body("employmentBasis", equalTo("FULL_TIME"))
            .body("isActive", equalTo(true));
    }

    @Test
    void create_whenEmployeeDoesNotExist_returnsNotFoundAndMatchesSchema() {
        UUID employeeId = UUID.randomUUID();

        given()
            .contentType(ContentType.JSON)
            .body(contractRequest(
                LocalDate.now().minusMonths(1),
                null,
                BigDecimal.valueOf(37.5)))
            .when()
            .post("/api/v1/employees/{employeeId}/contracts", employeeId)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body(matchesJsonSchemaInClasspath("schemas/error-response.schema.json"))
            .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
            .body("error", equalTo("Not Found"));
    }

    @Test
    void create_whenDatesOverlapExistingContract_returnsUnprocessableContent() {
        Employee employee = employeeRepository.save(
            employee("employee@example.com"));

        contractRepository.save(
            contract(
                employee,
                LocalDate.now().minusMonths(6),
                null));

        given()
            .contentType(ContentType.JSON)
            .body(contractRequest(
                LocalDate.now(),
                LocalDate.now().plusMonths(6),
                BigDecimal.valueOf(37.5)))
            .when()
            .post("/api/v1/employees/{employeeId}/contracts", employee.getId())
            .then()
            .statusCode(HttpStatus.UNPROCESSABLE_CONTENT.value())
            .body(matchesJsonSchemaInClasspath("schemas/error-response.schema.json"))
            .body("message", equalTo("Validation errors"))
            .body("details.contract", hasItem("Contract dates overlap with an existing contract"));
    }

    @Test
    void create_whenStartDateIsAfterEndDate_returnsUnprocessableContent() {
        Employee employee = employeeRepository.save(
            employee("employee@example.com"));

        LocalDate startDate = LocalDate.now().plusMonths(2);

        LocalDate endDate = LocalDate.now().plusMonths(1);

        given()
            .contentType(ContentType.JSON)
            .body(contractRequest(
                startDate,
                endDate,
                BigDecimal.valueOf(37.5)))
            .when()
            .post("/api/v1/employees/{employeeId}/contracts", employee.getId())
            .then()
            .statusCode(HttpStatus.UNPROCESSABLE_CONTENT.value())
            .body(matchesJsonSchemaInClasspath("schemas/error-response.schema.json"))
            .body("message", equalTo("Validation errors"))
            .body("details.contract", hasItem("Contract start date must be before or equal to contract end date"));
    }

    @Test
    void create_whenHoursPerWeekExceedsMaximum_returnsBadRequest() {
        Employee employee = employeeRepository.save(
            employee("employee@example.com"));

        given()
            .contentType(ContentType.JSON)
            .body(contractRequest(
                LocalDate.now().minusMonths(1),
                null,
                BigDecimal.valueOf(81)))
            .when()
            .post("/api/v1/employees/{employeeId}/contracts", employee.getId())
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getById_whenContractBelongsToAnotherEmployee_returnsNotFoundAndMatchesSchema() {
        Employee employee = employeeRepository.save(
            employee("employee@example.com"));

        Employee anotherEmployee = employeeRepository.save(
            employee("another@example.com"));

        Contract contract = contractRepository.save(
            contract(
                anotherEmployee,
                LocalDate.now().minusMonths(1),
                null));

        given()
            .when()
            .get("/api/v1/employees/{employeeId}/contracts/{contractId}", employee.getId(), contract.getId())
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body(matchesJsonSchemaInClasspath("schemas/error-response.schema.json"))
            .body("status", equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void update_whenRequestIsValid_returnsUpdatedContractAndMatchesSchema() {
        Employee employee = employeeRepository.save(
            employee("employee@example.com"));

        Contract contract = contractRepository.save(
            contract(
                employee,
                LocalDate.now().minusYears(1),
                null));

        LocalDate newStartDate = LocalDate.now().minusMonths(6);

        LocalDate newEndDate = LocalDate.now().plusMonths(6);

        given()
            .contentType(ContentType.JSON)
            .body(contractRequest(
                newStartDate,
                newEndDate,
                BigDecimal.valueOf(40)))
            .when()
            .patch("/api/v1/employees/{employeeId}/contracts/{contractId}", employee.getId(), contract.getId())
            .then()
            .statusCode(HttpStatus.OK.value())
            .body(matchesJsonSchemaInClasspath("schemas/contract-response.schema.json"))
            .body("id", equalTo(contract.getId().toString()))
            .body("startDate", equalTo(newStartDate.toString()))
            .body("endDate", equalTo(newEndDate.toString()))
            .body("hoursPerWeek", equalTo(40));
    }

    private Map<String, Object> contractRequest(
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal hoursPerWeek) {

        Map<String, Object> request = new HashMap<>();

        request.put(
            "contractType",
            "CONTRACT");

        request.put(
            "startDate",
            startDate.toString());

        request.put(
            "endDate",
            endDate == null
                ? null
                : endDate.toString());

        request.put(
            "employmentBasis",
            "FULL_TIME");

        request.put(
            "hoursPerWeek",
            hoursPerWeek);

        return request;
    }

    private Employee employee(String emailAddress) {
        Address address = new Address();

        address.setAddressLine1("1 Test Street");
        address.setCity("London");
        address.setPostcode("SW1A 1AA");
        address.setCountry("United Kingdom");

        ContactDetails contactDetails = new ContactDetails();

        contactDetails.setEmailAddress(emailAddress);
        contactDetails.setMobileNumber("07123456789");
        contactDetails.setAddress(address);

        Employee employee = new Employee();

        employee.setFirstName("Test");
        employee.setLastName("Employee");
        employee.setContactDetails(contactDetails);

        return employee;
    }

    private Contract contract(
        Employee employee,
        LocalDate startDate,
        LocalDate endDate) {

        Contract contract = new Contract();

        contract.setEmployee(employee);
        contract.setContractType(ContractType.CONTRACT);
        contract.setStartDate(startDate);
        contract.setEndDate(endDate);
        contract.setEmploymentBasis(EmploymentBasis.FULL_TIME);
        contract.setHoursPerWeek(BigDecimal.valueOf(37.5));

        return contract;
    }
}
