package com.luizercole.bankworkflow.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizercole.bankworkflow.dto.UserDTO;
import com.luizercole.bankworkflow.dto.UserInsertDTO;
import com.luizercole.bankworkflow.repositories.UserRepository;
import com.luizercole.bankworkflow.services.UserService;
import com.luizercole.bankworkflow.services.exceptions.DatabaseException;
import com.luizercole.bankworkflow.services.exceptions.EntityNotFoundException;
import com.luizercole.bankworkflow.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserResource.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private UserInsertDTO userInsertDTO;

    private UserDTO userDTO;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        userDTO = Factory.createUserDTO();
        userInsertDTO = Factory.createUserInsertDTO();
    }

    @Test
    public void findAllShouldReturnList() throws Exception {
        Mockito.when(userService.findAll()).thenReturn(Arrays.asList(userDTO));

        ResultActions resultActions = mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnBankWhenIdExists() throws Exception {
        Mockito.when(userService.findById(existingId)).thenReturn(userDTO);

        ResultActions resultActions = mockMvc.perform(get("/users/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        Mockito.when(userService.findById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        ResultActions resultActions = mockMvc.perform(get("/users/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void createShouldReturnCreatedAndUserDTO() throws Exception {
        Mockito.when(userService.createUser(any())).thenReturn(userDTO);

        String jsonBody = objectMapper.writeValueAsString(userInsertDTO);

        ResultActions resultActions = mockMvc.perform(post("/users")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.username").exists());
        resultActions.andExpect(jsonPath("$.active").exists());
    }

    @Test
    public void updateShouldReturnBankDTOWhenIdExists() throws Exception {
        Mockito.when(userService.updateUser(eq(existingId), any())).thenReturn(userDTO);

        String jsonBody = objectMapper.writeValueAsString(userInsertDTO);

        ResultActions resultActions = mockMvc.perform(put("/users/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.username").exists());
        resultActions.andExpect(jsonPath("$.active").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        Mockito.when(userService.updateUser(eq(nonExistingId), any())).thenThrow(EntityNotFoundException.class);

        String jsonBody = objectMapper.writeValueAsString(userInsertDTO);

        ResultActions resultActions = mockMvc.perform(put("/users/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldDeleteWhenIdExists() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(existingId);

        ResultActions resultActions = mockMvc.perform(delete("/users/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldThrowNotFoundWhenIdDoesNotExists() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class).when(userService).deleteUser(nonExistingId);

        ResultActions resultActions = mockMvc.perform(delete("/users/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdIsDependent() throws Exception {
        Mockito.doThrow(DatabaseException.class).when(userService).deleteUser(dependentId);

        ResultActions resultActions = mockMvc.perform(delete("/users/{id}", dependentId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isBadRequest());
    }
}
