package org.skills.loanflow.configs;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.skills.loanflow.dto.loan.response.LoanResponseDTO;
import org.skills.loanflow.dto.product.response.FeeResponseDTO;
import org.skills.loanflow.dto.product.response.ProductResponseDTO;
import org.skills.loanflow.dto.profile.response.ProfileResponseDTO;
import org.skills.loanflow.entity.customer.ProfileEntity;
import org.skills.loanflow.entity.loan.LoanEntity;
import org.skills.loanflow.entity.product.ProductEntity;
import org.springframework.context.annotation.Bean;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 11:23
 */
@org.springframework.context.annotation.Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        modelMapper.addConverter(new ProductEntityToProductDTOConverter());
        modelMapper.addConverter(new ProfileEntityToProfileResponseDTOConverter());
        modelMapper.addConverter(new LoanEntityToLoanResponseDTOConverter());
        return modelMapper;
    }

    private static class ProductEntityToProductDTOConverter implements Converter<ProductEntity, ProductResponseDTO> {
        @Override
        public ProductResponseDTO convert(MappingContext<ProductEntity, ProductResponseDTO> mappingContext) {
            var productEntity = mappingContext.getSource();
            //Create Fees
            var fees = productEntity.getProductFees().stream()
                    .map(productFeeEntity -> FeeResponseDTO
                            .builder()
                            .amount(productFeeEntity.getFeeAmount())
                            .rate(productFeeEntity.getFeeRate())
                            .feeType(productFeeEntity.getFeeTypeEntity().getFeeType())
                            .feeTypeName(productFeeEntity.getFeeTypeEntity().getFeeTypeName())
                            .whenToCharge(productFeeEntity.getFeeTypeEntity().getWhenToCharge())
                            .build()
                    ).toList();

            return ProductResponseDTO
                    .builder()
                    .productId(productEntity.getProductId())
                    .name(productEntity.getName())
                    .tenure(generateTenure(productEntity.getTenureDuration(),productEntity.getTenureDurationTypeEntity().getTenureDurationType()))
                    .daysAfterDueForFeeApplication(productEntity.getDaysAfterDueForFeeApplication())
                    .disbursementType(productEntity.getDisbursementType())
                    .disbursementIntervalInDays(productEntity.getDisbursementIntervalInDays())
                    .billingCycle(productEntity.getBillingCycle())
                    .fees(fees)
                    .build();
        }

        private static String generateTenure(int duration, String type){
            if(duration > 1){
                return String.format("%d %ss", duration,type);
            }
            return String.format("%d %s", duration,type);
        }
    }

    private static class ProfileEntityToProfileResponseDTOConverter implements Converter<ProfileEntity, ProfileResponseDTO> {
        @Override
        public ProfileResponseDTO convert(MappingContext<ProfileEntity, ProfileResponseDTO> mappingContext) {
            var profileEntity = mappingContext.getSource();
            var customer = profileEntity.getCustomer();
            return ProfileResponseDTO
                    .builder()
                    .profileId(profileEntity.getProfileId())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .address(customer.getAddress())
                    .email(customer.getEmail())
                    .creditScore(profileEntity.getCreditScore())
                    .pinStatus(profileEntity.getPinStatus())
                    .msisdn(profileEntity.getMsisdn())
                    .build();
        }
    }

    private static class LoanEntityToLoanResponseDTOConverter implements Converter<LoanEntity, LoanResponseDTO>{
        @Override
        public LoanResponseDTO convert(MappingContext<LoanEntity, LoanResponseDTO> mappingContext) {
            var loan = mappingContext.getSource();
            var loanResponse = new LoanResponseDTO();
            loanResponse.setLoanName(loan.getLoanOffer().getProduct().getName());
            loanResponse.setPrincipal(loan.getPrincipal());
            loanResponse.setPrincipalDisbursed(loan.getNetDisbursedAmount());
            loanResponse.setLoanState(loan.getLoanState());
            loanResponse.setFullyDisbursed(loan.isFullyDisbursed());
            return loanResponse;
        }
    }

}
