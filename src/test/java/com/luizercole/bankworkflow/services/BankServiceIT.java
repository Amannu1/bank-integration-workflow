package com.luizercole.bankworkflow.services;

import com.luizercole.bankworkflow.dto.BankDTO;
import com.luizercole.bankworkflow.repositories.BankRepository;
import com.luizercole.bankworkflow.services.exceptions.EntityNotFoundException;
import com.luizercole.bankworkflow.services.exceptions.ResourceNotFoundException;
import com.luizercole.bankworkflow.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class BankServiceIT {

    @Autowired
    private BankService bankService;

    @Autowired
    private BankRepository bankRepository;

    private Long existingId;
    private Long nonDependentId;
    private Long nonExistingId;
    private Long countTotalBanks;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonDependentId = 3L;
        nonExistingId = 1000L;
        countTotalBanks = bankRepository.count();
    }

    @Test
    public void findAllShouldReturnList() {

        List<BankDTO> result = bankService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void findByIdShouldReturnBankDTOWhenIdExists() {

        BankDTO result = bankService.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
    }

    @Test
    public void findByIdShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bankService.findById(nonExistingId);
        });
    }

    @Test
    public void createBankShouldReturnBankDTO() {
        BankDTO bankDTO = Factory.createBankDTO();

        BankDTO result = bankService.createBank(bankDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(bankDTO.getName(), result.getName());
    }

    @Test
    public void updateShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {

        BankDTO bankDTO = Factory.createBankDTO();

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bankService.updateBank(nonExistingId, bankDTO);
        });
    }

    @Test
    public void updateShouldReturnBankDTOWhenIdExists() {

        BankDTO bankDTO = Factory.createBankDTO();

        BankDTO result = bankService.updateBank(existingId, bankDTO);

        Assertions.assertNotNull(result);
    }

    @Test
    public void deleteShouldDeleteNonDependentBankWhenIdExists() {

        bankService.deleteBank(nonDependentId);

        Assertions.assertEquals(countTotalBanks - 1, bankRepository.count());
    }

    @Test
    public void deleteShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bankService.deleteBank(nonExistingId);
        });
    }
}
