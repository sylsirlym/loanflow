package org.skills.loanflow.service;

import lombok.RequiredArgsConstructor;
import org.skills.loanflow.entity.ProductEntity;
import org.skills.loanflow.entity.TenureDurationTypeEntity;
import org.skills.loanflow.exception.ResourceNotFoundException;
import org.skills.loanflow.repository.ProductRepository;
import org.skills.loanflow.repository.TenureDurationTypesRepository;
import org.springframework.stereotype.Service;

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

    ProductEntity createProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
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


}
