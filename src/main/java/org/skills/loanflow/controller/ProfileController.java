package org.skills.loanflow.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.skills.loanflow.dto.profile.request.LoanOfferRequestDTO;
import org.skills.loanflow.dto.profile.request.ProfileRequestDTO;
import org.skills.loanflow.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 21:57
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/profile")
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<Object> createProfile(@Valid @RequestBody ProfileRequestDTO profileRequestDTO) {
        var profile = profileService.createProfile(profileRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(profile);
    }

    /**
     * Fetch a profile by MSISDN.
     */
    @GetMapping("/{msisdn}")
    public ResponseEntity<Object> getProfileByMsisdn(@PathVariable String msisdn) {
        var profile = profileService.getProfileByMsisdn(msisdn);
        return ResponseEntity.ok(profile);
    }

    /**
     * Update a profile's credit score.
     */
    @PutMapping("/{msisdn}/credit-score")
    public ResponseEntity<Object> updateProfileCreditScore(
            @PathVariable String msisdn,
            @RequestParam BigDecimal newCreditScore) {
        var profile = profileService.updateProfileCreditScore(msisdn, newCreditScore);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/{msisdn}/loan-offers")
    public ResponseEntity<Object> createProfileLoanOffers(@PathVariable String msisdn, @RequestBody LoanOfferRequestDTO loanOfferRequestDTO) {
        var loanOffers = profileService.createProfileOffer(msisdn, loanOfferRequestDTO);
        return ResponseEntity.ok(loanOffers);
    }

    /**
     * Fetch loan offers for a profile.
     */
    @GetMapping("/{msisdn}/loan-offers")
    public ResponseEntity<Object> getProfileLoanOffers(@PathVariable String msisdn) {
        var loanOffers = profileService.getProfileLoanOffers(msisdn);
        return ResponseEntity.ok(loanOffers);
    }
}
