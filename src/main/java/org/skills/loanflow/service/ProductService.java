package org.skills.loanflow.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.skills.loanflow.dto.product.request.FeeRequestDTO;
import org.skills.loanflow.dto.product.request.ProductRequestDTO;
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

    public List<GenericResponseDTO> fetchTenureDurationTypes() {
        var tenureTypes = storageService.fetchTenureDurationType();
        return tenureTypes.stream().map(
                tenureType -> GenericResponseDTO
                        .builder()
                        .id(String.valueOf(tenureType.getTenureDurationTypeId()))
                        .name(tenureType.getTenureDurationType()).build()
        ).toList();
    }

    public List<GenericResponseDTO> fetchFeeTypes() {
        var fetchFeeTypes = storageService.fetchFeeTypes();
        return fetchFeeTypes.stream().map(
                feeType -> GenericResponseDTO
                        .builder()
                        .id(String.valueOf(feeType.getFeeTypeId()))
                        .name(feeType.getFeeTypeName()).build()
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
                var productFee = ProductFeeEntity
                        .builder()
                        .productEntity(product)
                        .feeTypeEntity(feeType)
                        .feeAmount(feeRequest.getAmount())
                        .feeCurrency(feeRequest.getCurrency())
                        .build();
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
        ProductEntity product = storageService.findProductByID(productID);
        FeeTypeEntity feeType = storageService.fetchFeeTypeById(attachFeeRequestDTO.getFeeTypeId());

        // Create the mapping
        var productFee = ProductFeeEntity
                .builder()
                .productEntity(product)
                .feeTypeEntity(feeType)
                .feeCurrency(attachFeeRequestDTO.getCurrency())
                .feeAmount(attachFeeRequestDTO.getAmount())
                .build();

        // Save the mapping
        storageService.createProductFee(productFee);
        return modelMapper.map(product, ProductResponseDTO.class);
    }
}
