package com.nology.employeemanager.contract;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContractRepository extends JpaRepository<Contract, UUID> {

    @Query("""
            SELECT COUNT(c) > 0
            FROM Contract c
            WHERE c.employee.id = :employeeId
            AND (:excludeContractId IS NULL OR c.id <> :excludeContractId)
            AND (:endDate IS NULL OR c.startDate <= :endDate)
            AND (c.endDate IS NULL OR c.endDate >= :startDate)
        """)
    boolean existsOverlappingContract(
        UUID employeeId,
        LocalDate startDate,
        LocalDate endDate,
        UUID excludeContractId
);

    Page<Contract> findByEmployee_Id(UUID employeeId, Pageable pageable);
}
