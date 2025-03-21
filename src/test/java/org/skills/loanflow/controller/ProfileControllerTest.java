package org.skills.loanflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skills.loanflow.dto.profile.request.LoanOfferRequestDTO;
import org.skills.loanflow.dto.profile.request.ProfileRequestDTO;
import org.skills.loanflow.dto.profile.response.LoanOfferResponseDTO;
import org.skills.loanflow.dto.profile.response.ProfileResponseDTO;
import org.skills.loanflow.service.ProfileService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 21/03/2025
 * Time: 12:54
 */

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileController).build();
    }

    @Test
    void testCreateProfile() throws Exception {
        ProfileRequestDTO request = new ProfileRequestDTO();
        request.setMsisdn("1234567890");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setAddress("123 Main St");
        request.setPinHash("hashedPin");
        request.setCreditScore(BigDecimal.valueOf(750));

        ProfileResponseDTO response = new ProfileResponseDTO();
        response.setProfileId(1L);
        response.setMsisdn("1234567890");
        response.setFirstName("John");
        response.setLastName("Doe");

        // Mock the service method
        when(profileService.createProfile(any(ProfileRequestDTO.class))).thenReturn(response);

        // Perform the request and verify the response
        mockMvc.perform(post("/v1/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msisdn").value("1234567890"))
                .andExpect(jsonPath("$.firstName").value("John"));

        // Verify that the service method was called
        verify(profileService, times(1)).createProfile(any(ProfileRequestDTO.class));
    }

    @Test
    void testGetProfileByMsisdn() throws Exception {
        ProfileResponseDTO response = new ProfileResponseDTO();
        response.setProfileId(1L);
        response.setMsisdn("1234567890");
        response.setFirstName("John");
        response.setLastName("Doe");

        // Mock the service method
        when(profileService.getProfileByMsisdn("1234567890")).thenReturn(response);

        // Perform the request and verify the response
        mockMvc.perform(get("/v1/profile/1234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msisdn").value("1234567890"))
                .andExpect(jsonPath("$.firstName").value("John"));

        // Verify that the service method was called
        verify(profileService, times(1)).getProfileByMsisdn("1234567890");
    }

    @Test
    void testUpdateProfileCreditScore() throws Exception {
        ProfileResponseDTO response = new ProfileResponseDTO();
        response.setProfileId(1L);
        response.setMsisdn("1234567890");
        response.setCreditScore(BigDecimal.valueOf(800));

        // Mock the service method
        when(profileService.updateProfileCreditScore("1234567890", BigDecimal.valueOf(800))).thenReturn(response);

        // Perform the request and verify the response
        mockMvc.perform(put("/v1/profile/1234567890/credit-score")
                        .param("newCreditScore", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.creditScore").value(800));

        // Verify that the service method was called
        verify(profileService, times(1)).updateProfileCreditScore("1234567890", BigDecimal.valueOf(800));
    }

    @Test
    void testCreateProfileLoanOffers() throws Exception {
        LoanOfferRequestDTO request = new LoanOfferRequestDTO();
        request.setProductId(1L);
        request.setLoanLimit(BigDecimal.valueOf(10000));

        LoanOfferResponseDTO response = new LoanOfferResponseDTO();
        response.setLoanOfferId(1L);
        response.setLoanLimit(BigDecimal.valueOf(10000));

        // Mock the service method
        when(profileService.createProfileOffer("1234567890", request)).thenReturn(response);

        // Perform the request and verify the response
        mockMvc.perform(post("/v1/profile/1234567890/loan-offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanLimit").value(10000));

        // Verify that the service method was called
        verify(profileService, times(1)).createProfileOffer("1234567890", request);
    }

    @Test
    void testGetProfileLoanOffers() throws Exception {
        LoanOfferResponseDTO response = new LoanOfferResponseDTO();
        response.setLoanOfferId(1L);
        response.setLoanLimit(BigDecimal.valueOf(10000));

        // Mock the service method
        when(profileService.getProfileLoanOffers("1234567890")).thenReturn(Collections.singletonList(response));

        // Perform the request and verify the response
        mockMvc.perform(get("/v1/profile/1234567890/loan-offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].loanLimit").value(10000));

        // Verify that the service method was called
        verify(profileService, times(1)).getProfileLoanOffers("1234567890");
    }
}
