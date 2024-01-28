package ecommerce.http.controllers.productSku;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import ecommerce.http.dtos.productSku.InsertNewSkuDto;
import ecommerce.http.dtos.productSku.UpdateProductSkuDto;
import ecommerce.http.entities.ProductSku;
import ecommerce.http.services.productSku.ProductSkuService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/sku")
public class ProductSkuController {
    private final ProductSkuService productSkuService;

    public ProductSkuController(ProductSkuService productSkuService) {
        this.productSkuService = productSkuService;
    }

    @PostMapping("/insert")
    public ResponseEntity<ProductSku> insertNewSkuOnProduct(
            @RequestBody @Valid InsertNewSkuDto insertNewSkuDto) {
        ProductSku newSku = this.productSkuService.insertNewSku(insertNewSkuDto.toProductSku());

        return ResponseEntity.status(201).body(newSku);
    }

    @PatchMapping("/update/{skuId}")
    public ResponseEntity<ProductSku> updateSkuInformations(
            @RequestBody @Valid UpdateProductSkuDto updateProductSkuDto,
            @PathVariable(name = "skuId", required = true) UUID skuId) {

        ProductSku productSkuUpdated =
                this.productSkuService.editSku(updateProductSkuDto.toProductSku(), skuId);

        return ResponseEntity.ok().body(productSkuUpdated);
    }

    @DeleteMapping("/delete/{skuId}")
    public ResponseEntity<Object> deleteSku(@PathVariable("skuId") UUID skuId) {
        this.productSkuService.deleteSku(skuId);

        return ResponseEntity.ok().build();
    }
}
