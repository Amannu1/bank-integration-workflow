package com.luizercole.bankworkflow.projections;

public interface UserDetailsProjection {

    String getName();
    String getPassword();
    Long getRoleId();
    String getAuthority();
}
