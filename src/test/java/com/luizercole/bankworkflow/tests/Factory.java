package com.luizercole.bankworkflow.tests;

import com.luizercole.bankworkflow.dto.BankDTO;
import com.luizercole.bankworkflow.entities.Bank;

public class Factory {

    public static Bank createBank(){
        Bank bank = new Bank(1L, "Itau", true);
        return bank;
    }

    public static BankDTO createBankDTO(){
        Bank bank = createBank();
        return new BankDTO(bank);
    }
}
