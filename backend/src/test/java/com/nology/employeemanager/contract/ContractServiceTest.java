package com.nology.employeemanager.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.nology.employeemanager.common.exceptions.NotFoundException;
import com.nology.employeemanager.common.exceptions.ServiceValidationException;
import com.nology.employeemanager.contract.dtos.ContractResponse;
import com.nology.employeemanager.contract.dtos.CreateContractRequest;
import com.nology.employeemanager.contract.dtos.UpdateContractRequest;
import com.nology.employeemanager.employee.Employee;
import com.nology.employeemanager.employee.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

	@Mock
	private ContractRepository contractRepository;

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private ContractService contractService;

	@Test
	void create_whenEmployeeDoesNotExist_throwsNotFoundExceptionAndDoesNotSave() {
		UUID employeeId = UUID.randomUUID();

		CreateContractRequest request = createRequest(
				LocalDate.now().minusMonths(1),
				LocalDate.now().plusMonths(6));

		when(employeeRepository.findById(employeeId))
				.thenReturn(Optional.empty());

		assertThrows(
				NotFoundException.class,
				() -> contractService.create(employeeId, request));

		verifyNoInteractions(modelMapper);

		verify(contractRepository, never())
				.save(any(Contract.class));
	}

	@Test
	void create_whenDatesDoNotOverlap_savesContractAndReturnsResponse() {
		UUID employeeId = UUID.randomUUID();
		Employee employee = employee(employeeId);

		CreateContractRequest request = createRequest(
				LocalDate.now().minusMonths(1),
				LocalDate.now().plusMonths(6));

		Contract mappedContract = contract(
				null,
				employee,
				request.getStartDate(),
				request.getEndDate());

		Contract savedContract = contract(
				UUID.randomUUID(),
				employee,
				request.getStartDate(),
				request.getEndDate());

		when(employeeRepository.findById(employeeId))
				.thenReturn(Optional.of(employee));

		when(contractRepository.existsOverlappingContract(
				employeeId,
				request.getStartDate(),
				request.getEndDate(),
				null))
				.thenReturn(false);

		when(modelMapper.map(request, Contract.class))
				.thenReturn(mappedContract);

		when(contractRepository.save(mappedContract))
				.thenReturn(savedContract);

		ContractResponse response = contractService.create(
				employeeId,
				request);

		assertEquals(savedContract.getId(), response.id());
		assertEquals(employee, mappedContract.getEmployee());

		verify(modelMapper).map(request, Contract.class);
		verify(contractRepository).save(mappedContract);
	}

	@Test
	void create_whenStartDateIsAfterEndDate_throwsServiceValidationExceptionAndDoesNotSave() {
		UUID employeeId = UUID.randomUUID();
		Employee employee = employee(employeeId);

		CreateContractRequest request = createRequest(
				LocalDate.now().plusMonths(2),
				LocalDate.now().plusMonths(1));

		when(employeeRepository.findById(employeeId))
				.thenReturn(Optional.of(employee));

		when(contractRepository.existsOverlappingContract(
				employeeId,
				request.getStartDate(),
				request.getEndDate(),
				null))
				.thenReturn(false);

		ServiceValidationException exception = assertThrows(
				ServiceValidationException.class,
				() -> contractService.create(employeeId, request));

		assertEquals(
				"Contract start date must be before or equal to contract end date",
				exception.getErrors().get("contract").get(0));

		verifyNoInteractions(modelMapper);

		verify(contractRepository, never())
				.save(any(Contract.class));
	}

	@Test
	void create_whenDatesOverlapExistingContract_throwsServiceValidationExceptionAndDoesNotSave() {
		UUID employeeId = UUID.randomUUID();
		Employee employee = employee(employeeId);

		CreateContractRequest request = createRequest(
				LocalDate.now().minusMonths(1),
				LocalDate.now().plusMonths(6));

		when(employeeRepository.findById(employeeId))
				.thenReturn(Optional.of(employee));

		when(contractRepository.existsOverlappingContract(
				employeeId,
				request.getStartDate(),
				request.getEndDate(),
				null))
				.thenReturn(true);

		ServiceValidationException exception = assertThrows(
				ServiceValidationException.class,
				() -> contractService.create(employeeId, request));

		assertEquals(
				"Contract dates overlap with an existing contract",
				exception.getErrors().get("contract").get(0));

		verifyNoInteractions(modelMapper);

		verify(contractRepository, never())
				.save(any(Contract.class));
	}

	@Test
	void update_whenContractBelongsToAnotherEmployee_throwsNotFoundExceptionAndDoesNotSave() {
		UUID employeeId = UUID.randomUUID();
		UUID anotherEmployeeId = UUID.randomUUID();
		UUID contractId = UUID.randomUUID();

		Employee employee = employee(employeeId);
		Employee anotherEmployee = employee(anotherEmployeeId);

		Contract existingContract = contract(
				contractId,
				anotherEmployee,
				LocalDate.now().minusYears(1),
				null);

		UpdateContractRequest request = updateRequest(
				LocalDate.now().minusMonths(1),
				null);

		when(employeeRepository.findById(employeeId))
				.thenReturn(Optional.of(employee));

		when(contractRepository.findById(contractId))
				.thenReturn(Optional.of(existingContract));

		assertThrows(
				NotFoundException.class,
				() -> contractService.update(
						employeeId,
						contractId,
						request));

		verify(contractRepository, never())
				.existsOverlappingContract(
						any(),
						any(),
						any(),
						any());

		verifyNoInteractions(modelMapper);

		verify(contractRepository, never())
				.save(any(Contract.class));
	}

	@Test
	void update_whenRequestIsValid_excludesCurrentContractAndSaves() {
		UUID employeeId = UUID.randomUUID();
		UUID contractId = UUID.randomUUID();

		Employee employee = employee(employeeId);

		Contract existingContract = contract(
				contractId,
				employee,
				LocalDate.now().minusYears(1),
				null);

		UpdateContractRequest request = updateRequest(
				LocalDate.now().minusMonths(6),
				LocalDate.now().plusMonths(6));

		when(employeeRepository.findById(employeeId))
				.thenReturn(Optional.of(employee));

		when(contractRepository.findById(contractId))
				.thenReturn(Optional.of(existingContract));

		when(contractRepository.existsOverlappingContract(
				employeeId,
				request.getStartDate(),
				request.getEndDate(),
				contractId))
				.thenReturn(false);

		when(contractRepository.save(existingContract))
				.thenReturn(existingContract);

		ContractResponse response = contractService.update(
				employeeId,
				contractId,
				request);

		verify(contractRepository)
				.existsOverlappingContract(
						employeeId,
						request.getStartDate(),
						request.getEndDate(),
						contractId);

		verify(modelMapper).map(request, existingContract);
		verify(contractRepository).save(existingContract);

		assertEquals(contractId, response.id());
	}

	@Test
	void getById_whenContractBelongsToAnotherEmployee_throwsNotFoundException() {
		UUID employeeId = UUID.randomUUID();
		UUID anotherEmployeeId = UUID.randomUUID();
		UUID contractId = UUID.randomUUID();

		Employee employee = employee(employeeId);
		Employee anotherEmployee = employee(anotherEmployeeId);

		Contract contract = contract(
				contractId,
				anotherEmployee,
				LocalDate.now().minusMonths(1),
				null);

		when(employeeRepository.findById(employeeId))
				.thenReturn(Optional.of(employee));

		when(contractRepository.findById(contractId))
				.thenReturn(Optional.of(contract));

		assertThrows(
				NotFoundException.class,
				() -> contractService.getById(
						employeeId,
						contractId));
	}

	private Employee employee(UUID id) {
		Employee employee = new Employee();
		employee.setId(id);

		return employee;
	}

	private CreateContractRequest createRequest(
			LocalDate startDate,
			LocalDate endDate) {

		CreateContractRequest request = new CreateContractRequest();

		request.setContractType(ContractType.CONTRACT);
		request.setStartDate(startDate);
		request.setEndDate(endDate);
		request.setEmploymentBasis(EmploymentBasis.FULL_TIME);
		request.setHoursPerWeek(BigDecimal.valueOf(37.5));

		return request;
	}

	private UpdateContractRequest updateRequest(
			LocalDate startDate,
			LocalDate endDate) {

		UpdateContractRequest request = new UpdateContractRequest();

		request.setContractType(ContractType.CONTRACT);
		request.setStartDate(startDate);
		request.setEndDate(endDate);
		request.setEmploymentBasis(EmploymentBasis.FULL_TIME);
		request.setHoursPerWeek(BigDecimal.valueOf(37.5));

		return request;
	}

	private Contract contract(
			UUID id,
			Employee employee,
			LocalDate startDate,
			LocalDate endDate) {

		Contract contract = new Contract();

		contract.setId(id);
		contract.setEmployee(employee);
		contract.setContractType(ContractType.CONTRACT);
		contract.setStartDate(startDate);
		contract.setEndDate(endDate);
		contract.setEmploymentBasis(EmploymentBasis.FULL_TIME);
		contract.setHoursPerWeek(BigDecimal.valueOf(37.5));

		return contract;
	}
}
