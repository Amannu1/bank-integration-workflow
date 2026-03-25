package com.luizercole.bankworkflow.resources;

import com.luizercole.bankworkflow.dto.BankDTO;
import com.luizercole.bankworkflow.entities.Bank;
import com.luizercole.bankworkflow.services.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.function.RequestPredicates.path;

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

    @GetMapping("/{id}")
    public ResponseEntity<BankDTO> findById(@PathVariable Long id){
        BankDTO dto = bankService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<BankDTO> createBank(@RequestBody BankDTO bankDTO){
        bankDTO = bankService.createBank(bankDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(bankDTO.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}