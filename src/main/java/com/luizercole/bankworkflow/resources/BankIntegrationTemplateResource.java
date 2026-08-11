package com.luizercole.bankworkflow.resources;

import com.luizercole.bankworkflow.dto.BankIntegrationTemplateDTO;
import com.luizercole.bankworkflow.services.BankIntegrationTemplateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/bank-integration-templates")
public class BankIntegrationTemplateResource {

    @Autowired
    private BankIntegrationTemplateService bankIntegrationTemplateService;

    @GetMapping
    public ResponseEntity<List<BankIntegrationTemplateDTO>> findAll(){
        List<BankIntegrationTemplateDTO> list = bankIntegrationTemplateService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankIntegrationTemplateDTO> findById(@PathVariable Long id){
        BankIntegrationTemplateDTO dto = bankIntegrationTemplateService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<BankIntegrationTemplateDTO> createBankIntegrationDTO(@Valid @RequestBody BankIntegrationTemplateDTO bankIntegrationTemplateDTO){
        bankIntegrationTemplateDTO = bankIntegrationTemplateService.createBankIntegrationTemplate(bankIntegrationTemplateDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(bankIntegrationTemplateDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(bankIntegrationTemplateDTO);

    }

    @PutMapping("/{id}")
    public ResponseEntity<BankIntegrationTemplateDTO> updateBankIntegrationTemplate(@PathVariable Long id, @RequestBody @Valid BankIntegrationTemplateDTO bankIntegrationTemplateDTO){
        bankIntegrationTemplateDTO = bankIntegrationTemplateService.updateBankIntegrationTemplate(id, bankIntegrationTemplateDTO);
        return ResponseEntity.ok().body(bankIntegrationTemplateDTO);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<BankIntegrationTemplateDTO> deleteBankIntegrationTemplate(@PathVariable Long id){
        bankIntegrationTemplateService.deleteBankIntegrationTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
