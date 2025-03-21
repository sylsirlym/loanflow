package org.skills.loanflow.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.skills.loanflow.dto.profile.request.LoanOfferRequestDTO;
import org.skills.loanflow.dto.profile.request.ProfileRequestDTO;
import org.skills.loanflow.dto.profile.response.LoanOfferResponseDTO;
import org.skills.loanflow.dto.profile.response.ProfileResponseDTO;
import org.skills.loanflow.entity.customer.CustomerEntity;
import org.skills.loanflow.entity.customer.LoanOfferEntity;
import org.skills.loanflow.entity.customer.ProfileEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 21/03/2025
 * Time: 12:50
 */


@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private StorageService storageService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProfileService profileService;

    private ProfileRequestDTO profileRequestDTO;
    private ProfileEntity profileEntity;
    private CustomerEntity customerEntity;
    private ProfileResponseDTO profileResponseDTO;
    private LoanOfferResponseDTO loanOfferResponseDTO;

    @BeforeEach
    void setUp() {
        profileRequestDTO = new ProfileRequestDTO();
        profileRequestDTO.setMsisdn("1234567890");
        profileRequestDTO.setFirstName("John");
        profileRequestDTO.setLastName("Doe");
        profileRequestDTO.setEmail("john.doe@example.com");
        profileRequestDTO.setAddress("123 Main St");
        profileRequestDTO.setPinHash("hashedPin");
        profileRequestDTO.setCreditScore(BigDecimal.valueOf(750));

        customerEntity=new CustomerEntity();
        customerEntity.setFirstName("John");
        customerEntity.setLastName("Doe");
        customerEntity.setEmail("john.doe@example.com");
        customerEntity.setAddress("123 Main St");

        profileEntity = new ProfileEntity();
        profileEntity.setProfileId(1L);
        profileEntity.setMsisdn("1234567890");
        profileEntity.setCustomer(customerEntity);
        profileEntity.setPinHash("hashedPin");
        profileEntity.setCreditScore(BigDecimal.valueOf(750));

        profileResponseDTO = new ProfileResponseDTO();
        profileResponseDTO.setMsisdn("1234567890");
        profileResponseDTO.setFirstName("John");
        profileResponseDTO.setLastName("Doe");
        profileResponseDTO.setCreditScore(BigDecimal.valueOf(750));

        loanOfferResponseDTO = new LoanOfferResponseDTO();
        loanOfferResponseDTO.setLoanLimit(BigDecimal.valueOf(10000));
    }

    @Test
    void testCreateProfile() {

        // Mock the storageService to return the saved profile
        when(storageService.saveProfile(any(ProfileEntity.class))).thenReturn(profileEntity);
        when(modelMapper.map(profileRequestDTO, ProfileEntity.class)).thenReturn(profileEntity);
        when(modelMapper.map(profileRequestDTO, CustomerEntity.class)).thenReturn(customerEntity);
        when(modelMapper.map(profileEntity, ProfileResponseDTO.class)).thenReturn(profileResponseDTO);


        // Call the service method
        ProfileResponseDTO response = profileService.createProfile(profileRequestDTO);

        // Verify the result
        assertNotNull(response);
        assertEquals("1234567890", response.getMsisdn());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals(BigDecimal.valueOf(750), response.getCreditScore());

        // Verify that saveProfile was called
        verify(storageService, times(1)).saveProfile(any(ProfileEntity.class));
    }

    @Test
    void testGetProfileByMsisdn() {
        // Mock the storageService to return the profile
        when(storageService.findProfileByMsisdn("1234567890")).thenReturn(profileEntity);
        when(modelMapper.map(profileEntity, ProfileResponseDTO.class)).thenReturn(profileResponseDTO);

        // Call the service method
        ProfileResponseDTO response = profileService.getProfileByMsisdn("1234567890");

        // Verify the result
        assertNotNull(response);
        assertEquals("1234567890", response.getMsisdn());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());

        // Verify that findProfileByMsisdn was called
        verify(storageService, times(1)).findProfileByMsisdn("1234567890");
    }

    @Test
    void testUpdateProfileCreditScore() {
        // Mock the storageService to return the profile
        when(storageService.findProfileByMsisdn("1234567890")).thenReturn(profileEntity);
        when(storageService.saveProfile(any(ProfileEntity.class))).thenReturn(profileEntity);
        profileResponseDTO.setCreditScore(BigDecimal.valueOf(800));
        when(modelMapper.map(profileEntity, ProfileResponseDTO.class)).thenReturn(profileResponseDTO);
        // Call the service method
        ProfileResponseDTO response = profileService.updateProfileCreditScore("1234567890", BigDecimal.valueOf(800));

        // Verify the result
        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(800), response.getCreditScore());

        // Verify that saveProfile was called
        verify(storageService, times(1)).saveProfile(any(ProfileEntity.class));
    }

    @Test
    void testCreateProfileLoanOffers() {
        LoanOfferRequestDTO loanOfferRequestDTO = new LoanOfferRequestDTO();
        loanOfferRequestDTO.setProductId(1L);
        loanOfferRequestDTO.setLoanLimit(BigDecimal.valueOf(10000));

        LoanOfferEntity loanOfferEntity = new LoanOfferEntity();
        loanOfferEntity.setLoanOfferId(1L);
        loanOfferEntity.setLoanLimit(BigDecimal.valueOf(10000));




        // Mock the storageService to return the profile and save the loan offer
        when(storageService.findProfileByMsisdn("1234567890")).thenReturn(profileEntity);
        when(storageService.saveLoanOffer(any(LoanOfferEntity.class))).thenReturn(loanOfferEntity);
        when(modelMapper.map(loanOfferRequestDTO, LoanOfferEntity.class)).thenReturn(loanOfferEntity);
        when(modelMapper.map(loanOfferEntity, LoanOfferResponseDTO.class)).thenReturn(loanOfferResponseDTO);

        // Call the service method
        LoanOfferResponseDTO response = profileService.createProfileOffer("1234567890", loanOfferRequestDTO);

        // Verify the result
        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(10000), response.getLoanLimit());

        // Verify that saveLoanOffer was called
        verify(storageService, times(1)).saveLoanOffer(any(LoanOfferEntity.class));
    }

    @Test
    void testGetProfileLoanOffers() {
        LoanOfferEntity loanOfferEntity = new LoanOfferEntity();
        loanOfferEntity.setLoanOfferId(1L);
        loanOfferEntity.setLoanLimit(BigDecimal.valueOf(10000));
        profileEntity.setLoanOffers(Collections.singletonList(loanOfferEntity));

        // Mock the storageService to return the profile
        when(storageService.findProfileByMsisdn("1234567890")).thenReturn(profileEntity);
        when(modelMapper.map(loanOfferEntity, LoanOfferResponseDTO.class)).thenReturn(loanOfferResponseDTO);

        // Call the service method
        List<LoanOfferResponseDTO> response = profileService.getProfileLoanOffers("1234567890");

        // Verify the result
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(BigDecimal.valueOf(10000), response.get(0).getLoanLimit());

        // Verify that findProfileByMsisdn was called
        verify(storageService, times(1)).findProfileByMsisdn("1234567890");
    }
}
