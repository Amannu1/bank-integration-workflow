package com.luizercole.bankworkflow.dto;

import jakarta.validation.constraints.NotBlank;

public class UserInsertDTO extends UserDTO{
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Required field.")
    private String password;

    UserInsertDTO(){
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
