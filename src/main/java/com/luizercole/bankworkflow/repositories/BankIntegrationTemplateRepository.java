package com.luizercole.bankworkflow.repositories;

import com.luizercole.bankworkflow.entities.BankIntegrationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankIntegrationTemplateRepository extends JpaRepository<BankIntegrationTemplate, Long> {

    Optional<BankIntegrationTemplate> findByBankIdAndActiveTrue(Long bankId);
}
