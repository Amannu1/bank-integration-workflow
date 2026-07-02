package com.luizercole.bankworkflow.services;

import com.luizercole.bankworkflow.dto.UserDTO;
import com.luizercole.bankworkflow.dto.UserInsertDTO;
import com.luizercole.bankworkflow.dto.UserUpdateDTO;
import com.luizercole.bankworkflow.repositories.UserRepository;
import com.luizercole.bankworkflow.services.exceptions.EntityNotFoundException;
import com.luizercole.bankworkflow.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class UserServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalUsers;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalUsers = userRepository.count();
    }

    @Test
    public void findAllShouldReturnList() {

        List<UserDTO> result = userService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void findByIdShouldReturnUserDTOWhenIdExists() {

        UserDTO result = userService.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
    }

    @Test
    public void findByIdShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userService.findById(nonExistingId);
        });
    }

    @Test
    public void createUserShouldReturnUserDTO() {
        UserInsertDTO userInsertDTO = Factory.createUserInsertDTO();

        UserDTO result = userService.createUser(userInsertDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userInsertDTO.getUsername(), result.getUsername());
    }

    @Test
    public void updateShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {

        UserUpdateDTO userUpdateDTO = Factory.userUpdateDTO();

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userService.updateUser(nonExistingId, userUpdateDTO);
        });
    }

    @Test
    public void updateShouldReturnUserDTOWhenIdExists() {

        UserUpdateDTO userUpdateDTO = Factory.userUpdateDTO();

        UserDTO result = userService.updateUser(existingId, userUpdateDTO);

        Assertions.assertNotNull(result);
    }

    @Test
    public void deleteShouldDeleteRWhenIdExists() {

        userService.deleteUser(existingId);

        Assertions.assertEquals(countTotalUsers - 1, userRepository.count());
    }

    @Test
    public void deleteShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(nonExistingId);
        });
    }
}
