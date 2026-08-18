package com.luizercole.bankworkflow.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizercole.bankworkflow.dto.BankDTO;
import com.luizercole.bankworkflow.entities.Bank;
import com.luizercole.bankworkflow.repositories.BankRepository;
import com.luizercole.bankworkflow.tests.Factory;
import com.luizercole.bankworkflow.tests.TokenUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BankResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private BankRepository bankRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalBanks;

    private String username, password, bearerToken;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalBanks = bankRepository.count();

        username = "Maria";
        password = "123456";

        bearerToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
    }

    @Test
    public void findAllShouldReturnList() throws Exception {
        ResultActions result = mockMvc.perform(get("/banks")
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$").isArray());
        result.andExpect(jsonPath("$.length()").value(countTotalBanks.intValue()));
        result.andExpect(jsonPath("$[0].name").value("Itaú"));
        result.andExpect(jsonPath("$[1].name").value("SICREDI"));
    }

    @Test
    public void findByIdShouldReturnBankWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(get("/banks/{id}", existingId)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(get("/banks/{id}", nonExistingId)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnCreatedAndBankDTO() throws Exception {
        BankDTO bankDTO = Factory.createBankDTO();

        String jsonBody = objectMapper.writeValueAsString(bankDTO);

        String expectedName = bankDTO.getName();
        Boolean expectedActive = bankDTO.isActive();

        ResultActions result = mockMvc.perform(post("/banks")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").value(expectedName));
        result.andExpect(jsonPath("$.active").value(expectedActive));
    }

    @Test
    public void updateShouldReturnBankDTOWhenIdExists() throws Exception {
        BankDTO bankDTO = Factory.createBankDTO();

        String jsonBody = objectMapper.writeValueAsString(bankDTO);

        String expectedName = bankDTO.getName();
        Boolean expectedActive = bankDTO.isActive();

        ResultActions result = mockMvc.perform(put("/banks/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.name").value(expectedName));
        result.andExpect(jsonPath("$.active").value(expectedActive));
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        Bank bank = bankRepository.save(new Bank(null, "Banco Teste", true));

        ResultActions result = mockMvc.perform(delete("/banks/{id}", bank.getId())
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
        Assertions.assertFalse(bankRepository.existsById(bank.getId()));
    }
}
