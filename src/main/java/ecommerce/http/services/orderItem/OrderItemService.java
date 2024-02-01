package ecommerce.http.services.orderItem;

import java.math.BigDecimal;
import java.util.Optional;
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
import ecommerce.http.repositories.OrderItemRepository;
import ecommerce.http.repositories.ProductRepository;
import ecommerce.http.repositories.ProductSkuRepository;

@Service
public class OrderItemService {
    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final OrderItemRepository orderItemRepository;

    @Autowired
    private final ProductSkuRepository productSkuRepository;

    public OrderItemService(ProductRepository productRepository,
            OrderItemRepository orderItemRepository, ProductSkuRepository productSkuRepository) {
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.productSkuRepository = productSkuRepository;

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

    public ProductSku handleOrderItemQuantity(OrderItem orderItem, String action) {
        if (orderItem == null) {
            throw new BadRequestException("Invalid order item");
        }

        Optional<OrderItem> doesItemExists = this.orderItemRepository.findById(orderItem.getId());

        if (doesItemExists.isEmpty()) {
            throw new NotFoundException("An item from this order wasn't found.");
        }

        OrderItem getItem = doesItemExists.get();

        Optional<ProductSku> getItemSku = this.productSkuRepository.findById(getItem.getSkuId());

        if (getItemSku.isEmpty()) {
            throw new NotFoundException("Item sku not found.");
        }

        if (action.equals("subtract")) {
            getItemSku.get().subtractQuantity(orderItem.getQuantity());
        }

        if (action.equals("increase")) {
            getItemSku.get().increaseQuantity(orderItem.getQuantity());
        }

        return getItemSku.get();
    }
}
