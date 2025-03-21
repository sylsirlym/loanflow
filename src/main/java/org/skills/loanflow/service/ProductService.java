package org.skills.loanflow.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.skills.loanflow.configs.LoanFlowConfigs;
import org.skills.loanflow.dto.product.request.FeeRequestDTO;
import org.skills.loanflow.dto.product.request.ProductRequestDTO;
import org.skills.loanflow.dto.product.response.FeeTypeResponseDTO;
import org.skills.loanflow.dto.product.response.GenericResponseDTO;
import org.skills.loanflow.dto.product.response.ProductResponseDTO;
import org.skills.loanflow.entity.product.FeeTypeEntity;
import org.skills.loanflow.entity.product.ProductEntity;
import org.skills.loanflow.entity.product.ProductFeeEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 19/03/2025
 * Time: 21:50
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    private final StorageService storageService;
    private final ModelMapper modelMapper;
    private final LoanFlowConfigs configs;

    public List<GenericResponseDTO> fetchTenureDurationTypes() {
        var tenureTypes = storageService.fetchTenureDurationType();
        return tenureTypes.stream().map(
                tenureType -> GenericResponseDTO
                        .builder()
                        .id(String.valueOf(tenureType.getTenureDurationTypeId()))
                        .name(tenureType.getTenureDurationType()).build()
        ).toList();
    }

    public List<FeeTypeResponseDTO> fetchFeeTypes() {
        var fetchFeeTypes = storageService.fetchFeeTypes();
        return fetchFeeTypes.stream().map(
                feeType -> FeeTypeResponseDTO
                        .builder()
                        .id(String.valueOf(feeType.getFeeTypeId()))
                        .name(feeType.getFeeTypeName())
                        .type(feeType.getFeeType())
                        .build()
        ).toList();
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        // Map ProductRequestDTO to ProductEntity
        var product = modelMapper.map(productRequestDTO, ProductEntity.class);

        // Fetch and set the TenureDurationTypeEntity
        product.setTenureDurationTypeEntity(
                storageService.fetchTenureDurationTypeById(productRequestDTO.getTenureDurationTypeID())
        );

        // Process and set the fees
        if (productRequestDTO.getFees() != null) {
            for (FeeRequestDTO feeRequest : productRequestDTO.getFees()) {
                // Fetch the FeeTypeEntity
                var feeType = storageService.fetchFeeTypeById(feeRequest.getFeeTypeId());

                // Create and associate the ProductFeeEntity
                var productFee = this.createProductFee(product,feeType,feeRequest);

                // Add the ProductFeeEntity to the ProductEntity's list
                product.getProductFees().add(productFee);
            }
        }

        // Save the product
        var savedProduct = storageService.createProduct(product);
        // Map the saved product to a ProductResponseDTO and return it
        return modelMapper.map(savedProduct, ProductResponseDTO.class);
    }

    public ProductResponseDTO getProductById(Long id) {
        var product = storageService.findProductByID(id);
        return modelMapper.map(product, ProductResponseDTO.class);
    }

    public List<ProductResponseDTO> getProducts() {
        return storageService.findProducts()
                .stream().map(
                        productEntity -> modelMapper.map(productEntity, ProductResponseDTO.class)
                ).toList();
    }

    @Transactional
    public ProductResponseDTO attachFeeToProduct(Long productID,FeeRequestDTO attachFeeRequestDTO) {
        // Fetch the product and fee type
        var product = storageService.findProductByID(productID);
        var feeType = storageService.fetchFeeTypeById(attachFeeRequestDTO.getFeeTypeId());

        // Create the mapping
        var productFee = this.createProductFee(product,feeType,attachFeeRequestDTO);

        // Save the mapping
        storageService.createProductFee(productFee);
        return modelMapper.map(product, ProductResponseDTO.class);
    }

    private ProductFeeEntity createProductFee(ProductEntity product, FeeTypeEntity feeType, FeeRequestDTO feeRequestDTO) {
        var productFeeBuilder = ProductFeeEntity
                .builder()
                .productEntity(product)
                .feeTypeEntity(feeType);
        if (feeType.getFeeType().equalsIgnoreCase(configs.getFixedAmountLoanType())) {
            productFeeBuilder.feeAmount(feeRequestDTO.getAmount());
        } else {
            productFeeBuilder.feeRate(feeRequestDTO.getRate());
        }
        return productFeeBuilder.build();
    }
}
