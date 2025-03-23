package org.skills.loanflow.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.skills.loanflow.dto.product.request.FeeRequestDTO;
import org.skills.loanflow.dto.product.request.ProductRequestDTO;
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
@RequestMapping("/v1/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/duration-types")
    public ResponseEntity<Object> fetchTenureDurationTypes() {
        var tenureDurationTypes = productService.fetchTenureDurationTypes();
        return ResponseEntity.ok(tenureDurationTypes);
    }

    @GetMapping("/fee-types")
    public ResponseEntity<Object> fetchFeeTypes() {
        var feeTypes = productService.fetchFeeTypes();
        return ResponseEntity.ok(feeTypes);
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        var product = productService.createProduct(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping
    public ResponseEntity<Object> getProducts() {
        var product = productService.getProducts();
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable Long id) {
        var product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/{id}/attach-fee")
    public ResponseEntity<Object> attachFeeToProduct(@RequestBody FeeRequestDTO request, @PathVariable Long id) {
        var product = productService.attachFeeToProduct(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
}
