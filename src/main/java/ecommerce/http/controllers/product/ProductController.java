package ecommerce.http.controllers.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ecommerce.http.dtos.product.InsertNewProductDto;
import ecommerce.http.dtos.product.UpdateProductDto;
import ecommerce.http.entities.Product;
import ecommerce.http.services.product.ProductService;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Product> getById(@PathVariable("productId") String pathVariable) {

        Product findProduct = this.productService.filterById(pathVariable);

        return ResponseEntity.ok().body(findProduct);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Product>> getAll(@RequestParam("page") Integer page,
            @RequestParam("perPage") Integer perPage) {

        Page<Product> getAllProducts = this.productService.listAllProducts(page, perPage);

        return ResponseEntity.ok().body(getAllProducts);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@RequestBody UpdateProductDto updateProductDto,
            @PathVariable("productId") UUID productId) {

        Product editedProduct =
                this.productService.editProduct(updateProductDto.toProduct(productId));

        return ResponseEntity.ok().body(editedProduct);
    }
}