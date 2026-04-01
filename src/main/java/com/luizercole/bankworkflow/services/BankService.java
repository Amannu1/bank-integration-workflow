package com.luizercole.bankworkflow.services;

import com.luizercole.bankworkflow.dto.BankDTO;
import com.luizercole.bankworkflow.entities.Bank;
import com.luizercole.bankworkflow.repositories.BankRepository;
import com.luizercole.bankworkflow.services.exceptions.DatabaseException;
import com.luizercole.bankworkflow.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    @Transactional(readOnly = true)
    public List<BankDTO> findAll(){
        List<Bank> list = bankRepository.findAll();

        return list.stream().map(x -> new BankDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BankDTO findById(Long id){
        Optional<Bank> obj = bankRepository.findById(id);
        Bank entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        return new BankDTO(entity);
    }

    @Transactional
    public BankDTO createBank(BankDTO bankDTO){
        Bank entity = new Bank();
        entity.setName(bankDTO.getName());
        bankRepository.save(entity);
        return new BankDTO(entity);
    }

    @Transactional
    public BankDTO updateBank(Long id, BankDTO bankDTO){
        try {
            Bank entity = bankRepository.getReferenceById(id);
            entity.setName(bankDTO.getName());
            entity.setActive(bankDTO.isActive());
            bankRepository.save(entity);
            return new BankDTO(entity);
        }catch(EntityNotFoundException e){
            throw new EntityNotFoundException("Id not found: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteBank(Long id){
        if(!bankRepository.existsById(id)){
            throw new EntityNotFoundException("Entity not found.");
        }
        try {

            bankRepository.deleteById(id);

        }catch(DataIntegrityViolationException e){
            throw new DatabaseException("Database error.");
        }
    }
}
