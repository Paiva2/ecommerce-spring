package ecommerce.http.entities;

import java.util.UUID;

import jakarta.persistence.CascadeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private UUID skuId;

    @Column(nullable = false)
    private String skuColor;

    @Column(nullable = false)
    private String skuSize;

    @Column(nullable = false)
    private String skuGender;

    @Column(nullable = false)
    private Boolean wasOnSale;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(targetEntity = Order.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem() {}

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public UUID getSkuId() {
        return skuId;
    }

    public void setSkuId(UUID skuId) {
        this.skuId = skuId;
    }

    public String getSkuColor() {
        return skuColor;
    }

    public void setSkuColor(String skuColor) {
        this.skuColor = skuColor;
    }

    public String getSkuSize() {
        return skuSize;
    }

    public void setSkuSize(String skuSize) {
        this.skuSize = skuSize;
    }

    public String getSkuGender() {
        return skuGender;
    }

    public void setSkuGender(String skuGender) {
        this.skuGender = skuGender;
    }

    public Boolean getWasOnSale() {
        return wasOnSale;
    }

    public void setWasOnSale(Boolean wasOnSale) {
        this.wasOnSale = wasOnSale;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
