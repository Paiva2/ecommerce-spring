package ecommerce.http.services.orderItem;

import java.math.BigDecimal;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.http.entities.Order;
import ecommerce.http.entities.OrderItem;
import ecommerce.http.entities.Product;
import ecommerce.http.entities.ProductSku;

import ecommerce.http.exceptions.BadRequestException;
import ecommerce.http.exceptions.ConflictException;
import ecommerce.http.exceptions.NotFoundException;

import ecommerce.http.repositories.ProductRepository;

@Service
public class OrderItemService {
    @Autowired
    private final ProductRepository productRepository;

    public OrderItemService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public OrderItem newOrderItem(Order order, OrderItem orderItem, Set<ProductSku> orderSkuList) {
        if (order == null || orderItem.getProductId() == null) {
            throw new BadRequestException("Invalid order.");
        }

        if (orderItem == null || orderItem.getSkuId() == null) {
            throw new BadRequestException("Invalid order item.");
        }

        Product orderProduct = this.productRepository
                .findByIdAndSku(orderItem.getProductId(), orderItem.getSkuId()).orElse(null);

        if (orderProduct == null) {
            throw new NotFoundException("Product and sku, not found.");
        }

        BigDecimal orderQuantity = new BigDecimal(orderItem.getQuantity());

        ProductSku orderSku = orderProduct.getSkus().iterator().next();

        if (Integer.valueOf(orderQuantity.toString()) > orderSku.getQuantity()) {
            throw new ConflictException(
                    "Quantity of product " + orderProduct.getName() + " isn't available.");
        }

        orderItem.setOrder(order);

        orderItem.setProductName(orderProduct.getName());
        orderItem.setWasOnSale(orderSku.getIsOnSale());
        orderItem.setSkuColor(orderSku.getColor());
        orderItem.setSkuGender(orderSku.getGender().toString());
        orderItem.setSkuSize(orderSku.getSize());

        BigDecimal getItemValue =
                orderSku.getIsOnSale() ? orderSku.getPriceOnSale() : orderSku.getPrice();

        BigDecimal itemValueTotal = getItemValue.multiply(orderQuantity);

        order.incrementTotal(itemValueTotal);

        orderSku.subtractQuantity(orderItem.getQuantity());

        orderSkuList.add(orderSku);

        return orderItem;
    }
}
