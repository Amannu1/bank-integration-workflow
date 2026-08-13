package com.luizercole.bankworkflow.services;

import com.luizercole.bankworkflow.dto.BankIntegrationTemplateDTO;
import com.luizercole.bankworkflow.entities.Bank;
import com.luizercole.bankworkflow.entities.BankIntegrationTemplate;
import com.luizercole.bankworkflow.repositories.BankIntegrationTemplateRepository;
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
public class BankIntegrationTemplateService {

    @Autowired
    private BankIntegrationTemplateRepository bankIntegrationTemplateRepository;

    @Autowired
    private BankRepository bankRepository;

    @Transactional(readOnly = true)
    public List<BankIntegrationTemplateDTO> findAll(){
        List<BankIntegrationTemplate> list = bankIntegrationTemplateRepository.findAll();

        return list.stream().map(x -> new BankIntegrationTemplateDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BankIntegrationTemplateDTO findActiveByBankId(Long bankId){
        Optional<BankIntegrationTemplate> obj = bankIntegrationTemplateRepository.findByBankIdAndActiveTrue(bankId);
        BankIntegrationTemplate entity = obj.orElseThrow(() -> new EntityNotFoundException("Active template no found."));

        return new BankIntegrationTemplateDTO(entity);
    }

    @Transactional(readOnly = true)
    public BankIntegrationTemplateDTO findById(Long id){
        Optional<BankIntegrationTemplate> obj = bankIntegrationTemplateRepository.findById(id);
        BankIntegrationTemplate entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found."));

        return new BankIntegrationTemplateDTO(entity);
    }

    @Transactional
    public BankIntegrationTemplateDTO createBankIntegrationTemplate(BankIntegrationTemplateDTO bankIntegrationTemplateDTO){
        BankIntegrationTemplate entity = new BankIntegrationTemplate();
        copyDtoToEntity(bankIntegrationTemplateDTO, entity);

        return new BankIntegrationTemplateDTO(entity);
    }

    @Transactional
    public BankIntegrationTemplateDTO updateBankIntegrationTemplate(Long id, BankIntegrationTemplateDTO bankIntegrationTemplateDTO){
        try{
            BankIntegrationTemplate entity = bankIntegrationTemplateRepository.getReferenceById(id);
            copyDtoToEntity(bankIntegrationTemplateDTO, entity);

            return new BankIntegrationTemplateDTO(entity);
        }catch(EntityNotFoundException e){
            throw new EntityNotFoundException("Id not found: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteBankIntegrationTemplate(Long id){
        if(!bankIntegrationTemplateRepository.existsById(id)){
            throw new EntityNotFoundException("Entity not found.");
        }

        try{
            bankIntegrationTemplateRepository.deleteById(id);
        }catch(DataIntegrityViolationException e){
            throw new DatabaseException("Database error.");
        }
    }

    private void copyDtoToEntity(BankIntegrationTemplateDTO dto, BankIntegrationTemplate entity){
        Bank bank = bankRepository.getReferenceById(dto.getBankId());

        entity.setBank(bank);
        entity.setActive(dto.isActive());
        entity.setName(dto.getName());
        entity.setVersion(dto.getVersion());
        entity.setStepsJson(dto.getStepsJson());
        bankIntegrationTemplateRepository.save(entity);
    }
}
