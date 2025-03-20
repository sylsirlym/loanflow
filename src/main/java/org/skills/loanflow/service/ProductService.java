package org.skills.loanflow.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.skills.loanflow.dto.products.ProductRequestDTO;
import org.skills.loanflow.dto.products.ProductResponseDTO;
import org.skills.loanflow.dto.products.TenureDurationTypeDTO;
import org.skills.loanflow.entity.ProductEntity;
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

    public List<TenureDurationTypeDTO> fetchTenureDurationTypes() {
        var tenureTypes = storageService.fetchTenureDurationType();
        return tenureTypes.stream().map(
                tenureType -> modelMapper.map(tenureType, TenureDurationTypeDTO.class)
        ).toList();
    }

    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        var product = modelMapper.map(productRequestDTO, ProductEntity.class);
        product.setTenureDurationTypeEntity(storageService.fetchTenureDurationTypeById(productRequestDTO.getTenureDurationTypeID()));
        var savedProduct = storageService.createProduct(product);
        return modelMapper.map(savedProduct, ProductResponseDTO.class);
    }

    public ProductResponseDTO getProductById(Long id) {
        var product = storageService.findProductByID(id);
        return modelMapper.map(product, ProductResponseDTO.class);
    }
}
