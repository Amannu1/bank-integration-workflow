package com.luizercole.bankworkflow.resources;

import com.luizercole.bankworkflow.dto.BankDTO;
import com.luizercole.bankworkflow.entities.Bank;
import com.luizercole.bankworkflow.services.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/banks")
public class BankResource {

    @Autowired
    private BankService bankService;

    @GetMapping
    public ResponseEntity<List<BankDTO>> findAll(){
        List<BankDTO> list = bankService.findAll();
        return ResponseEntity.ok().body(list);
    }
}
