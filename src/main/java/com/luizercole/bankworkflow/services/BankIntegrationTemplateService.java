package com.luizercole.bankworkflow.services;

import com.luizercole.bankworkflow.dto.BankIntegrationTemplateDTO;
import com.luizercole.bankworkflow.entities.Bank;
import com.luizercole.bankworkflow.entities.BankIntegrationTemplate;
import com.luizercole.bankworkflow.repositories.BankIntegrationTemplateRepository;
import com.luizercole.bankworkflow.repositories.BankRepository;
import com.luizercole.bankworkflow.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public BankIntegrationTemplateDTO findById(Long id){
        Optional<BankIntegrationTemplate> obj = bankIntegrationTemplateRepository.findById(id);
        BankIntegrationTemplate entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found."));

        return new BankIntegrationTemplateDTO(entity);
    }

    @Transactional
    public BankIntegrationTemplateDTO createBankIntegrationTemplate(BankIntegrationTemplateDTO bankIntegrationTemplateDTO){
        BankIntegrationTemplate entity = new BankIntegrationTemplate();
        Bank bank = bankRepository.getReferenceById(bankIntegrationTemplateDTO.getBankId());

        entity.setBank(bank);
        entity.setActive(bankIntegrationTemplateDTO.isActive());
        entity.setName(bankIntegrationTemplateDTO.getName());
        entity.setVersion(bankIntegrationTemplateDTO.getVersion());
        entity.setStepsJson(bankIntegrationTemplateDTO.getStepsJson());
        bankIntegrationTemplateRepository.save(entity);

        return new BankIntegrationTemplateDTO(entity);
    }
}
