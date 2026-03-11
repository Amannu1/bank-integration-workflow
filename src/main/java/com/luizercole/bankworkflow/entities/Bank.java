package com.luizercole.bankworkflow.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_bank")
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean active;

    public Bank(){

    }

    public Bank(Long id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public Long getId(){
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Objects.equals(id, bank.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
