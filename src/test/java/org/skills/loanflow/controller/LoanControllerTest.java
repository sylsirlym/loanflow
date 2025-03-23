package org.skills.loanflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skills.loanflow.dto.loan.request.LoanRequestDTO;
import org.skills.loanflow.dto.loan.response.LoanResponseDTO;
import org.skills.loanflow.service.LoanService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoanControllerTest {
    private MockMvc mockMvc;

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
    }

    @Test
    @DisplayName("Should successfully request a loan via API")
    void requestLoan_Success() throws Exception {
        LoanRequestDTO requestDTO = new LoanRequestDTO();
        requestDTO.setRequestedAmount(new BigDecimal("5000"));

        LoanResponseDTO responseDTO = new LoanResponseDTO();

        when(loanService.requestLoan(anyLong(), any(LoanRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/v1/loan/{offerId}/request", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should fetch loans successfully via API")
    void fetchLoans_Success() throws Exception {
        when(loanService.fetchLoans(anyString(), any())).thenReturn(List.of(new LoanResponseDTO()));

        mockMvc.perform(get("/v1/loan/{msisdn}","25477894899"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should cancel a loan via API")
    void cancelLoan_Success() throws Exception {
        doNothing().when(loanService).cancelLoan(anyLong());

        mockMvc.perform(put("/v1/loan/{loanId}/cancel", 1))
                .andExpect(status().isOk());
    }
}
