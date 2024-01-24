package ecommerce.http.controllers.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ecommerce.http.dtos.product.InsertNewProductDto;
import ecommerce.http.services.product.ProductService;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/insert")
    public ResponseEntity<Map<String, String>> insertNewProduct(
            @RequestBody @Valid InsertNewProductDto insertNewProductDto) {
        this.productService.insertProduct(insertNewProductDto.toProduct());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("message", "Product inserted successfully."));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Map<String, String>> getById(
            @PathVariable("productId") String pathVariable) {

        Map<String, String> findProduct = this.productService.filterById(pathVariable);

        return ResponseEntity.ok().body(findProduct);
    }
}
