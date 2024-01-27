package ecommerce.http.controllers.product;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ecommerce.http.dtos.product.InsertNewProductDto;
import ecommerce.http.dtos.product.UpdateProductDto;
import ecommerce.http.entities.Product;
import ecommerce.http.services.ProductSku.ProductSkuService;
import ecommerce.http.services.product.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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


    @Autowired
    private final ProductSkuService productSkuService;

    public ProductController(ProductService productService, ProductSkuService productSkuService) {
        this.productService = productService;
        this.productSkuService = productSkuService;
    }

    @PostMapping("/insert")
    public ResponseEntity<Map<String, String>> insertNewProduct(
            @RequestBody @Valid InsertNewProductDto insertNewProductDto) {

        this.productService.insertProduct(insertNewProductDto.toProduct(),
                insertNewProductDto.toProductSku());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("message", "Product inserted successfully."));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getById(@PathVariable("productId") String pathVariable) {

        Product findProduct = this.productService.filterById(pathVariable);

        return ResponseEntity.ok().body(findProduct);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<?>> getAll(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "perPage", required = false) Integer perPage,
            @RequestParam(name = "active", required = false) Boolean active,
            @RequestParam(name = "color", required = false) String color,
            @RequestParam(name = "name", required = false) String name) {

        Page<?> getAllProducts;

        if (color != null) {
            getAllProducts = this.productSkuService.filterProductSkuBy(name, color);

        } else {
            getAllProducts =
                    this.productService.listAllProducts(page, perPage, active, color, name);

        }

        return ResponseEntity.ok().body(getAllProducts);
    }


    @PatchMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @RequestBody @Valid UpdateProductDto updateProductDto,
            @PathVariable("productId") UUID productId) {
        Product editedProduct =
                this.productService.editProduct(updateProductDto.toProduct(productId));

        return ResponseEntity.ok().body(editedProduct);
    }


    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Map<String, String>> deleteProduct(
            @PathVariable("productId") @Valid UUID productId) {
        this.productService.deleteAProduct(productId);

        return ResponseEntity.ok()
                .body(Collections.singletonMap("message", "Product deleted successfully!"));
    }
}
