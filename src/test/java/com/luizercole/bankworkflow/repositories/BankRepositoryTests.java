package com.luizercole.bankworkflow.repositories;

import com.luizercole.bankworkflow.entities.Bank;
import com.luizercole.bankworkflow.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class BankRepositoryTests {

    private long existingId;
    private long nonExistingId;
    private long countTotalBanks;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalBanks = 2L;
    }

    @Autowired
    private BankRepository bankRepository;

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdNotExist(){
        Optional<Bank> result = bankRepository.findById(nonExistingId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByIdShouldReturnNotEmptyOptionalWhenIdExists(){
        Optional<Bank> result = bankRepository.findById(existingId);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull(){
        Bank bank = Factory.createBank();
        bank.setId(null);

        bank = bankRepository.save(bank);

        Assertions.assertNotNull(bank.getId());
        Assertions.assertEquals(countTotalBanks + 1, bank.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        bankRepository.deleteById(existingId);

        Optional<Bank> result = bankRepository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }
}
