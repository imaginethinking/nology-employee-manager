package com.nology.employeemanager.employee;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

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

import com.nology.employeemanager.contract.Contract;
import com.nology.employeemanager.contract.ContractRepository;
import com.nology.employeemanager.contract.ContractType;
import com.nology.employeemanager.contract.EmploymentBasis;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class EmployeeApiTest {

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
	void create_whenRequestIsValid_returnsCreatedEmployeeAndMatchesSchema() {
		Map<String, Object> request = employeeRequest(
			"Alice",
			"Smith",
			"alice@example.com");

		given()
			.contentType(ContentType.JSON)
			.body(request)
			.when()
			.post("/api/v1/employees")
			.then()
			.statusCode(HttpStatus.CREATED.value())
			.body(matchesJsonSchemaInClasspath("schemas/employee-response.schema.json"))
			.body("firstName", equalTo("Alice"))
			.body("lastName", equalTo("Smith"))
			.body("contactDetails.emailAddress", equalTo("alice@example.com"));
	}

	@Test
	void create_whenRequiredDataIsMissing_returnsBadRequest() {
		Map<String, Object> request = new HashMap<>();

		request.put("firstName", "Alice");

		given()
			.contentType(ContentType.JSON)
			.body(request)
			.when()
			.post("/api/v1/employees")
			.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void getById_whenEmployeeExists_returnsEmployeeAndMatchesSchema() {
		Employee employee = employeeRepository.save(
			employee(
				"Alice",
				"Smith",
				"alice@example.com"));

		given()
			.when()
			.get("/api/v1/employees/{employeeId}", employee.getId())
			.then()
			.statusCode(HttpStatus.OK.value())
			.body(matchesJsonSchemaInClasspath("schemas/employee-response.schema.json"))
			.body("id", equalTo(employee.getId().toString()))
			.body("firstName", equalTo("Alice"))
			.body("lastName", equalTo("Smith"));
	}

	@Test
	void getById_whenEmployeeDoesNotExist_returnsNotFoundAndMatchesSchema() {
		UUID employeeId = UUID.randomUUID();

		given()
			.when()
			.get("/api/v1/employees/{employeeId}", employeeId)
			.then()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body(matchesJsonSchemaInClasspath("schemas/error-response.schema.json"))
			.body("status", equalTo(HttpStatus.NOT_FOUND.value()))
			.body("error", equalTo("Not Found"));
	}

	@Test
	void getPagedEmployees_whenSearchMatchesName_returnsMatchingEmployee() {
		employeeRepository.save(
			employee(
				"Alice",
				"Smith",
				"alice@example.com"));

		employeeRepository.save(
			employee(
				"Bob",
				"Jones",
				"bob@example.com"));

		given()
			.queryParam("search", "sMiTh")
			.when()
			.get("/api/v1/employees")
			.then()
			.statusCode(HttpStatus.OK.value())
			.body(matchesJsonSchemaInClasspath("schemas/employee-page-response.schema.json"))
			.body("data", hasSize(1))
			.body("totalResults", equalTo(1))
			.body("data[0].firstName", equalTo("Alice"))
			.body("data[0].lastName", equalTo("Smith"));
	}

	@Test
	void getPagedEmployees_whenActiveIsTrue_returnsOnlyEmployeesWithActiveContract() {
		Employee activeEmployee = employeeRepository.save(
			employee(
				"Alice",
				"Active",
				"active@example.com"));

		employeeRepository.save(
			employee(
				"Ian",
				"Inactive",
				"inactive@example.com"));

		contractRepository.save(
			activeContract(activeEmployee));

		given()
			.queryParam("active", true)
			.when()
			.get("/api/v1/employees")
			.then()
			.statusCode(HttpStatus.OK.value())
			.body(matchesJsonSchemaInClasspath("schemas/employee-page-response.schema.json"))
			.body("data", hasSize(1))
			.body("totalResults", equalTo(1))
			.body("data[0].firstName", equalTo("Alice"));
	}

	@Test
	void getPagedEmployees_whenActiveIsFalse_returnsOnlyEmployeesWithoutActiveContract() {
		Employee activeEmployee = employeeRepository.save(
			employee(
				"Alice",
				"Active",
				"active@example.com"));

		employeeRepository.save(
			employee(
				"Ian",
				"Inactive",
				"inactive@example.com"));

		contractRepository.save(
			activeContract(activeEmployee));

		given()
			.queryParam("active", false)
			.when()
			.get("/api/v1/employees")
			.then()
			.statusCode(HttpStatus.OK.value())
			.body(matchesJsonSchemaInClasspath("schemas/employee-page-response.schema.json"))
			.body("data", hasSize(1))
			.body("totalResults", equalTo(1))
			.body("data[0].firstName", equalTo("Ian"));
	}

	@Test
	void getPagedEmployees_whenPageIsBeyondTotalPages_returnsUnprocessableContent() {
		employeeRepository.save(
			employee(
				"Alice",
				"Smith",
				"alice@example.com"));

		given()
			.queryParam("page", 2)
			.queryParam("size", 10)
			.when()
			.get("/api/v1/employees")
			.then()
			.statusCode(HttpStatus.UNPROCESSABLE_CONTENT.value())
			.body(matchesJsonSchemaInClasspath("schemas/error-response.schema.json"))
			.body("status", equalTo(HttpStatus.UNPROCESSABLE_CONTENT.value()));
	}

	private Map<String, Object> employeeRequest(
		String firstName,
		String lastName,
		String emailAddress) {

		Map<String, Object> address = new HashMap<>();
		address.put("addressLine1", "1 Test Street");
		address.put("addressLine2", null);
		address.put("city", "London");
		address.put("postcode", "SW1A 1AA");
		address.put("country", "United Kingdom");

		Map<String, Object> contactDetails = new HashMap<>();
		contactDetails.put("emailAddress", emailAddress);
		contactDetails.put("mobileNumber", "07123456789");
		contactDetails.put("address", address);

		Map<String, Object> employee = new HashMap<>();
		employee.put("firstName", firstName);
		employee.put("middleName", null);
		employee.put("lastName", lastName);
		employee.put("contactDetails", contactDetails);

		return employee;
	}

	private Employee employee(
		String firstName,
		String lastName,
		String emailAddress) {

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
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setContactDetails(contactDetails);

		return employee;
	}

	private Contract activeContract(Employee employee) {
		Contract contract = new Contract();

		contract.setEmployee(employee);
		contract.setContractType(ContractType.CONTRACT);
		contract.setStartDate(LocalDate.now().minusMonths(1));
		contract.setEndDate(null);
		contract.setEmploymentBasis(EmploymentBasis.FULL_TIME);
		contract.setHoursPerWeek(BigDecimal.valueOf(37.5));

		return contract;
	}
}
