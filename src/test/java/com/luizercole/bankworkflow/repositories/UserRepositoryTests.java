package com.luizercole.bankworkflow.repositories;

import com.luizercole.bankworkflow.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTests {


    private long existingId;
    private long nonExistingId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdNotExist(){
        Optional<User> result = userRepository.findById(nonExistingId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByIdShouldReturnNotEmptyOptionalWhenIdExists(){
        Optional<User> result = userRepository.findById(existingId);

        Assertions.assertTrue(result.isPresent());
    }


    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        userRepository.deleteById(existingId);

        Optional<User> result = userRepository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }
}
