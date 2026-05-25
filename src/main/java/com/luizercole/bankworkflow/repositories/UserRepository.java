package com.luizercole.bankworkflow.repositories;

import com.luizercole.bankworkflow.entities.User;
import com.luizercole.bankworkflow.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);
}
