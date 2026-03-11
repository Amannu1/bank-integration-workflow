package com.luizercole.bankworkflow.repositories;

import com.luizercole.bankworkflow.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
}
