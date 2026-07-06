package com.luizercole.bankworkflow.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizercole.bankworkflow.dto.UserDTO;
import com.luizercole.bankworkflow.repositories.UserRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalUsers;

    private String username, password, bearerToken;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalUsers = userRepository.count();

        username = "Maria";
        password = "123456";

        bearerToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
    }

    @Test
    public void findAllShouldReturnList() throws Exception {
        ResultActions result = mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$").isArray());
        result.andExpect(jsonPath("$.length()").value(countTotalUsers.intValue()));
        result.andExpect(jsonPath("$[0].username").value("Alex"));
        result.andExpect(jsonPath("$[1].username").value("Maria"));
    }

    @Test
    public void findByIdShouldReturnUserWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(get("/users/{id}", existingId)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(get("/users/{id}", nonExistingId)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void createShouldReturnCreatedAndUserDTO() throws Exception {
        UserDTO userDTO = Factory.createUserInsertDTO();

        String jsonBody = objectMapper.writeValueAsString(userDTO);

        String expectedUsername = userDTO.getUsername();
        Boolean expectedActive = userDTO.isActive();

        ResultActions result = mockMvc.perform(post("/users")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.username").value(expectedUsername));
        result.andExpect(jsonPath("$.active").value(expectedActive));
    }

    @Test
    public void updateShouldReturnUserDTOWhenIdExists() throws Exception {
        UserDTO userDTO = Factory.createUserDTO();

        String jsonBody = objectMapper.writeValueAsString(userDTO);

        String expectedUsername = userDTO.getUsername();
        Boolean expectedActive = userDTO.isActive();

        ResultActions result = mockMvc.perform(put("/users/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.username").value(expectedUsername));
        result.andExpect(jsonPath("$.active").value(expectedActive));
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(delete("/users/{id}", existingId)
                .header("Authorization", "Bearer " + bearerToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
        Assertions.assertFalse(userRepository.existsById(existingId));
    }
}
