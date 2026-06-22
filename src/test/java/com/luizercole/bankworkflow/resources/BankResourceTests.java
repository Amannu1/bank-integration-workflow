package com.luizercole.bankworkflow.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizercole.bankworkflow.dto.BankDTO;
import com.luizercole.bankworkflow.services.BankService;
import com.luizercole.bankworkflow.services.exceptions.DatabaseException;
import com.luizercole.bankworkflow.services.exceptions.EntityNotFoundException;
import com.luizercole.bankworkflow.services.exceptions.ResourceNotFoundException;
import com.luizercole.bankworkflow.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = BankResource.class)
@AutoConfigureMockMvc(addFilters = false)
public class BankResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BankService bankService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private BankDTO bankDTO;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        bankDTO = Factory.createBankDTO();
    }

    @Test
    public void findAllShouldReturnList() throws Exception {
        Mockito.when(bankService.findAll()).thenReturn(Arrays.asList(bankDTO));

        ResultActions resultActions = mockMvc.perform(get("/banks")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnBankWhenIdExists() throws Exception {
        Mockito.when(bankService.findById(existingId)).thenReturn(bankDTO);

        ResultActions resultActions = mockMvc.perform(get("/banks/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        Mockito.when(bankService.findById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        ResultActions resultActions = mockMvc.perform(get("/banks/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void createShouldReturnCreatedAndBankDTO() throws Exception {
        Mockito.when(bankService.createBank(any())).thenReturn(bankDTO);

        String jsonBody = objectMapper.writeValueAsString(bankDTO);

        ResultActions resultActions = mockMvc.perform(post("/banks")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.active").exists());
    }

    @Test
    public void updateShouldReturnBankDTOWhenIdExists() throws Exception {
        Mockito.when(bankService.updateBank(eq(existingId), any())).thenReturn(bankDTO);

        String jsonBody = objectMapper.writeValueAsString(bankDTO);

        ResultActions resultActions = mockMvc.perform(put("/banks/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.active").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        Mockito.when(bankService.updateBank(eq(nonExistingId), any())).thenThrow(EntityNotFoundException.class);

        String jsonBody = objectMapper.writeValueAsString(bankDTO);

        ResultActions resultActions = mockMvc.perform(put("/banks/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldDeleteWhenIdExists() throws Exception {
        Mockito.doNothing().when(bankService).deleteBank(existingId);

        ResultActions resultActions = mockMvc.perform(delete("/banks/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldThrowNotFoundWhenIdDoesNotExists() throws Exception {
        Mockito.doThrow(EntityNotFoundException.class).when(bankService).deleteBank(nonExistingId);

        ResultActions resultActions = mockMvc.perform(delete("/banks/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdIsDependent() throws Exception {
        Mockito.doThrow(DatabaseException.class).when(bankService).deleteBank(dependentId);

        ResultActions resultActions = mockMvc.perform(delete("/banks/{id}", dependentId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isBadRequest());

    }
}
