package com.luizercole.bankworkflow.services;

import com.luizercole.bankworkflow.dto.BankDTO;
import com.luizercole.bankworkflow.entities.Bank;
import com.luizercole.bankworkflow.repositories.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
}
