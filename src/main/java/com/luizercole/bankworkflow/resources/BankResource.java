package com.luizercole.bankworkflow.resources;

import com.luizercole.bankworkflow.entities.Bank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/banks")
public class BankResource {

    @GetMapping
    public ResponseEntity<List<Bank>> findAll(){
        List<Bank> list = new ArrayList<>();
        list.add(new Bank(1L, "Itaú", true));
        list.add(new Bank(2L, "Sicredi", false));
        return ResponseEntity.ok().body(list);
    }
}
