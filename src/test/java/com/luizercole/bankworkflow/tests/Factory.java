package com.luizercole.bankworkflow.tests;

import com.luizercole.bankworkflow.dto.*;
import com.luizercole.bankworkflow.entities.Bank;
import com.luizercole.bankworkflow.entities.Role;
import com.luizercole.bankworkflow.entities.User;

public class Factory {

    public static Bank createBank(){
        Bank bank = new Bank(1L, "Itau", true);
        return bank;
    }

    public static User createUser(){
        User user = new User(1L, "Sophia", true, "123456");
        return user;
    }

    public static UserDTO createUserDTO(){
        User user = createUser();
        user.addRole(createRole());
        return new UserDTO(user);
    }

    public static Role createRole(){
        Role role = new Role(1L, "ROLE_OPERATOR");
        return role;
    }

    public static UserInsertDTO createUserInsertDTO(){
        UserInsertDTO dto = new UserInsertDTO();
        dto.setUsername("Sophia123");
        dto.setActive(true);
        dto.setPassword("123456");
        dto.getRoles().add(new RoleDTO(1L, "ROLE_OPERATOR"));
        return dto;
    }

    public static UserUpdateDTO userUpdateDTO(){
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUsername("Sophia123");
        dto.setActive(true);
        dto.getRoles().add(new RoleDTO(1L, "ROLE_OPERATOR"));
        return dto;

    }

    public static BankDTO createBankDTO(){
        Bank bank = createBank();
        return new BankDTO(bank);
    }
}
