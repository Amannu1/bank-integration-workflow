package com.luizercole.bankworkflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.luizercole.bankworkflow.entities.Bank;
import java.io.Serializable;
import java.time.Instant;

public class BankDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private boolean active;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant updatedAt;

    public BankDTO(Long id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public BankDTO(){

    }

    public BankDTO(Bank entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.active = entity.isActive();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
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

    public boolean isActive(){ return active;}

    public void setActive(boolean active){this.active = active;}

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
