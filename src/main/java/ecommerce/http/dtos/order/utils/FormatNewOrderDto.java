package ecommerce.http.dtos.order.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import ecommerce.http.dtos.order.NewOrderItemsDto;
import ecommerce.http.entities.Order;
import ecommerce.http.entities.OrderItem;

public class FormatNewOrderDto {
    private Set<OrderItem> orderItems;

    private Set<NewOrderItemsDto> dtoItems;

    private Order newOrder;

    public FormatNewOrderDto(Set<NewOrderItemsDto> dtoItems) {
        this.dtoItems = dtoItems;
        this.newOrder = new Order();
        this.orderItems = new HashSet<>();
    }

    public void insertItemsOnOrder() {
        this.dtoItems.forEach(item -> {
            OrderItem orderItem = new OrderItem();

            orderItem.setSkuId(UUID.fromString(item.getSkuId()));
            orderItem.setProductId(UUID.fromString(item.getProductId()));
            orderItem.setQuantity(item.getQuantity());

            orderItems.add(orderItem);
        });

        this.newOrder.setItems(orderItems);
    }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public Order getOrder() {
        return this.newOrder;
    }
}
