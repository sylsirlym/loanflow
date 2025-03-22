package org.skills.loanflow.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skills.loanflow.entity.product.FeeTypeEntity;
import org.skills.loanflow.entity.product.ProductEntity;
import org.skills.loanflow.entity.product.ProductFeeEntity;
import org.skills.loanflow.entity.product.TenureDurationTypeEntity;
import org.skills.loanflow.repository.product.FeeTypeRepository;
import org.skills.loanflow.repository.product.ProductFeeRepository;
import org.skills.loanflow.repository.product.ProductRepository;
import org.skills.loanflow.repository.product.TenureDurationTypesRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 13:58
 */


@ExtendWith(MockitoExtension.class)
class StorageServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private TenureDurationTypesRepository tenureDurationTypesRepository;

    @Mock
    private FeeTypeRepository feeTypeRepository;

    @Mock
    private ProductFeeRepository productFeeRepository;

    @InjectMocks
    private StorageService storageService;

    private ProductEntity productEntity;
    private TenureDurationTypeEntity tenureDurationTypeEntity;
    private FeeTypeEntity feeTypeEntity;
    private ProductFeeEntity productFeeEntity;

    @BeforeEach
    void setUp() {
        // Initialize test data
        tenureDurationTypeEntity = new TenureDurationTypeEntity();
        tenureDurationTypeEntity.setTenureDurationTypeId(1);
        tenureDurationTypeEntity.setTenureDurationType("MONTHS");

        feeTypeEntity = new FeeTypeEntity();
        feeTypeEntity.setFeeTypeId(1);
        feeTypeEntity.setFeeTypeName("SERVICE_FEE");

        productEntity = new ProductEntity();
        productEntity.setProductId(1L);
        productEntity.setName("Personal Loan");

        productFeeEntity = ProductFeeEntity.builder()
                .productEntity(productEntity)
                .feeTypeEntity(feeTypeEntity)
                .feeAmount(100.0)
                .feeRate(5D)
                .build();
    }

    @Test
    @DisplayName("Create Product - Success")
    void testCreateProduct() {
        // Mock repository
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        // Call the method
        ProductEntity result = storageService.createProduct(productEntity);

        // Verify the result
        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        assertEquals("Personal Loan", result.getName());

        // Verify the interaction
        verify(productRepository, times(1)).save(productEntity);
    }

    @Test
    @DisplayName("Find Product By ID - Success")
    void testFindProductByID() {
        // Mock repository
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));

        // Call the method
        ProductEntity result = storageService.findProductByID(1L);

        // Verify the result
        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        assertEquals("Personal Loan", result.getName());

        // Verify the interaction
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Fetch Tenure Duration Types - Success")
    void testFetchTenureDurationType() {
        // Mock repository
        when(tenureDurationTypesRepository.findAll()).thenReturn(Collections.singletonList(tenureDurationTypeEntity));

        // Call the method
        List<TenureDurationTypeEntity> result = storageService.fetchTenureDurationType();

        // Verify the result
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getTenureDurationTypeId());
        assertEquals("MONTHS", result.get(0).getTenureDurationType());

        // Verify the interaction
        verify(tenureDurationTypesRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Fetch Fee Type By ID - Success")
    void testFetchFeeTypeById() {
        // Mock repository
        when(feeTypeRepository.findById(1)).thenReturn(Optional.of(feeTypeEntity));

        // Call the method
        FeeTypeEntity result = storageService.fetchFeeTypeById(1);

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.getFeeTypeId());
        assertEquals("SERVICE_FEE", result.getFeeTypeName());

        // Verify the interaction
        verify(feeTypeRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Create Product Fee - Success")
    void testCreateProductFee() {
        // Mock repository
        when(productFeeRepository.save(any(ProductFeeEntity.class))).thenReturn(productFeeEntity);

        // Call the method
        storageService.createProductFee(productFeeEntity);

        // Verify the interaction
        verify(productFeeRepository, times(1)).save(productFeeEntity);
    }
}
