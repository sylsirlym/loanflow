package org.skills.loanflow.service;

import lombok.RequiredArgsConstructor;
import org.skills.loanflow.entity.customer.LoanOfferEntity;
import org.skills.loanflow.entity.customer.ProfileEntity;
import org.skills.loanflow.entity.loan.DisbursementEntity;
import org.skills.loanflow.entity.loan.LoanEntity;
import org.skills.loanflow.entity.product.FeeTypeEntity;
import org.skills.loanflow.entity.product.ProductEntity;
import org.skills.loanflow.entity.product.ProductFeeEntity;
import org.skills.loanflow.entity.product.TenureDurationTypeEntity;
import org.skills.loanflow.exception.ResourceNotFoundException;
import org.skills.loanflow.repository.customer.LoanOfferRepository;
import org.skills.loanflow.repository.customer.ProfileRepository;
import org.skills.loanflow.repository.loan.DisbursementRepository;
import org.skills.loanflow.repository.loan.LoanRepository;
import org.skills.loanflow.repository.product.FeeTypeRepository;
import org.skills.loanflow.repository.product.ProductFeeRepository;
import org.skills.loanflow.repository.product.ProductRepository;
import org.skills.loanflow.repository.product.TenureDurationTypesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 19/03/2025
 * Time: 22:07
 */
@Service
@RequiredArgsConstructor
public class StorageService {
    private final ProductRepository productRepository;
    private final TenureDurationTypesRepository tenureDurationTypesRepository;
    private final FeeTypeRepository feeTypeRepository;
    private final ProductFeeRepository productFeeRepository;
    private final ProfileRepository profileRepository;
    private final LoanOfferRepository loanOfferRepository;
    private final LoanRepository loanRepository;
    private final DisbursementRepository disbursementRepository;
    final

    ProductEntity createProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }
    List<ProductEntity> findProducts() {
        return productRepository.findAll();
    }
    ProductEntity findProductByID(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product with ID "+id+" not found"));
    }

    List<TenureDurationTypeEntity> fetchTenureDurationType() {
        return tenureDurationTypesRepository.findAll();
    }

    TenureDurationTypeEntity fetchTenureDurationTypeById(Integer id) {
        return tenureDurationTypesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tenure Duration Type with ID "+id+" not found"));
    }

    FeeTypeEntity fetchFeeTypeById(Integer id) {
        return feeTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fee type not found with ID: " + id));
    }

    FeeTypeEntity createFeeType(FeeTypeEntity feeTypeEntity) {
        return feeTypeRepository.save(feeTypeEntity);
    }

    void createProductFee(ProductFeeEntity productFeeEntity) {
        productFeeRepository.save(productFeeEntity);
    }
    List<FeeTypeEntity> fetchFeeTypes() {
        return feeTypeRepository.findAll();
    }

    ProfileEntity saveProfile(ProfileEntity profileEntity) {
        return profileRepository.save(profileEntity);
    }

    ProfileEntity findProfileByMsisdn(String msisdn) {
        return profileRepository.findByMsisdn(msisdn).orElseThrow(() -> new ResourceNotFoundException("Profile with MSISDN "+msisdn+" not found"));
    }

    LoanOfferEntity saveLoanOffer(LoanOfferEntity loanOfferEntity) {
        return loanOfferRepository.save(loanOfferEntity);
    }

    LoanOfferEntity findLoanOfferByID(Long id) {
        return loanOfferRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loan offer with ID "+id+" not found"));
    }
    LoanEntity saveLoan(LoanEntity loanEntity) {
       return loanRepository.save(loanEntity);
    }
    DisbursementEntity saveDisbursement(DisbursementEntity disbursementEntity) {
        return disbursementRepository.save(disbursementEntity);
    }
    List<DisbursementEntity> findUnprocessedDisbursements() {
        return disbursementRepository.findByScheduledDateAndIsDisbursedFalse(LocalDate.now());
    }
}
