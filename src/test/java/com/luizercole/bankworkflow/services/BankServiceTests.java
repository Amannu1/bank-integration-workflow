package com.luizercole.bankworkflow.services;

import com.luizercole.bankworkflow.dto.BankDTO;
import com.luizercole.bankworkflow.entities.Bank;
import com.luizercole.bankworkflow.repositories.BankRepository;
import com.luizercole.bankworkflow.services.exceptions.DatabaseException;
import com.luizercole.bankworkflow.services.exceptions.EntityNotFoundException;
import com.luizercole.bankworkflow.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BankServiceTests {

    @InjectMocks
    private BankService bankService;

    @Mock
    private BankRepository bankRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private Bank bank;
    BankDTO bankDTO;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 3L;
        bank = Factory.createBank();
        bankDTO = Factory.createBankDTO();
    }

    @Test
    public void findAllShouldReturnList(){
        Mockito.when(bankRepository.findAll()).thenReturn(Arrays.asList(bank));

        List<BankDTO> result = bankService.findAll();

        Assertions.assertNotNull(result);
        Mockito.verify(bankRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void findByIdShouldReturnBankDTOWhenIdExists(){
        Mockito.when(bankRepository.findById(existingId)).thenReturn(Optional.of(bank));

        BankDTO result = bankService.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Assertions.assertEquals(bank.getName(), result.getName());
        Mockito.verify(bankRepository, Mockito.times(1)).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist(){
        Mockito.when(bankRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bankService.findById(nonExistingId);
        });
    }

    @Test
    public void createBankShouldReturnBankDTO(){
        BankDTO dto = Factory.createBankDTO();
        Bank bank = Factory.createBank();

        Mockito.when(bankRepository.save(Mockito.any())).thenReturn(bank);

        BankDTO result = bankService.createBank(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(dto.getName(), result.getName());
        Mockito.verify(bankRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void updateShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist(){
        Mockito.when(bankRepository.getReferenceById(nonExistingId)).thenThrow(jakarta.persistence.EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bankService.updateBank(nonExistingId, bankDTO);
        });
    }

    @Test
    public void updateShouldReturnBankDTOWhenIdExists() {
        Mockito.when(bankRepository.getReferenceById(existingId)).thenReturn(bank);
        Mockito.when(bankRepository.save(Mockito.any())).thenReturn(bank);

        BankDTO result = bankService.updateBank(existingId, bankDTO);

        Assertions.assertNotNull(result);
    }

    @Test
    public void deleteShouldDeleteWhenIdExist(){
        Mockito.when(bankRepository.existsById(existingId)).thenReturn(true);
        Mockito.doNothing().when(bankRepository).deleteById(existingId);

        Assertions.assertDoesNotThrow(() -> {
             bankService.deleteBank(existingId);
        });
        Mockito.verify(bankRepository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowEntityNotFoundExceptionWhenIdDoesNotExists(){
        Mockito.when(bankRepository.existsById(nonExistingId)).thenReturn(false);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bankService.deleteBank(nonExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdIsDependent(){
        Mockito.when(bankRepository.existsById(dependentId)).thenReturn(true);
        Mockito.doThrow(DataIntegrityViolationException.class).when(bankRepository).deleteById(dependentId);

        Assertions.assertThrows(DatabaseException.class, () -> {
           bankService.deleteBank(dependentId);
        });
    }
}