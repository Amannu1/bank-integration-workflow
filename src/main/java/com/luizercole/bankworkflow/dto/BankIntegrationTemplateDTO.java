package com.luizercole.bankworkflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.luizercole.bankworkflow.entities.Bank;
import com.luizercole.bankworkflow.entities.BankIntegrationTemplate;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.Instant;

public class BankIntegrationTemplateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Required field.")
    private String name;

    private Long bankId;

    private String bankName;

    @NotBlank(message = "Required field.")
    private String stepsJson;

    @NotBlank(message = "Required field.")
    private String version;

    private boolean active;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant updatedAt;

    public BankIntegrationTemplateDTO(){

    }

    public BankIntegrationTemplateDTO(Long id, String name, Long bankId, String bankName, String stepsJson, String version, boolean active) {
        this.id = id;
        this.name = name;
        this.bankId = bankId;
        this.bankName = bankName;
        this.stepsJson = stepsJson;
        this.version = version;
        this.active = active;
    }

    public BankIntegrationTemplateDTO(BankIntegrationTemplate entity) {
        id = entity.getId();
        name = entity.getName();
        bankId = entity.getBank().getId();
        bankName = entity.getBank().getName();
        stepsJson = entity.getStepsJson();
        version = entity.getVersion();
        active = entity.isActive();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBankId() {
        return bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getStepsJson() {
        return stepsJson;
    }

    public void setStepsJson(String stepsJson) {
        this.stepsJson = stepsJson;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }


    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
