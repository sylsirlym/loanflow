package org.skills.loanflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skills.loanflow.dto.product.request.FeeRequestDTO;
import org.skills.loanflow.dto.product.request.ProductRequestDTO;
import org.skills.loanflow.dto.product.response.GenericResponseDTO;
import org.skills.loanflow.dto.product.response.ProductResponseDTO;
import org.skills.loanflow.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 20/03/2025
 * Time: 12:32
 */


@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Test Fetch Tenure Duration Types")
    void testFetchTenureDurationTypes() throws Exception {
        // Mock data
        List<GenericResponseDTO> tenureDurationTypes = Arrays.asList(
                new GenericResponseDTO("1", "DAYS"),
                new GenericResponseDTO("2", "MONTHS")
        );

        // Mock service method
        when(productService.fetchTenureDurationTypes()).thenReturn(tenureDurationTypes);

        // Perform GET request and validate response
        mockMvc.perform(get("/v1/product/duration-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("DAYS"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("MONTHS"));
    }

    @Test
    @DisplayName("Test Fetch Fee Types")
    void testFetchFeeTypes() throws Exception {
        // Mock data
        List<GenericResponseDTO> feeTypes = Arrays.asList(
                new GenericResponseDTO("1", "SERVICE_FEE"),
                new GenericResponseDTO("2", "DAILY_FEE")
        );

        // Mock service method
        when(productService.fetchFeeTypes()).thenReturn(feeTypes);

        // Perform GET request and validate response
        mockMvc.perform(get("/v1/product/fee-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("SERVICE_FEE"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("DAILY_FEE"));
    }

    @Test
    @DisplayName("Test Create Product")
    void testCreateProduct() throws Exception {
        // Mock data
        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Personal Loan");
        request.setTenureDuration(12);
        request.setTenureDurationTypeID(1);
        request.setDaysAfterDueForFeeApplication(5);

        ProductResponseDTO response = new ProductResponseDTO();
        response.setProductId(1L);
        response.setName("Personal Loan");

        // Mock service method
        when(productService.createProduct(any(ProductRequestDTO.class))).thenReturn(response);

        // Perform POST request and validate response
        mockMvc.perform(post("/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.name").value("Personal Loan"));
    }

    @Test
    @DisplayName("Test Attach Fee")
    void testAttachFeeToProduct() throws Exception {
        // Mock data
        FeeRequestDTO request = new FeeRequestDTO();
        request.setFeeTypeId(1);
        request.setAmount(100.0);

        ProductResponseDTO response = new ProductResponseDTO();
        response.setProductId(1L);
        response.setName("Personal Loan");

        // Mock service method
        when(productService.attachFeeToProduct(any(Long.class), any(FeeRequestDTO.class))).thenReturn(response);

        // Perform POST request and validate response
        mockMvc.perform(post("/v1/product/1/attach-fee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.name").value("Personal Loan"));
    }

    @Test
    @DisplayName("Test Get Products")
    void testGetProducts() throws Exception {
        // Mock data
        ProductResponseDTO response = new ProductResponseDTO();
        response.setProductId(1L);
        response.setName("Personal Loan");
        response.setDaysAfterDueForFeeApplication(5);

        ProductResponseDTO response1 = new ProductResponseDTO();
        response1.setProductId(1L);
        response1.setName("Another Loan");
        response.setDaysAfterDueForFeeApplication(15);

        List<ProductResponseDTO> products = Arrays.asList(response,response1);

        // Mock service method
        when(productService.getProducts()).thenReturn(products);

        // Perform GET request and validate response
        mockMvc.perform(get("/v1/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].name").value("Personal Loan"));
    }

    @Test
    @DisplayName("Test Get Product")
    void testGetProduct() throws Exception {
        // Mock data
        ProductResponseDTO response = new ProductResponseDTO();
        response.setProductId(1L);
        response.setName("Personal Loan");

        // Mock service method
        when(productService.getProductById(1L)).thenReturn(response);

        // Perform GET request and validate response
        mockMvc.perform(get("/v1/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.name").value("Personal Loan"));
    }
}
