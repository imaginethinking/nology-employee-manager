package com.nology.employeemanager.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.nology.employeemanager.common.exceptions.NotFoundException;
import com.nology.employeemanager.employee.dtos.CreateEmployeeRequest;
import com.nology.employeemanager.employee.dtos.EmployeeResponse;
import com.nology.employeemanager.employee.dtos.UpdateEmployeeRequest;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void create_whenRequestIsValid_savesMappedEmployeeAndReturnsResponse() {
        CreateEmployeeRequest request = new CreateEmployeeRequest();

        Employee mappedEmployee = employee(
            null,
            "Alice",
            "Smith");

        Employee savedEmployee = employee(
            UUID.randomUUID(),
            "Alice",
            "Smith");

        when(modelMapper.map(request, Employee.class))
            .thenReturn(mappedEmployee);

        when(employeeRepository.save(mappedEmployee))
            .thenReturn(savedEmployee);

        EmployeeResponse response = employeeService.create(request);

        assertEquals(savedEmployee.getId(), response.id());
        assertEquals("Alice", response.firstName());
        assertEquals("Smith", response.lastName());

        verify(modelMapper).map(request, Employee.class);
        verify(employeeRepository).save(mappedEmployee);
    }

    @Test
    void getById_whenEmployeeExists_returnsEmployee() {
        UUID employeeId = UUID.randomUUID();

        Employee employee = employee(
            employeeId,
            "Alice",
            "Smith");

        when(employeeRepository.findById(employeeId))
            .thenReturn(Optional.of(employee));

        EmployeeResponse response = employeeService.getById(employeeId);

        assertEquals(employeeId, response.id());
        assertEquals("Alice", response.firstName());
        assertEquals("Smith", response.lastName());
    }

    @Test
    void getById_whenEmployeeDoesNotExist_throwsNotFoundException() {
        UUID employeeId = UUID.randomUUID();

        when(employeeRepository.findById(employeeId))
            .thenReturn(Optional.empty());

        assertThrows(
            NotFoundException.class,
            () -> employeeService.getById(employeeId));
    }

    @Test
    void update_whenEmployeeExists_mapsOntoExistingEmployeeAndSaves() {
        UUID employeeId = UUID.randomUUID();

        UpdateEmployeeRequest request = new UpdateEmployeeRequest();

        Employee employee = employee(
            employeeId,
            "Alice",
            "Smith");

        when(employeeRepository.findById(employeeId))
            .thenReturn(Optional.of(employee));

        when(employeeRepository.save(employee))
            .thenReturn(employee);

        EmployeeResponse response = employeeService.update(
            employeeId,
            request);

        verify(modelMapper).map(request, employee);
        verify(employeeRepository).save(employee);

        assertEquals(employeeId, response.id());
    }

    @Test
    void update_whenEmployeeDoesNotExist_throwsNotFoundExceptionAndDoesNotSave() {
        UUID employeeId = UUID.randomUUID();

        UpdateEmployeeRequest request = new UpdateEmployeeRequest();

        when(employeeRepository.findById(employeeId))
            .thenReturn(Optional.empty());

        assertThrows(
            NotFoundException.class,
            () -> employeeService.update(employeeId, request));

        verifyNoInteractions(modelMapper);

        verify(employeeRepository, never())
            .save(any(Employee.class));
    }

    @Test
    void delete_whenEmployeeExists_deletesEmployee() {
        UUID employeeId = UUID.randomUUID();

        Employee employee = employee(
            employeeId,
            "Alice",
            "Smith");

        when(employeeRepository.findById(employeeId))
            .thenReturn(Optional.of(employee));

        employeeService.delete(employeeId);

        verify(employeeRepository).delete(employee);
    }

    @Test
    void delete_whenEmployeeDoesNotExist_throwsNotFoundExceptionAndDoesNotDelete() {
        UUID employeeId = UUID.randomUUID();

        when(employeeRepository.findById(employeeId))
            .thenReturn(Optional.empty());

        assertThrows(
            NotFoundException.class,
            () -> employeeService.delete(employeeId));

        verify(employeeRepository, never())
            .delete(any(Employee.class));
    }

    private Employee employee(
        UUID id,
        String firstName,
        String lastName) {

        Address address = new Address();
        address.setAddressLine1("1 Test Street");
        address.setCity("London");
        address.setPostcode("SW1A 1AA");
        address.setCountry("United Kingdom");

        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setEmailAddress("alice@example.com");
        contactDetails.setMobileNumber("07123456789");
        contactDetails.setAddress(address);

        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setContactDetails(contactDetails);

        return employee;
    }
}
