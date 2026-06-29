package com.luizercole.bankworkflow.services;

import com.luizercole.bankworkflow.dto.UserDTO;
import com.luizercole.bankworkflow.dto.UserInsertDTO;
import com.luizercole.bankworkflow.dto.UserUpdateDTO;
import com.luizercole.bankworkflow.entities.Role;
import com.luizercole.bankworkflow.entities.User;
import com.luizercole.bankworkflow.repositories.RoleRepository;
import com.luizercole.bankworkflow.repositories.UserRepository;
import com.luizercole.bankworkflow.services.exceptions.EntityNotFoundException;
import com.luizercole.bankworkflow.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private long existingId;
    private long nonExistingId;
    private User user;
    private Role role;
    UserDTO userDTO;
    UserUpdateDTO userUpdateDTO;
    UserInsertDTO userInsertDTO;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        user = Factory.createUser();
        role = Factory.createRole();
        userDTO = Factory.createUserInsertDTO();
        userUpdateDTO = Factory.userUpdateDTO();
        userInsertDTO = Factory.createUserInsertDTO();
    }

    @Test
    public void findAllShouldReturnList(){
        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        List<UserDTO> result = userService.findAll();

        Assertions.assertNotNull(result);
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void findByIdShouldReturnBankDTOWhenIdExists(){
        Mockito.when(userRepository.findById(existingId)).thenReturn(Optional.of(user));

        UserDTO result = userService.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Assertions.assertEquals(user.getUsername(), result.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist(){
        Mockito.when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userService.findById(nonExistingId);
        });
    }

    @Test
    public void createUserShouldReturnUserDTO(){

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("encoded-password");

        UserDTO result = userService.createUser(userInsertDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userInsertDTO.getUsername(), result.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(userInsertDTO.getPassword());
    }

    @Test
    public void updateShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userService.updateUser(nonExistingId, userUpdateDTO);
        });
    }

    @Test
    public void updateShouldReturnBankDTOWhenIdExists(){
        Mockito.when(userRepository.findById(existingId)).thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        UserDTO result = userService.updateUser(existingId, userUpdateDTO);

        Assertions.assertNotNull(result);
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists(){
        Mockito.when(userRepository.existsById(existingId)).thenReturn(true);
        Mockito.doNothing().when(userRepository).deleteById(existingId);

        Assertions.assertDoesNotThrow(() -> {
            userService.deleteUser(existingId);
        });
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowEntityNotFoundExceptionWhenIdDoesNotExists(){
        Mockito.when(userRepository.existsById(nonExistingId)).thenReturn(false);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(nonExistingId);
        });
    }
}
