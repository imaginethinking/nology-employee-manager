package com.nology.employeemanager.contract;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContractRepository extends JpaRepository<Contract, UUID> {

    @Query("""
            SELECT c
            FROM Contract c
            WHERE c.employee.id = :employeeId
                AND c.startDate <= CURRENT_DATE
                AND (c.endDate IS NULL OR c.endDate >= CURRENT_DATE)
            """)
    List<Contract> findActiveByEmployee_Id(UUID employeeId);

    Page<Contract> findByEmployee_Id(UUID employeeId, Pageable pageable);
}
