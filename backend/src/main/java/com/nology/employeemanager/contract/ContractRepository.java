package com.nology.employeemanager.contract;

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
                AND c.startDate <= :today
                AND (c.endDate IS NULL OR c.endDate >= :today)
            """)
    List<Contract> findActiveByEmployee_Id(UUID employeeId);

    Page<Contract> findByEmployee_Id(UUID employeeId, Pageable pageable);
}
