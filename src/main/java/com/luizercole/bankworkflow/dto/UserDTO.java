package com.luizercole.bankworkflow.dto;

import com.luizercole.bankworkflow.entities.User;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.BooleanUtils.forEach;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Required field.")
    private String name;
    private boolean active;

    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO(){

    }

    public UserDTO(Long id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public UserDTO(User entity){
        id = entity.getId();
        name = entity.getName();
        active = entity.isActive();
        entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }
}
