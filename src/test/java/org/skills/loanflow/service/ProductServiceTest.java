package org.skills.loanflow.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import org.skills.loanflow.entity.product.TenureDurationTypeEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 13:50
 */


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private StorageService storageService;
    @Mock
    private LoanFlowConfigs configs;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService productService;
    
    private ProductEntity productEntity;
    private ProductResponseDTO productResponseDTO;
    private TenureDurationTypeEntity tenureDurationTypeEntity;
    private FeeTypeEntity feeTypeEntity;

    @BeforeEach
    void setUp() {
        // Initialize test data
        tenureDurationTypeEntity = new TenureDurationTypeEntity();
        tenureDurationTypeEntity.setTenureDurationTypeId(1);
        tenureDurationTypeEntity.setTenureDurationType("MONTHS");

        feeTypeEntity = new FeeTypeEntity();
        feeTypeEntity.setFeeTypeId(1);
        feeTypeEntity.setFeeTypeName("SERVICE_FEE");
        feeTypeEntity.setFeeType("FIXED_AMOUNT");

        productEntity = new ProductEntity();
        productEntity.setProductId(1L);
        productEntity.setName("Personal Loan");
        productEntity.setTenureDurationTypeEntity(tenureDurationTypeEntity);

        productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setProductId(1L);
        productResponseDTO.setName("Personal Loan");
    }

    @Test
    @DisplayName("Fetch Tenure Duration Types - Success")
    void testFetchTenureDurationTypes() {
        // Mock data
        List<TenureDurationTypeEntity> tenureTypes = Collections.singletonList(tenureDurationTypeEntity);
        when(storageService.fetchTenureDurationType()).thenReturn(tenureTypes);

        // Call the method
        List<GenericResponseDTO> result = productService.fetchTenureDurationTypes();

        // Verify the result
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("MONTHS", result.get(0).getName());

        // Verify the interaction
        verify(storageService, times(1)).fetchTenureDurationType();
    }

    @Test
    @DisplayName("Fetch Fee Types - Success")
    void testFetchFeeTypes() {
        // Mock data
        List<FeeTypeEntity> feeTypes = Collections.singletonList(feeTypeEntity);
        when(storageService.fetchFeeTypes()).thenReturn(feeTypes);

        // Call the method
        List<FeeTypeResponseDTO> result = productService.fetchFeeTypes();

        // Verify the result
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("SERVICE_FEE", result.get(0).getName());

        // Verify the interaction
        verify(storageService, times(1)).fetchFeeTypes();
    }

    @Test
    @DisplayName("Create Product - Success")
    void testCreateProduct() {
        // Mock data
        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Personal Loan");
        request.setTenureDurationTypeID(1);
        request.setFees(List.of(new FeeRequestDTO(1, 100.0, 7D)));

        // Mock the first call: map ProductRequestDTO to ProductEntity
        when(modelMapper.map(request, ProductEntity.class)).thenReturn(productEntity);

        // Mock the second call: map ProductEntity to ProductResponseDTO
        when(modelMapper.map(productEntity, ProductResponseDTO.class)).thenReturn(productResponseDTO);

        // Mock service methods
        when(storageService.fetchTenureDurationTypeById(1)).thenReturn(tenureDurationTypeEntity);
        when(storageService.fetchFeeTypeById(1)).thenReturn(feeTypeEntity);
        when(storageService.createProduct(any(ProductEntity.class))).thenReturn(productEntity);
        when(configs.getFixedAmountLoanType()).thenReturn("FIXED_AMOUNT");

        // Call the method
        ProductResponseDTO result = productService.createProduct(request);

        // Verify the result
        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        assertEquals("Personal Loan", result.getName());

        // Verify the interactions
        verify(storageService, times(1)).fetchTenureDurationTypeById(1);
        verify(storageService, times(1)).fetchFeeTypeById(1);
        verify(storageService, times(1)).createProduct(any(ProductEntity.class));
        verify(modelMapper, times(1)).map(request, ProductEntity.class);
        verify(modelMapper, times(1)).map(productEntity, ProductResponseDTO.class);
    }

    @Test
    @DisplayName("Attach Fee to Product - Success")
    void testAttachFeeToProduct() {
        // Mock data
        FeeRequestDTO request = new FeeRequestDTO(1, 100.0, 5D);

        when(storageService.findProductByID(1L)).thenReturn(productEntity);
        when(storageService.fetchFeeTypeById(1)).thenReturn(feeTypeEntity);
        when(modelMapper.map(productEntity, ProductResponseDTO.class)).thenReturn(productResponseDTO);
        when(configs.getFixedAmountLoanType()).thenReturn("FIXED_AMOUNT");

        // Call the method
        ProductResponseDTO result = productService.attachFeeToProduct(1L, request);

        // Verify the result
        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        assertEquals("Personal Loan", result.getName());

        // Verify the interactions
        verify(storageService, times(1)).findProductByID(1L);
        verify(storageService, times(1)).fetchFeeTypeById(1);
        verify(storageService, times(1)).createProductFee(any(ProductFeeEntity.class));
        verify(modelMapper, times(1)).map(productEntity, ProductResponseDTO.class);
    }

    @Test
    @DisplayName("Get Product By ID - Success")
    void testGetProductById() {
        // Mock data
        when(storageService.findProductByID(1L)).thenReturn(productEntity);
        when(modelMapper.map(productEntity, ProductResponseDTO.class)).thenReturn(productResponseDTO);

        // Call the method
        ProductResponseDTO result = productService.getProductById(1L);

        // Verify the result
        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        assertEquals("Personal Loan", result.getName());

        // Verify the interactions
        verify(storageService, times(1)).findProductByID(1L);
        verify(modelMapper, times(1)).map(productEntity, ProductResponseDTO.class);
    }
}
