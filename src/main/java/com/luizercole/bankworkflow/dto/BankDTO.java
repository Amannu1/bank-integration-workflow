package com.luizercole.bankworkflow.dto;

import com.luizercole.bankworkflow.entities.Bank;
import java.io.Serializable;

public class BankDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    public BankDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public BankDTO(){

    }

    public BankDTO(Bank entity){
        this.id = entity.getId();
        this.name = entity.getName();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
