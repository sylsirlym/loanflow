package org.skills.loanflow.configs;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.skills.loanflow.dto.products.response.FeeResponseDTO;
import org.skills.loanflow.dto.products.response.ProductResponseDTO;
import org.skills.loanflow.entity.ProductEntity;
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
                            .currency(productFeeEntity.getFeeCurrency())
                            .feeType(productFeeEntity.getFeeTypeEntity().getFeeTypeName())
                            .build()
                    ).toList();

            return ProductResponseDTO
                    .builder()
                    .productId(productEntity.getProductId())
                    .name(productEntity.getName())
                    .tenure(generateTenure(productEntity.getTenureDuration(),productEntity.getTenureDurationTypeEntity().getTenureDurationType()))
                    .daysAfterDueForFeeApplication(productEntity.getDaysAfterDueForFeeApplication())
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


}
