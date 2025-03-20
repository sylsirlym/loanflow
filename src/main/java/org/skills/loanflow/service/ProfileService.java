package org.skills.loanflow.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.skills.loanflow.dto.profile.request.LoanOfferRequestDTO;
import org.skills.loanflow.dto.profile.request.ProfileRequestDTO;
import org.skills.loanflow.dto.profile.response.LoanOfferResponseDTO;
import org.skills.loanflow.dto.profile.response.ProfileResponseDTO;
import org.skills.loanflow.entity.customer.CustomerEntity;
import org.skills.loanflow.entity.customer.LoanOfferEntity;
import org.skills.loanflow.entity.customer.ProfileEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 22:32
 */
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ModelMapper modelMapper;
    private final StorageService storageService;

    @Transactional
    public ProfileResponseDTO createProfile(ProfileRequestDTO profileRequestDTO) {
        // Create and save the customer using builder
        var customer = modelMapper.map(profileRequestDTO, CustomerEntity.class);

        // Create and save the profile using builder
        var profile = modelMapper.map(profileRequestDTO, ProfileEntity.class);
        profile.setCustomer(customer);
        profile.setPinStatus(1);
        var savedProfile = storageService.saveProfile(profile);

        // Map the saved profile to a DTO and return it
        return modelMapper.map(savedProfile, ProfileResponseDTO.class);
    }

    /**
     * Fetch a profile by MSISDN.
     */
    public ProfileResponseDTO getProfileByMsisdn(String msisdn) {
        var profile = storageService.findProfileByMsisdn(msisdn);
        return modelMapper.map(profile, ProfileResponseDTO.class);
    }

    /**
     * Update a profile's credit score.
     */
    @Transactional
    public ProfileResponseDTO updateProfileCreditScore(String msisdn, BigDecimal newCreditScore) {
        var profile = storageService.findProfileByMsisdn(msisdn);
        // Update the credit score
        profile.setCreditScore(newCreditScore);

        // Save the updated profile
        var updatedProfile = storageService.saveProfile(profile);

        // Map the updated entity to a DTO and return it
        return modelMapper.map(updatedProfile, ProfileResponseDTO.class);
    }

    public LoanOfferResponseDTO createProfileOffer(String msisdn, LoanOfferRequestDTO loanOfferRequestDTO) {
        var profile = storageService.findProfileByMsisdn(msisdn);
        var product = storageService.findProductByID(loanOfferRequestDTO.getProductId());
        var loanOffer = modelMapper.map(loanOfferRequestDTO, LoanOfferEntity.class);
        loanOffer.setProfile(profile);
        loanOffer.setProduct(product);
        var savedLoanOffer = storageService.saveLoanOffer(loanOffer);
        return modelMapper.map(savedLoanOffer, LoanOfferResponseDTO.class);
    }
    /**
     * Fetch loan offers for a profile.
     */
    public List<LoanOfferResponseDTO> getProfileLoanOffers(String msisdn) {
        var profile = storageService.findProfileByMsisdn(msisdn);
        return profile.getLoanOffers().stream()
                .map(loanOffer -> modelMapper.map(loanOffer, LoanOfferResponseDTO.class))
                .toList();
    }
}
