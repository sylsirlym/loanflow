package org.skills.loanflow.controller;

import lombok.RequiredArgsConstructor;
import org.skills.loanflow.dto.products.ProductRequestDTO;
import org.skills.loanflow.dto.products.ProductResponseDTO;
import org.skills.loanflow.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by sylvester
 * Email: musyokisyl81@gmail.com
 * Date: 19/03/2025
 * Time: 21:14
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/duration-types")
    public ResponseEntity<Object> fetchTenureDurationTypes() {
        var tenureDurationTypes = productService.fetchTenureDurationTypes();
        return ResponseEntity.status(HttpStatus.CREATED).body(tenureDurationTypes);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        var product = productService.createProduct(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

}
